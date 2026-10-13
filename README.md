# OSDU CRS Catalog Service for Azure

[![Release](https://img.shields.io/github/v/release/Azure/osdu-spi-crs-catalog)](https://github.com/Azure/osdu-spi-crs-catalog/releases)
[![Validate](https://github.com/Azure/osdu-spi-crs-catalog/actions/workflows/validate.yml/badge.svg?branch=main)](https://github.com/Azure/osdu-spi-crs-catalog/actions/workflows/validate.yml)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

> [!NOTE]
> Shared service code comes from the [OSDU community upstream](https://community.opengroup.org/osdu/platform/system/reference/crs-catalog-service).

CRS Catalog answers lookups and searches for coordinate reference systems, coordinate transformations, and their areas of use, so callers can interpret and convert spatial data consistently.

## At a glance

| | |
|---|---|
| API base path | `/api/crs/catalog/v2/`, `/api/crs/catalog/v3/` |
| Swagger UI | `/api/crs/catalog/swagger` |
| Health | `:8081/actuator/health` |
| Depends on | Entitlements; Search for the v3 API |
| Azure resources | Key Vault URL only; the v2 catalog is a JSON file on a pod volume |
| Deployed by | [OSDU SPI Stack](https://github.com/Azure/osdu-spi-stack) (`software/stacks/osdu/services-reference/crs-catalog.yaml`) |

## Repository layout

[CONTRIBUTING.md](CONTRIBUTING.md) explains where each kind of change belongs.

| Path | Owner | Contents |
|---|---|---|
| `crs-catalog-core/` | OSDU upstream | Shared service code |
| `provider/crs-catalog-azure/` | This repository | Azure provider, in the `crs-catalog-aks` module |
| `crs-catalog_acceptance_test/` | OSDU upstream | End-to-end Python suite run against a deployed environment |
| `data/` | OSDU upstream | The catalog file, `crs_catalog_v2.json` |
| `testing/catalog_test_core/` | OSDU upstream | Shared Python integration tests |
| `testing/catalog_test_azure/` | This repository | Azure runner for the integration tests |
| `.spi/service.yaml` | This repository | How CI deploys and tests the service on SPI Stack |

## Build

Requires Java 17 and Maven 3.6.3+. OSDU dependencies resolve from the public community registry through the settings file in `.mvn`:

```bash
mvn --settings .mvn/community-maven.settings.xml -P core,azure clean install
```

The runnable jar lands at `provider/crs-catalog-azure/crs-catalog-aks/target/crs-catalog-aks-*-spring-boot.jar`.

## Configuration

SPI Stack sets the service's environment from two places: the shared `osdu-config` ConfigMap and the service's own entry in [`services-reference/crs-catalog.yaml`](https://github.com/Azure/osdu-spi-stack/blob/main/software/stacks/osdu/services-reference/crs-catalog.yaml). Those files are the contract; the tables below distinguish variables CRS Catalog reads from additional service-specific values the stack sets but this service ignores.

**Shared, from `osdu-config`:**

| Variable | Purpose |
|---|---|
| `KEYVAULT_URI` | Central Key Vault |
| `AAD_CLIENT_ID` | Application ID that caller tokens are issued for, read as `aad_client_id` |
| `APPINSIGHTS_KEY` | Telemetry, read as `appinsights_key` |
| `SERVER_PORT` | HTTP port (`8080`) |

**Specific to CRS Catalog**, from `services-reference/crs-catalog.yaml`:

| Variable and value on SPI Stack | Purpose |
|---|---|
| `SPRING_APPLICATION_NAME`<br>`crs-catalog` | Application name in logs and telemetry |
| `SERVER_SERVLET_CONTEXTPATH`<br>`/api/crs/catalog/` | API base path |
| `ENTITLEMENT_URL`<br>`http://entitlements/api/entitlements/v2` | Caller authorization; each request fetches the caller's groups |
| `AZURE_PAAS_WORKLOADIDENTITY_ISENABLED`<br>`true` | Authenticate to Azure with workload identity |
| `AZURE_ISTIOAUTH_ENABLED`<br>`true` | Not read by this service |
| `ACCEPT_HTTP`<br>`true` | Not read by this service |
| `PARTITION_SERVICE_ENDPOINT`<br>`http://partition/api/partition/v1` | Not read by this service |

Two variables the provider reads are not set by the stack and keep their defaults: `CRS_CATALOG_FILENAME` defaults to `/mnt/crs_catalogs/crs_catalog_v2.json`, where the stack mounts the catalog, and `SEARCH_API` defaults to `NOT_FOUND` (see [Service notes](#service-notes)).

The service authenticates to Azure with workload identity, which injects `AZURE_CLIENT_ID` and a federated token; there are no client secrets.

## Test

| Suite | Where | Runs in CI | Run it yourself |
|---|---|---|---|
| Unit | `crs-catalog-core` | Pull requests (Java Build) | `mvn ... install` from [Build](#build) |
| Acceptance | [`crs-catalog_acceptance_test`](crs-catalog_acceptance_test/docs/README.md) | No | See below |
| Integration | `testing/catalog_test_core`, `testing/catalog_test_azure` | No | See below |

CI runs unit tests on pull requests from this repository that change code. Documentation-only changes skip the build.

**Acceptance** is the upstream pytest suite for the v2 and v3 APIs. The test suite image CI builds runs Maven suites, so Deploy and Test cannot run this one. It accepts a bearer token in `PRIVILEGED_USER_TOKEN`, so `spi token` can supply one, but it has not been proven against SPI Stack, and its v3 cases need Search (see [Service notes](#service-notes)). Against an environment you are connected to:

```bash
cd crs-catalog_acceptance_test
VIRTUAL_SERVICE_HOST_NAME=<gateway> MY_TENANT=<partition> ENTITLEMENTS_DOMAIN=<domain> \
  PRIVILEGED_USER_TOKEN="$(spi token)" ./run-acceptance-tests.sh
```

**Integration** is the older suite carried from upstream. `testing/catalog_test_azure/run_test.py` runs the shared v2 cases in `testing/catalog_test_core`, the same cases the acceptance suite carries, and CI does not run it. It accepts a bearer token in `INTEGRATION_TESTER_ACCESS_TOKEN`, so `spi token` can supply one, but it has not been proven against SPI Stack.

To call the API by hand, `spi token` mints a bearer token:

```bash
curl -H "Authorization: Bearer $(spi token)" -H "data-partition-id: <partition>" \
  https://<gateway>/api/crs/catalog/v2/catalog/attributes
```

## Deploy

For a pull request from this repository that changes code, CI publishes the service image, `osdu-spi-crs-catalog`, to GHCR. The Deploy and Test lane borrows an SPI Stack environment, runs the new image there, proves it with the service's test suite image, and restores the environment's own image. That lane does not run for this service: the test suite image, `osdu-spi-crs-catalog-acceptance`, bakes Maven suites, and both suites here are Python. The Validation Summary on the pull request shows why the lane skipped. This repository does not own infrastructure; SPI Stack does.

To try a build by hand on an environment you are connected to, pin it by digest and release the pin when done:

```bash
spi service pin crs-catalog --image ghcr.io/azure/osdu-spi-crs-catalog@sha256:<digest>
spi service reset crs-catalog
```

## Service notes

**Catalog data.** The v2 API serves the catalog from a JSON file the service loads at startup, and the service does not start without it. The image does not carry the file. On SPI Stack, an init container downloads `data/crs_catalog_v2.json` from the upstream `master` branch into a volume mounted at `/mnt/crs_catalogs` on every pod start, so the catalog served follows upstream rather than this repository's `data/` folder. Outside SPI Stack, set `CRS_CATALOG_FILENAME` to `data/crs_catalog_v2.json` in this checkout.

**The v3 API and Search.** The v3 endpoints look up CRS and transformation records through Search at the URL in `SEARCH_API`. SPI Stack does not set it, so the Search client is built against the default `NOT_FOUND`, and v3 requests fail on SPI Stack until the stack supplies the Search endpoint. The v2 endpoints do not use Search.

## License

Copyright © Microsoft Corporation

Licensed under the [Apache License 2.0](LICENSE).
