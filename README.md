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

## Running Azure Catalog Service locally
#### Build and run Catalog Service locally using bash
- Set the required environments described in [Release/deployment](##Release/deployment) section
- Navigate to the Catalog Service's root folder ```crs-catalog-service```
- Build core and run unit tests in command line:
```bash
mvn clean install
# To run without tests add -Dmaven.test.skip=true
```
- Navigate to the Catalog Service's root folder ```crs-catalog-service```
- Run application with command
```bash
java -Dclient-id=${aad_client_id} -jar provider/crs-catalog-azure/crs-catalog-aks/target/crs-catalog-aks-1.0.0.jar
```

#### Running Azure Catalog Service using IntelliJ IDEA
Navigate to the **Create Run/Debug Configuration** tool
Select **'Add New Configuration'** and select **Application**

Type the next commands into the suggested fields: 
- Working directory: ```{path_to_the_catalog}/crs-catalog-service```
- Main class: ```org.opengroup.osdu.crs.CrsAksApplication```
- Use classpath of module:  ```crs-catalog-aks```  
***Note: If you don't see "crs-catalog-aks" in the dropdown menu - find appropriate pom.xml and click "Add as a Maven project"***
- Environment variables: Set the required environments described in [Release/deployment](##Release/deployment) section  

Execute **Run** or **Debug** for configured Application


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
- default value is false (Currently only in Azure it is enabled)
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

### Build and run the Docker container locally
1. Run the `maven run` command to have the .jar file generated.
1. Have the Azure subscription set up 
1. Open a Powershell
1. Install the Azure CLI locally
1. Authenticate yourself to Azure Container Registry (acr) with the following command:
```az acr login --name delfi```
1. Execute the following command to build the container image:
```docker build -t crs-catalog .```
1. Execute the following command to build the container image:
```docker run -t --rm -p 8080:8080 crs-catalog```
1. Use Postman or curl to try out the endpoints


## Release/deployment
VSTS release definition is located at provider\crs-catalog-azure\crs-catalog-aks\devops, which 
requires the following environment variables:

| Variable | Contents |
|----------|----------|
| ENTITLEMENT_URL | Required |
| client-id | Required, Azure AAD client id |
| CRS_CATALOG_FILENAME | Required, e.g. `{path_to_the_catalog}/crs-catalog-service/data/crs_catalog_v2.json` |

## Google Cloud

The Google Cloud provider has been removed from this repository.
