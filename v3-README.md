Copyright © Amazon

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http:#www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

# Spatial Reference Catalog Service v3

This README contains details around the addition of v3 endpoints

## Context

Changes made to this service for v3 are described in OSDU forum ADR here: https://community.opengroup.org/osdu/platform/system/home/-/issues/94

Swagger doc can be found under docs/api_spec/crs-catalog-openapi-v3.yaml

## Design

OSDU users desired a version of CRS Catalog that was based on reference data records as opposed to a 
hard-coded catalog (v2). Reference data has been provided that represents Coordinate Transformation and
Coordinate Reference System data which can be loaded into OSDU platform. 

Two new spatial filters were added to Search service to support this functionality.

v3 endpoints in this service are meant to be used as a domain helper to facilitate querying CRS and CT
reference data residing in elasticsearch. 

Each response (with exception of the area of use endpoint) includes a query string to help the user 
directly query Search service for the same results and less latency.


## Deployment

All deployment and testing is run the same way with the same pipelines and scripts.

New integration testing script was added.
