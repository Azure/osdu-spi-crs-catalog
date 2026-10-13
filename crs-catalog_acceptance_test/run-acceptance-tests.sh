#!/bin/bash
#
#  Copyright 2020-2026 Google LLC
#  Copyright 2020-2026 EPAM Systems, Inc
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

set -eu

# Setup Python virtual environment
python3 -m venv venv
source venv/bin/activate

# Install Python dependencies
pip install -q --upgrade pip
pip install -q -r requirements.txt
pip install -q -r v2/requirements.txt

CLEANUP_STATUS=0
on_exit() {
  local prior=$?
  if [ -f .temp_fixtures_manifest.json ]; then
    echo ""
    echo "***CLEANING TEMPORARY FIXTURES***"
    echo ""
    # Prefer venv interpreter so EXIT trap still works after deactivate.
    if [ -x venv/bin/python3 ]; then
      PY=venv/bin/python3
    else
      PY=python3
    fi
    if ! "$PY" cleanup_temp_fixtures.py; then
      CLEANUP_STATUS=1
      echo "WARNING: temporary fixture cleanup failed" >&2
    fi
  fi
  if [ "$prior" -ne 0 ]; then
    exit "$prior"
  fi
  exit "$CLEANUP_STATUS"
}
trap on_exit EXIT

echo ""
echo "***RUNNING CRS CATALOG TESTS WITH REPORTING***"
echo ""

# Run all tests with pytest (handles both v2 and v3, pytest and unittest tests)
set +e
pytest test_crs_catalog_v2.py test_crs_catalog_v3.py \
    --alluredir=cimpl/allure-results \
    --clean-alluredir \
    -v
TEST_STATUS=$?
set -e

echo ""
echo "***FINISHED CRS CATALOG TESTS***"
echo ""

deactivate
exit "$TEST_STATUS"
