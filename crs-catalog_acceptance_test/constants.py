#!/usr/bin/python

import sys, os


def main(argv):
    pass


if __name__ == '__main__':
    main(sys.argv)

BASE_URL = os.getenv("BASE_URL", '/api/crs/catalog/v2')
ROOT_URL = os.getenv("VIRTUAL_SERVICE_HOST_NAME")
MY_TENANT = os.getenv("MY_TENANT", "osdu")
ENTITLEMENTS_DOMAIN = os.getenv("ENTITLEMENTS_DOMAIN", "group")
SCHEMA_AUTHORITY = os.getenv("SCHEMA_AUTHORITY", "osdu")
LOCAL_MODE = os.getenv("LOCAL_MODE", "false")

