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
# Spatial Reference Catalog Service

## This repository contains 
1. The Java implementation of the Spatial Reference Catalog (aka crs-catalog). The Java code is located in the ```src``` folder. To open the Java project, open ```pom.xml```.
1. Tests are located in ```src/test/java/org/opengroup/osdu/crs/...```
1. The openapi specification file is crs_catalog_openapi.json.
1. Python integration and health tests in the ```testing``` folder. 
See also the test's [README.md](testing/README.md)

## Prerequisites
1. The project builds with [Maven](https://maven.apache.org/) **3.9.16+**.
1. Community modules (`crs-catalog-core`, `crs-catalog-core-plus`) require **JDK 25** (Epic 82).
1. The project requires the [Lombok](https://projectlombok.org/) plug-in installed for your IDE (**1.18.40+** for JDK 25).

### Build service and run unit tests

```sh
mvn clean install
```

## Running locally
See [crs-catalog-core-plus/README.md](crs-catalog-core-plus/README.md) for the environment variables and steps to run the service locally.

### Debug locally - e.g. using Postman
In the Postman Settings / General, turn SSL certificate validation off when running locally.
Similarly, when not using Postman but client code, set the configuration  ```verify_ssl``` false (see [instructions](https://github.com/swagger-api/swagger-codegen/issues/7778))

Run application using debug mode and use [Postman](https://www.getpostman.com/)
to send a GET request to obtaining the Swagger API documentation

### Open API 3.0 - Swagger

- Swagger UI : https://host/context-path/swagger (will redirect to https://host/context-path/swagger-ui/index.html)
- api-docs [All Versions] (JSON) : https://host/context-path/api-docs
- api-docs [All versions] (YAML) :https://host/context-path/api-docs.yaml
- api-docs [Version V2] (JSON) : https://host/context-path/api-docs/v2
- api-docs [Version V3] (JSON) : https://host/context-path/api-docs/v3        

All the Swagger and OpenAPI related common properties are managed here [swagger.properties](https://community.opengroup.org/osdu/platform/system/reference/crs-catalog-service/-/blob/jb/az_swagger_openapi/crs-catalog-core/src/main/resources/swagger.properties)

#### Server Url(full path vs relative path) configuration
- `api.server.fullUrl.enabled=true` It will generate full server url in the OpenAPI swagger
- `api.server.fullUrl.enabled=false` It will generate only the contextPath only
- default value is false
[Reference]:(https://springdoc.org/faq.html#_how_is_server_url_generated) 

```
http://localhost:8080/api/crs/catalog/v2/swagger.json
```
or send a Get request to catalog:
```
http://localhost:8080/api/crs/catalog/v2
```
Open the Swagger-UI:
```
http://localhost:8080/api/crs/catalog/swagger-ui.html
```

Headers for Postman:
| Key | Value |
|----------|----------|
| Authorization | Bearer `<token>` |
| data-partition-id | $MY_TENANT (see [testing\README.md](testing/README.md)) |

## Google Cloud

The Google Cloud provider has been removed from this repository.
