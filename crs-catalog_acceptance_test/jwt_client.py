#!/usr/bin/python
#
#  Copyright 2020-2022 Google LLC
#  Copyright 2020-2022 EPAM Systems, Inc
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

import os

import requests

def get_id_token():
    # Get the token from the environment variable if exists (injected token)
    if os.environ.get("PRIVILEGED_USER_TOKEN"):
        return os.environ["PRIVILEGED_USER_TOKEN"]
    
    # Get the OpenID provider details from the environment variables
    PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_ID = os.environ["PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_ID"]
    TEST_OPENID_PROVIDER_URL = os.environ["TEST_OPENID_PROVIDER_URL"]
    PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_SECRET = os.environ["PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_SECRET"]

    headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
    }
    request_body = {
        'grant_type': 'client_credentials',
        'scope': "openid",
        'client_id': PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_ID,
        'client_secret': PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_SECRET
    }


    response = requests.post(
        f"{TEST_OPENID_PROVIDER_URL}/protocol/openid-connect/token",
        headers=headers,
        data=request_body
    )
    response.raise_for_status()
    id_token = response.json()["id_token"]

    # Open a file in write mode
    with open("example.txt", "w") as file:
        # Write the string to the file
        file.write(id_token)

    return id_token


def get_invalid_token():

    '''
    This is dummy jwt
    {
         "sub": "dummy@dummy.com",
         "iss": "dummy@dummy.com",
         "aud": "dummy.dummy.com",
         "iat": 1556137273,
         "exp": 1556223673,
         "provider": "dummy.com",
         "client": "dummy.com",
         "userid": "dummytester.com",
         "email": "dummytester.com",
         "authz": "",
         "lastname": "dummy",
         "firstname": "dummy",
         "country": "",
         "company": "",
         "jobtitle": "",
         "subid": "dummyid",
         "idp": "dummy",
         "hd": "dummy.com",
         "desid": "dummyid",
         "contact_email": "dummy@dummy.com"
    }
    '''

    return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkdW1teUBkdW1teS5jb20iLCJpc3MiOiJkdW1teUBkdW1teS5jb20iLCJhdWQiOiJkdW1teS5kdW1teS5jb20iLCJpYXQiOjE1NTYxMzcyNzMsImV4cCI6MTU1NjIzMDk3OSwicHJvdmlkZXIiOiJkdW1teS5jb20iLCJjbGllbnQiOiJkdW1teS5jb20iLCJ1c2VyaWQiOiJkdW1teXRlc3Rlci5jb20iLCJlbWFpbCI6ImR1bW15dGVzdGVyLmNvbSIsImF1dGh6IjoiIiwibGFzdG5hbWUiOiJkdW1teSIsImZpcnN0bmFtZSI6ImR1bW15IiwiY291bnRyeSI6IiIsImNvbXBhbnkiOiIiLCJqb2J0aXRsZSI6IiIsInN1YmlkIjoiZHVtbXlpZCIsImlkcCI6ImR1bW15IiwiaGQiOiJkdW1teS5jb20iLCJkZXNpZCI6ImR1bW15aWQiLCJjb250YWN0X2VtYWlsIjoiZHVtbXlAZHVtbXkuY29tIiwianRpIjoiNGEyMWYyYzItZjU5Yy00NWZhLTk0MTAtNDNkNDdhMTg4ODgwIn0.nkiyKtfXXxAlC60iDjXuB2EAGDfZiVglP-CyU1T4etc"
