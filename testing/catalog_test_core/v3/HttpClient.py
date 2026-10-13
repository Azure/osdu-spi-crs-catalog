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
import json
import math
import time
from typing import Set

import constants
import requests


class HttpClient(object):

    def __init__(self, root_url, data_partition_id, jwt_client):
        self.data_partition_id = data_partition_id
        self.jwt_client = jwt_client
        self.bearer_token = self.jwt_client.get_id_token()
        self.unauth_retries = 0
        self.root_url = root_url

    def make_request(self, method: str, path: str, data = ''):

        if 'localhost' in self.root_url:
            url = 'http://' + self.root_url + path
        else:
            url = 'https://' + self.root_url + path

        if 'Bearer ' not in self.bearer_token:
            self.bearer_token = f"Bearer {self.bearer_token}"

        headers = {
            'content-type': 'application/json',
            'data-partition-id': self.data_partition_id,
            'Authorization': self.bearer_token
        }

        response = object

        if (method == 'GET'):
            response = requests.get(url=url, headers=headers)
        elif (method == 'DELETE'):
            response = requests.delete(url=url, headers=headers)
        elif (method == 'POST'):
            response = requests.post(url=url, data=data, headers=headers)
        elif (method == 'PUT'):
            response = requests.put(url=url, data=data, headers=headers)

        if (response.status_code == 401 or response.status_code == 403) and self.unauth_retries < 1:
            self.bearer_token = self.jwt_client.get_id_token()
            self.unauth_retries += 1
            self.make_request(method, path, data)

        self.unauth_retries = 0

        return response

    def get_all_ids_of_kind(self, kind: str) -> Set[str]:
        """
        Uses the search query by cursor to return the list of all IDs from a given `kind`
        """
        search_response_id_set = set()

        cursor = ""
        total_count = math.inf
        search_params = {
            "kind": kind,
            "limit": 1000,
            "returnedFields":["id"]
        }
        while len(search_response_id_set) < total_count:
            if cursor:
                search_params['cursor'] = cursor

            search_response = self.make_request('POST', '/api/search/v2/query_with_cursor', json.dumps(search_params))
            if search_response.status_code != 200:
                raise Exception(f"Could not search. received {search_response.status_code} from search service")

            search_response_body = json.loads(search_response.content)
            total_count = search_response_body['totalCount']
            cursor = search_response_body.get("cursor", "")

            for search_response_result in search_response_body["results"]:
                search_response_id_set.add(search_response_result["id"])

        return search_response_id_set

    def is_record_indexed(self, record_id: str) -> bool:
        """Return True when Search can find the exact record id."""
        search_params = {
            "kind": f"{constants.SCHEMA_AUTHORITY}:wks:reference-data--Coordinate*:1.*.*",
            "query": f'id:("{record_id}")',
            "limit": 1,
            "returnedFields": ["id"],
        }
        search_response = self.make_request(
            "POST", "/api/search/v2/query", json.dumps(search_params)
        )
        if search_response.status_code != 200:
            raise Exception(
                f"Could not search for record {record_id}. "
                f"received {search_response.status_code} from search service: "
                f"{search_response.text}"
            )
        search_response_body = json.loads(search_response.content)
        for result in search_response_body.get("results", []):
            if result.get("id") == record_id:
                return True
        return False

    def wait_for_record_indexed(self, record_id: str, max_checks: int = 20, sleep_seconds: int = 5):
        """Poll Search until the exact record id is indexed or raise."""
        for checks in range(max_checks):
            if self.is_record_indexed(record_id):
                return
            print(
                f"Didn't find record {record_id} after {checks + 1} checks. "
                "Waiting for indexing..."
            )
            time.sleep(sleep_seconds)
        raise Exception(
            f"Record not indexed after {max_checks} checks: {record_id}"
        )