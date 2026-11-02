# Spatial Reference Catalog Service

The main goals of the Spatial Reference Catalog service are to offer

- *Coordinate Reference Systems* (CRSs) to enable end-users to
make a CRS selection.
- Search for CRSs given a number of constraints.
- Download of the entire catalog for local caching and when the cache
has to be refreshed.
- Access to various sub-sets of the catalog.
- Once a CRS is found, produce a persistable reference to be stored with
the data, which fully describes the CRS; this persistable reference string
becomes catalog independent. This means any consumer will be able to
understand the CRS definition even if a different catalog is used in the
future context.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Requirements
* Java 25
* [Maven 3.9.16+](https://maven.apache.org/download.cgi)

### General Tips

**Environment Variable Management**
The following tools make environment variable configuration simpler
- [direnv](https://direnv.net/) - for a shell/terminal environment
- [EnvFile](https://plugins.jetbrains.com/plugin/7861-envfile) - for [Intellij IDEA](https://www.jetbrains.com/idea/)

**Lombok**
This project uses [Lombok](https://projectlombok.org/) for code generation. You may need to configure your IDE to take advantage of this tool.
- [Intellij configuration](https://projectlombok.org/setup/intellij)
- [VSCode configuration](https://projectlombok.org/setup/vscode)

### Installation
In order to run the service locally or remotely, you will need to have the following environment variables defined.

| name                        | value                                                                  | description                      | sensitive? | source                              |
|-----------------------------|------------------------------------------------------------------------|----------------------------------|------------|-------------------------------------|
| `LOG_PREFIX`                | `service`                                                              | Logging prefix                   | no         | -                                   |
| `SERVER_SERVLET_CONTEXPATH` | `/api/crs/catalog/`                                                    | CRS Catalog service context path | no         | -                                   |
| `CRS_CATALOG_FILENAME`      | ex `C:\\osdu-delfi\\os-crs-catalog-service\\data\\crs_catalog_v2.json` | Crs Catalog V2 file              | no         | output of infrastructure deployment |
| `ENTITLEMENTS_API`          | `http://entitlements/api/entitlements/v2/`                             | Entitlements service endpoint    | no         | -                                   |
| `SEARCH_API`                | `http://search/api/search/v2`                                          | Search service endpoint          | no         | -                                   |

Defined in default application property file but possible to override:

| name                              | value                                        | description                         | sensitive? | source        |
|-----------------------------------|----------------------------------------------|-------------------------------------|------------|---------------|
| `MANAGEMENT_ENDPOINTS_WEB_BASE`   | ex `/`                                       | Web base for Actuator               | no         | -             |
| `MANAGEMENT_SERVER_PORT`          | ex `8081`                                    | Port for Actuator                   | no         | -             |

### Run Locally
Check that maven is installed:

```bash
$ mvn --version
Apache Maven 3.9.16
Maven home: /usr/share/maven
Java version: 25.0.4, vendor: Eclipse Adoptium, runtime: /usr/lib/jvm/temurin-25-jdk
...
```

You will need to configure access to the remote maven repository that holds the OSDU dependencies. This file should live within `~/.m2/settings.xml`:

```bash
$ cat ~/.m2/settings.xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>os-core</id>
            <username>slb-des-ext-collaboration</username>
            <!-- Treat this auth token like a password. Do not share it with anyone, including Microsoft support. -->
            <password>${VSTS_FEED_TOKEN}</password>
        </server>
    </servers>
</settings>
```

* Navigate to CRS Catalog service root folder and run:

```bash
mvn jetty:run
## Testing
* Navigate to CRS Catalog service root folder and run:
 
```bash
mvn clean install   
```

* If you wish to see the coverage report then go to testing/target/site/jacoco-aggregate and open index.html

* If you wish to build the project without running tests

```bash
mvn clean install -DskipTests
```

After configuring your environment as specified above, you can follow these steps to build and run the application. These steps should be invoked from the *repository root.*

```bash
cd crs-catalog-core-plus/ && mvn spring-boot:run
```

## Testing
Navigate to CRS Catalog service root folder and run all the tests:

```bash
# build + test + install core service code
$ (cd crs-catalog-core/ && mvn clean install)
```

## Test the application
After the service has started it should be accessible via a web browser by visiting [http://localhost:8080/api/crs/catalog/swagger-ui.html](http://localhost:8080/api/crs/catalog/swagger-ui.html). If the request does not fail, you can then run the integration tests.


## Licence
Copyright © Google LLC
Copyright © EPAM Systems
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
