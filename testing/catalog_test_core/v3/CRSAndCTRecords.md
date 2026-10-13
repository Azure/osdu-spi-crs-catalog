# CRSAndCTRecords.json

Documents the reference-data record IDs and payloads used by v3 acceptance tests (EPSG CRS/CT codes from [Data Definitions manifests](https://community.opengroup.org/osdu/data/data-definitions)).

## Lifecycle

1. For each fixture ID, query Search by **exact** record id.
2. If the shared record is already indexed, tests **reuse** it and leave it unchanged —
   except geometry-sensitive fixtures listed below.
3. If it is missing from Search (or force-cloned), tests create a **temporary clone** with id
   `<original-id>--ci-<CI_JOB_ID>` and a job-scoped legal tag
   `crs-catalog-int-test-<CI_JOB_ID>`.
4. Only temporary clones and the job legal tag are deleted after the suite
   (success or failure). Shared reference data is never DELETE'd (see issue #141).

## Force-temporary fixtures

`Projected:EPSG::3851` is always cloned as a job-scoped temp, even when the shared id is
indexed. The shared catalog MultiPolygon is too detailed for a stable
`points-in-aou` antimeridian check; tests need the simple fixture geometry in
`CRSAndCTRecords.json`.
