#!/bin/bash -eu
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

# Setup Python virtual environment
python3 -m venv venv
source venv/bin/activate

pip install -q --upgrade pip
# jwt_client + HttpClient only need requests (and transitive deps from v2)
pip install -q requests==2.32.3

echo ""
echo "***CHECKING CIMPL SEARCH SERVICE***"
echo ""

python3 ensure_search_service.py
STATUS=$?

echo ""
echo "***FINISHED SEARCH SERVICE CHECK***"
echo ""

deactivate
exit $STATUS
