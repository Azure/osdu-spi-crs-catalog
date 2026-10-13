#!/usr/bin/env python3
#
# Copyright 2020-2026 Google LLC
# Copyright 2020-2026 EPAM Systems, Inc
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""CIMPL Search health gate used before cimpl-acceptance-test-python."""

import json
import os
import sys
import time
import uuid

import constants
import jwt_client
from v3.HttpClient import HttpClient

MAX_ATTEMPTS = 8
SLEEP_SECONDS = 20
SEARCH_ENDPOINTS = (
    "/api/search/v2/query",
    "/api/search/v2/query_with_cursor",
)
ALLURE_DIR = "cimpl/allure-results"


def write_allure_result(status: str, message: str, start_ms: int) -> None:
    os.makedirs(ALLURE_DIR, exist_ok=True)
    stop_ms = int(time.time() * 1000)
    result = {
        "uuid": str(uuid.uuid4()),
        "historyId": "ensure-search-service-gate",
        "name": "Ensure Search Service Health Gate",
        "fullName": "ensure_search_service.Ensure Search Service Health Gate",
        "status": status,
        "statusDetails": {"message": message},
        "stage": "finished",
        "start": start_ms,
        "stop": stop_ms,
    }
    filepath = os.path.join(ALLURE_DIR, f"{result['uuid']}-result.json")
    with open(filepath, "w", encoding="utf-8") as f:
        json.dump(result, f, indent=2)


def main() -> int:
    start_ms = int(time.time() * 1000)

    if not constants.ROOT_URL:
        msg = "ERROR: VIRTUAL_SERVICE_HOST_NAME is not set"
        print(msg, file=sys.stderr)
        write_allure_result("failed", msg, start_ms)
        return 1

    kind = f"{constants.SCHEMA_AUTHORITY}:wks:reference-data--Coordinate*:1.*.*"
    payload = json.dumps({
        "kind": kind,
        "query": "*",
        "limit": 1,
        "returnedFields": ["id"],
    })

    client = HttpClient(constants.ROOT_URL, constants.MY_TENANT, jwt_client)
    print(
        f"Checking Search endpoints {SEARCH_ENDPOINTS} for kind={kind} "
        f"on {constants.ROOT_URL}"
    )

    last_by_endpoint = {}
    for attempt in range(1, MAX_ATTEMPTS + 1):
        for endpoint in SEARCH_ENDPOINTS:
            response = client.make_request("POST", endpoint, payload)
            body = (response.content or b"").decode("utf-8", "ignore")[:500]
            last_by_endpoint[endpoint] = (response.status_code, body)
            if response.status_code == 200:
                print(
                    f"Search OK on attempt {attempt} via {endpoint} (HTTP 200)"
                )
                return 0
            print(
                f"attempt {attempt}/{MAX_ATTEMPTS} {endpoint}: "
                f"HTTP {response.status_code}: {body}",
                file=sys.stderr,
            )
        if attempt < MAX_ATTEMPTS:
            time.sleep(SLEEP_SECONDS)

    details = "; ".join(
        f"{ep} -> HTTP {status}: {body}"
        for ep, (status, body) in last_by_endpoint.items()
    )
    err_msg = f"Search unhealthy after {MAX_ATTEMPTS} attempts ({details})"
    print(f"ERROR: {err_msg}", file=sys.stderr)
    write_allure_result("failed", err_msg, start_ms)
    return 1


if __name__ == "__main__":
    sys.exit(main())
