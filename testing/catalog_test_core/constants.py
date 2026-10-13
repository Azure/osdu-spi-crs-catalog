#!/usr/bin/python

import sys, os


def main(argv):
    pass


if __name__ == '__main__':
    main(sys.argv)

VENDOR = os.getenv("VENDOR")
BASE_URL = os.getenv("BASE_URL", '/api/crs/catalog/v2')
if VENDOR == 'ibm':
    ROOT_URL = os.getenv("IBM_VIRTUAL_HOST_CRS_CATALOG")
else:
    ROOT_URL = os.getenv("VIRTUAL_SERVICE_HOST_NAME")
MY_TENANT = os.getenv("MY_TENANT", "osdu")
ACL_DOMAIN = os.getenv("ACL_DOMAIN", "osdu.example.com")
SCHEMA_AUTHORITY = os.getenv("SCHEMA_AUTHORITY", "osdu")
LOCAL_MODE = os.getenv("LOCAL_MODE", "false")

