### Running E2E Tests

You will need to have the following environment variables defined.

| name                                | value                                              |                                                  | sensitive? | source        |
|-------------------------------------|----------------------------------------------------|--------------------------------------------------|------------|---------------|
| `ENTITLEMENTS_DOMAIN`               | eg. `osdu.group`                                   | OSDU R2 entitlements domain to run tests under   | no         | -             |
| `VIRTUAL_SERVICE_HOST_NAME`         | eg. `osdu.core-dev.gcp.gnrg-osdu.projects.epam.com`| Host name of catalog service under test          | no         | -             |
| `MY_TENANT`                         | eg. `osdu`                                         | OSDU tenant used for testing                     | no         | -             |
| `SCHEMA_AUTHORITY`                  | eg. `osdu`                                         | OSDU schema authority used for testing           | no         | -             |

Authentication can be provided as OIDC config:

| name                                            | value                                   | description                   | sensitive? | source |
|-------------------------------------------------|-----------------------------------------|-------------------------------|------------|--------|
| `PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_ID`     | `********`                              | PRIVILEGED_USER Client Id     | yes        | -      |
| `PRIVILEGED_USER_OPENID_PROVIDER_CLIENT_SECRET` | `********`                              | PRIVILEGED_USER Client secret | yes        | -      |
| `TEST_OPENID_PROVIDER_URL`                      | `https://keycloak.com/auth/realms/osdu` | OpenID provider url           | yes        | -      |

Or tokens can be used directly from env variables:

| name                    | value      | description           | sensitive? | source |
|-------------------------|------------|-----------------------|------------|--------|
| `PRIVILEGED_USER_TOKEN` | `********` | PRIVILEGED_USER Token | yes        | -      |

#### Setup and Run Tests

```bash
cd crs-catalog_acceptance_test

# 1. Create virtual environment
python3 -m venv venv
source venv/bin/activate

# 2. Install dependencies
pip install -r requirements.txt
pip install -r v2/requirements.txt

# 3. Set environment variables (or use .env file)
export VIRTUAL_SERVICE_HOST_NAME="your-host.com"
export MY_TENANT="your-tenant"
export PRIVILEGED_USER_TOKEN="your-token"

# Or load from .env file
set -a; source .env; set +a

# 4. Run tests with Allure reporting
pytest test_crs_catalog_v2.py test_crs_catalog_v3.py \
    --alluredir=allure-results \
    --clean-alluredir \
    -v

# 5. Generate HTML report (using script or podman)
./run-acceptance-tests.sh

# Or with podman (no Allure CLI needed)
mkdir -p allure-report
podman run --rm \
  -v $(pwd)/allure-results:/allure-results:Z \
  -v $(pwd)/allure-report:/allure-report:Z \
  frankescobar/allure-docker-service \
  allure generate /allure-results -o /allure-report --clean
```

**Alternative: Simple HTML report (no Allure)**
```bash
pytest test_crs_catalog_v2.py test_crs_catalog_v3.py \
    --html=report.html --self-contained-html -v
```

