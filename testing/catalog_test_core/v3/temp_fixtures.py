# Copyright © Amazon
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http:#www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""Job-scoped temporary fixture helpers for v3 acceptance tests."""

from __future__ import annotations

import copy
import json
import os
from typing import Any, Dict, List, Optional, Set, Tuple

DEFAULT_MANIFEST_NAME = ".temp_fixtures_manifest.json"

# Always clone these even when the shared id is indexed. Shared catalog geometry for
# antimeridian CRS (EPSG::3851) is too detailed / incompatible with points-in-aou
# assertions; the fixture MultiPolygon is required for a stable acceptance check.
FORCE_TEMP_ID_MARKERS = (
    ":reference-data--CoordinateReferenceSystem:Projected:EPSG::3851",
)


def job_id() -> str:
    return os.environ.get("CI_JOB_ID") or os.environ.get("CI_PIPELINE_ID") or "local"


def should_force_temporary_fixture(original_id: str) -> bool:
    return any(marker in original_id for marker in FORCE_TEMP_ID_MARKERS)


def temp_record_id(original_id: str, suffix: Optional[str] = None) -> str:
    return f"{original_id}--ci-{suffix or job_id()}"


def job_legal_tag_short_name(suffix: Optional[str] = None) -> str:
    return f"crs-catalog-int-test-{suffix or job_id()}"


def job_legal_tag_full_name(data_partition_id: str, suffix: Optional[str] = None) -> str:
    return f"{data_partition_id}-{job_legal_tag_short_name(suffix)}"


def default_manifest_path(base_dir: str) -> str:
    return os.path.join(base_dir, DEFAULT_MANIFEST_NAME)


def write_manifest(path: str, temporary_record_ids: List[str], legal_tag_name: Optional[str]) -> None:
    payload = {
        "temporary_record_ids": temporary_record_ids,
        "legal_tag_name": legal_tag_name,
    }
    with open(path, "w", encoding="utf-8") as manifest_file:
        json.dump(payload, manifest_file, indent=2)
        manifest_file.write("\n")


def read_manifest(path: str) -> Dict[str, Any]:
    if not os.path.exists(path):
        return {"temporary_record_ids": [], "legal_tag_name": None}
    with open(path, encoding="utf-8") as manifest_file:
        return json.load(manifest_file)


def replace_ids_in_value(value: Any, id_map: Dict[str, str]) -> Any:
    """Recursively replace original fixture ids with active ids in nested structures."""
    if isinstance(value, dict):
        return {key: replace_ids_in_value(item, id_map) for key, item in value.items()}
    if isinstance(value, list):
        return [replace_ids_in_value(item, id_map) for item in value]
    if isinstance(value, str):
        # Replace longer ids first so prefixes do not collide.
        for original_id, active_id in sorted(id_map.items(), key=lambda item: len(item[0]), reverse=True):
            if original_id in value:
                value = value.replace(original_id, active_id)
        return value
    return value


def substitute_fixture_ids(text: str, id_map: Dict[str, str]) -> str:
    for original_id, active_id in sorted(id_map.items(), key=lambda item: len(item[0]), reverse=True):
        text = text.replace(original_id, active_id)
    return text


def result_by_id(results: List[Dict[str, Any]], record_id: str) -> Dict[str, Any]:
    for result in results:
        if result.get("id") == record_id:
            return result
    raise AssertionError(f"No search result found with id {record_id}")


def prepare_active_fixtures(
    client,
    records_obj: List[Dict[str, Any]],
    data_partition_id: str,
    legal_tag_template_path: str,
    manifest_path: str,
) -> Tuple[Dict[str, str], Set[str], List[str], Optional[str]]:
    """
    Decide which fixtures to reuse vs clone as job-scoped temps.

    Returns:
      id_map: original_id -> active_id
      record_id_set: active ids used by tests
      temporary_record_ids: ids created by this run (for cleanup)
      legal_tag_short: short legal-tag name created for temps (or None)
    """
    suffix = job_id()
    id_map: Dict[str, str] = {}
    temporary_record_ids: List[str] = []
    missing_originals: List[Dict[str, Any]] = []

    for record in records_obj:
        original_id = record["id"]
        force_temp = should_force_temporary_fixture(original_id)
        if not force_temp and client.is_record_indexed(original_id):
            id_map[original_id] = original_id
            print(f"Reusing indexed fixture {original_id}")
        else:
            active_id = temp_record_id(original_id, suffix)
            id_map[original_id] = active_id
            temporary_record_ids.append(active_id)
            missing_originals.append(record)
            if force_temp:
                print(
                    f"Forcing temporary fixture for geometry-sensitive id {original_id} "
                    f"-> {active_id}"
                )
            else:
                print(f"Fixture {original_id} not indexed; will create temporary {active_id}")

    legal_tag_short = None
    if temporary_record_ids:
        legal_tag_short = job_legal_tag_short_name(suffix)
        legal_tag_full = job_legal_tag_full_name(data_partition_id, suffix)

        with open(legal_tag_template_path, encoding="utf-8") as legal_tag_file:
            legal_tag = legal_tag_file.read().replace("{{legal_tag_name}}", legal_tag_short)
        legal_response = client.make_request("POST", "/api/legal/v1/legaltags", legal_tag)
        if legal_response.status_code not in [201, 409]:
            raise Exception(
                f"Could not create legal tag. Received {legal_response.status_code} from legal service"
            )

        # Persist cleanup targets before upload so EXIT trap can still clean if PUT fails.
        write_manifest(manifest_path, temporary_record_ids, legal_tag_short)

        temp_records = []
        for record in missing_originals:
            cloned = replace_ids_in_value(copy.deepcopy(record), id_map)
            cloned["id"] = id_map[record["id"]]
            if "legal" not in cloned:
                cloned["legal"] = {}
            cloned["legal"]["legaltags"] = [legal_tag_full]
            if "otherRelevantDataCountries" not in cloned["legal"]:
                cloned["legal"]["otherRelevantDataCountries"] = ["US"]
            temp_records.append(cloned)

        print(f"Uploading {len(temp_records)} temporary fixture records to storage")
        storage_response = client.make_request(
            "PUT", "/api/storage/v2/records", json.dumps(temp_records)
        )
        if storage_response.status_code != 201:
            raise Exception(
                f"Could not create temporary records. Received {storage_response.status_code} : "
                f"{storage_response.reason} from storage service"
            )

        for temp_id in temporary_record_ids:
            client.wait_for_record_indexed(temp_id)
    else:
        # Ensure stale manifest from a previous local run cannot delete shared data.
        if os.path.exists(manifest_path):
            os.remove(manifest_path)

    record_id_set = set(id_map.values())
    return id_map, record_id_set, temporary_record_ids, legal_tag_short


def cleanup_temporary_fixtures(client, manifest_path: str) -> None:
    """Idempotently delete temporary records and job legal tag from a manifest."""
    if not os.path.exists(manifest_path):
        print(f"No temporary fixture manifest at {manifest_path}; nothing to clean")
        return

    manifest = read_manifest(manifest_path)
    temporary_record_ids = manifest.get("temporary_record_ids") or []
    legal_tag_name = manifest.get("legal_tag_name")

    errors = []
    for record_id in temporary_record_ids:
        response = client.make_request("DELETE", f"/api/storage/v2/records/{record_id}")
        if response.status_code not in (204, 404):
            errors.append(
                f"Could not delete temporary record {record_id}: HTTP {response.status_code}"
            )
        else:
            print(f"Deleted temporary record {record_id} (HTTP {response.status_code})")

    if legal_tag_name:
        response = client.make_request("DELETE", f"/api/legal/v1/legaltags/{legal_tag_name}")
        # Legal may return 204, 404, or 404-equivalent body depending on env.
        if response.status_code not in (204, 404):
            errors.append(
                f"Could not delete legal tag {legal_tag_name}: HTTP {response.status_code}"
            )
        else:
            print(f"Deleted legal tag {legal_tag_name} (HTTP {response.status_code})")

    try:
        os.remove(manifest_path)
    except OSError as exc:
        errors.append(f"Could not remove manifest {manifest_path}: {exc}")

    if errors:
        raise Exception("; ".join(errors))
