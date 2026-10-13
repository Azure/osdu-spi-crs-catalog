import os
import msal
import urllib
import sys
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def get_id_token():
    # Generate valid Token for given tenant.
    tenant_id = os.getenv('AZURE_TENANT_ID')
    resource_id = os.getenv('AZURE_AD_APP_RESOURCE_ID')
    client_id = os.getenv('INTEGRATION_TESTER')
    client_secret = os.getenv('AZURE_TESTER_SERVICEPRINCIPAL_SECRET')
    authority_host_uri = 'https://login.microsoftonline.com'
    authority_uri = urllib.parse.urljoin(authority_host_uri, tenant_id)
    scopes = [resource_id + '/.default']
    integration_tester_access_token = os.getenv('INTEGRATION_TESTER_ACCESS_TOKEN')

    if integration_tester_access_token:
        logger.info("Using bearer token from environment variable: INTEGRATION_TESTER_ACCESS_TOKEN")
        return integration_tester_access_token
    else:
        logger.info("Generating bearer token using SPN client id and secret since environment variable INTEGRATION_TESTER_ACCESS_TOKEN is null/not defined")
        app = msal.ConfidentialClientApplication(client_id=client_id, authority=authority_uri, client_credential=client_secret)
        result = app.acquire_token_for_client(scopes=scopes)
        return result.get('access_token')

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
