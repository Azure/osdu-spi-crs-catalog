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

"""CLI/idempotent cleanup for job-scoped temporary v3 fixtures."""

import argparse
import os
import sys

import constants
import jwt_client
from v3.HttpClient import HttpClient
from v3.temp_fixtures import cleanup_temporary_fixtures, default_manifest_path


def main(argv=None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--manifest",
        default=default_manifest_path(os.path.dirname(os.path.abspath(__file__))),
        help="Path to temporary fixture manifest JSON",
    )
    args = parser.parse_args(argv)

    if not constants.ROOT_URL:
        print("ERROR: VIRTUAL_SERVICE_HOST_NAME is not set", file=sys.stderr)
        return 1

    client = HttpClient(constants.ROOT_URL, constants.MY_TENANT, jwt_client)
    try:
        cleanup_temporary_fixtures(client, args.manifest)
    except Exception as exc:
        print(f"ERROR: temporary fixture cleanup failed: {exc}", file=sys.stderr)
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
