Copyright 2017-2019, Schlumberger

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

# dps-crs-catalog integration tests

## Folder structure

testing/  

* catalog_test_core/  
  * ...
* catalog_test_$PROVIDER_NAME/  
  * jwt_client.py  
  * run_test.py  

This integration test uses a swagger generated Python client to test a
deployed crs-catalog service.

The python client code is automatically generated. The latest online version (May 2018)
created incorrect impost statements for cyclic class references. Therefore the current
code is generated using [swagger-codegen-cli-2.2.3.jar](http://repo1.maven.org/maven2/io/swagger/swagger-codegen-cli/2.2.3/swagger-codegen-cli-2.2.3.jar).
The command to create the python code is:
Linux

```bash
cd testing
java -jar ~/swagger-codegen-cli-2.2.3.jar generate -i catalog_test_core/api_spec/crs-catalog-openapi-v2.json -l python -o catalog_test_core/v2
```

Windows

```bat
cd testing
java -jar %UserProfile%\repositories\azure\swagger-codegen-cli-2.2.3.jar generate -i catalog_test_core\api_spec\crs-catalog-openapi-v2.json -l python -o catalog_test_core\v2
```

## Environment

The following parameters are expected as environment variables: Soon to change again with the standard authentication.

## Azure authentication provider (catalog_test_azure/jwt_client.py)

| Variable | Contents |
|----------|----------|
| AZURE_AD_APP_RESOURCE_ID | e.g. |
| AZURE_DEPLOY_TENANT | e.g.  |
| AZURE_DEPLOY_CLIENT_ID | e.g.  |
| AZURE_DEPLOY_CLIENT_SECRET | e.g.  |

## Baremetal auth provider (catalog_test_baremetal/jwt_client.py)

| Variable | Contents |
|----------|----------|
| INTEGRATION_TESTER | go to the google IAM & admin console, navigate to Service accounts to create a key and download the account info file. |

## Tests core (catalog_test_core/constants.py)

| Variable | Contents |
|----------|----------|
| BASE_URL | e.g. /api/crs/catalog/v2 |
| VIRTUAL_SERVICE_HOST_NAME | e.g. az-osdu1.evd.csp.slb.com |
| MY_TENANT | e.g. opendes |

## Building/running

Go to the provider folder:

```bash
cd catalog_test_$PROVIDER_NAME/ # e.g. catalog_test_sauth
```

To set up a virtual environment:

```bash
virtualenv venv
```

To activate the venv:

```bash
venv\Scripts\activate (on Windows)
source venv/bin/activate (on Linux)
```

Install runtime dependencies in venv

```bash
python3 -m pip install -r requirements.txt
```

To run:

```bash
python3 run_test.py
```

**Note:** To simulate a runtime exactly as that of the vsts build agent, you can simply exec into the docker image we use for the build agent, and run the tests from inside it. To know how to do this, please follow [this](https://slb-swt.visualstudio.com/data-at-rest/_git/dps-vsts-build-agent?path=%2FREADME.md&version=GBmaster) documentation.
