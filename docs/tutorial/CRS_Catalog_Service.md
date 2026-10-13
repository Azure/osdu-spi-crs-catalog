# CRS Catalog helper service tutorial

*The OSDU has two CRS helper services: "CRS Conversion" and "CRS
Catalog". This tutorial provides examples and background to help
application developers accomplish typical tasks.*

**Revision Log**

| **Version** | **Reason for change**             | **Author** | **Date**   |
| ----------- | --------------------------------- | ---------- | --------   |
| 1.0         | Initial version for R3.M13 API v3 | Geomatics Integration  | 2022-09-30 |
| 1.1         | Updated responses                 | Geomatics Integration  | 2023-04-19 |
| 1.2         | Add minor epxlanations on id.     | Geomatics Integration  | 2023-08-21 |
|             |                                   |                        |            |

**Table of Contents**

-   [1. Introduction](#1-introduction)
    -   [1.1 The importance of using the CRS record id](#11-the-importance-of-using-the-crs-record-id)
    -   [1.2 CRS concepts](#12-crs-concepts)
-   [2. CRS catalog endpoints](#2-crs-catalog-endpoints)
-   [3. Fetch the details of a single CRS or CT](#3-fetch-the-details-of-a-single-crs-or-ct)
    -   [3.1 Context](#31-context)
    -   [3.2 Simple examples](#32-simple-examples)
        -   [3.2.1 Alternative 1: retrieve a single CRS/CT record using the Search service](#321-alternative-1-retrieve-a-single-crsct-record-using-the-search-service)
        -   [3.2.2 Alternative 2: retrieve a single CRS/CT record using the Storage service](#322-alternative-2-retrieve-a-single-crsct-record-using-the-storage-service)
-   [4. Fetch a list of CRSs](#4-fetch-a-list-of-crss)
    -   [4.1 Context](#41-context)
        -   [4.1.1 Parameters for the POST coordinate-reference-point endpoint](#411-parameters-for-the-post-coordinate-reference-point-endpoint)
        -   [4.1.2 Performance check](#412-performance-check)
    -   [4.2 Retrieve the details of a single CRS](#42-retrieve-the-details-of-a-single-crs)
    -   [4.3 Fetch all CRSs of a specific Kind (e.g., BoundProjected)](#43-fetch-all-crss-of-a-specific-kind-eg-boundprojected)
    -   [4.4 Fetch all CRSs of a specific "kind" which area of use includes a specific location](#44-fetch-all-crss-of-a-specific-kind-which-area-of-use-includes-a-specific-location)
    -   [4.5 Fetch all CRSs that use a specific horizontal unit (e.g., meters)](#45-fetch-all-crss-that-use-a-specific-horizontal-unit-eg-meters)
    -   [4.6 Fetch all Projected CRSs based on WGS 84 that use the unit meter](#46-fetch-all-projected-crss-based-on-wgs-84-that-use-the-unit-meter)
    -   [4.7 Fetch all BoundProjected and Projected CRSs based on WGS 84 that use the unit meter](#47-fetch-all-boundprojected-and-projected-crss-based-on-wgs-84-that-use-the-unit-meter)
        -   [4.7.1 Projected](#471-projected)
        -   [4.7.2 Geographic](#472-geographic)
    -   [4.8 Less useful parameters (queries)](#48-less-useful-parameters-queries)
        -   [4.8.1 Find all Vertical CRSs based on datum Mean Sea Level (code 5100)](#481-find-all-vertical-crss-based-on-datum-mean-sea-level-code-5100)
        -   [4.8.2 Retrieve all CRSs with "Texas" in the name](#482-retrieve-all-crss-with-texas-in-the-name)
        -   [4.8.3 Find all CRSs with Texas in the extent description](#483-find-all-crss-with-texas-in-the-extent-description)
    -   [4.9 Alternatively using the OSDU Search service directly](#49-alternatively-using-the-osdu-search-service-directly)
        -   [4.9.1 Find all BoundProjected CRSs with a Horizontal unit of meters](#491-find-all-boundprojected-crss-with-a-horizontal-unit-of-meters)
        -   [4.9.2 Find all Projected CRSs based on WGS 84 with a Horizontal unit of meters](#492-find-all-projected-crss-based-on-wgs-84-with-a-horizontal-unit-of-meters)
-   [5. Getting a list of CTs](#5-getting-a-list-of-cts)
    -   [5.1 Context](#51-context)
        -   [5.1.1 Parameters for the POST coordinate-transformation endpoint](#511-parameters-for-the-post-coordinate-transformation-endpoint)
    -   [5.2 Find all (horizontal) CTs valid at a specific location](#52-find-all-horizontal-cts-valid-at-a-specific-location)
    -   [5.3 Find all CTs between two (horizontal) CRSs](#53-find-all-cts-between-two-horizontal-crss)
    -   [5.4 Find all vertical CTs](#54-find-all-vertical-cts)
    -   [5.5 Find all deprecated CTs (or CRSs)D](#55-find-all-deprecated-cts-or-crss)
-   [6. Check if a point is inside of the area of use of a CRS or CT](#6-check-if-a-point-is-inside-of-the-area-of-use-of-a-crs-or-ct)
    -   [6.1 Context](#61-context)
    -   [6.2 Examples](#62-examples)
    -   [6.3 Recommended usage by caller](#63-recommended-usage-by-caller)

# 1. Introduction

This "How To" tutorial serves as a quick start for developers by
giving examples of how to accomplish common tasks dealing with Coordinate Reference Systems (CRS)
and Coordinate Transformations (CT), as well as to provide 
background not easily described in [swagger open api specification](https://community.opengroup.org/osdu/platform/system/reference/crs-catalog-service/-/blob/master/docs/api_spec/crs-catalog-openapi-v3.yaml) or postman collections.

When loading data into the OSDU platform it is possible to define the CRS by using a `PersistableReference`, which is a self-contained actionable WKT definition that can be passed on to a geodetic engine. However, **it is much preferred to implicitly identify the CRS** by means of a CRS `record id`. The architectural design of the OSDU is to centrally manage CRS definitions as reference data, and to use this Catalog service to retrieve them. This makes it easier for ingest workflows to pick the CRS of the data, and it increases geodetic integrity of the platform. This is predicated on using a clear convention for the CRS `record id` that uniquely and permanently identifies the CRS. The convention using the type of CRS and (de facto) standard EPSG codes (see also the next section).

The CRS Catalog helper service can be seen as a data domain specific
service, i.e., API endpoints specific to make it easy to work with geodetic
reference data (CRSs and CTs) in OSDU. Since release R3.M12 (\~3Q2022) 
new endpoints have been implemented in v3 that
interact with the reference data stored in the OSDU. Prior to this a "phyisical catalog file"
was used, **but note that v2 has been deprecated and should not be used**. v3 is a breaking change, aligning with the Geomatics Integration workgroup recommended approach.

Use cases for the CRS Catalog Service include:
- Enable end-users to make a selection of **Coordinate Reference Systems** (CRSs) 
  and **Coordinate Transformations** (CTs) associated with ingested data and project setup.
- Search for CRSs given a number of constraints, including spatial constraints.
- Download of the entire catalog for local caching/synchronization in applications that require this.

The CRS Catalog service calls the standard OSDU search service query "under the hood" for most endpoints. Therefore, geodetic
information can also be retrieved by making queries via the standard
search service with identical results. The query is reported in
the response.

However, **users should use the dedicated CRS Catalog Service rather than the standard search API** to retrieve CRS definitions for the following reasons:

-   If the Search service is directly used this results in multiple or very complex queries, for example to fetch all transformations between two datums (due to the complexity of reversibility in the EPSG data model).  The CRS Catalog Service simply has a parameter to achieve this. Another example is
finding projected CRS based on the WGS 84 datum, or finding CRSs that use the meter as unit. Such queries can be easily done with the CRS Catalog Service but are hard to do with the Search.

-   By default the CRS Catalog Service returns all records, rather than a default limit of 10, and suffering from a hard limit of 1000, forcing the programmer to use pagination. There are less than 10,000 CRSs in the world, and perhaps 1500 or so in practical use by Operators. Therefore it is convenient to simply retrieve all records and then filter on the client side. To facilitate this the Service by default returns all records in the system (with the given filter parameters).  The limit parameter can still be used to set the number of records to be returned to a lower number.

-   By default only "valid" entities are returned. Deprecated (invalid)
entities are flagged with \"data.InactiveIndicator\": true, but if
it is absent then it means the entity is in active use (valid in
case of CRS). This property is located in the referenced "allOff
\$ref\$ AbstractReferenceType".  The query generated by the CRS Catalog service by default filters these out. There is a parameter to overwrite this behavior and retrieve invalid records.

-   The CRS Catalog Service by default limits the information returned when a search is performed. The OSDU contains several thousands CRS entities stored as reference
data. Each entity consists of 100-200 rows of information, i.e., the
total size of manifests may be around 20 MB.  A lot of properties are not of interest for many common tasks, where perhaps only the name and ID are needed. There is a flag to return all properties.

Populated reference data manifests for CRS and CT can be found in the OSDU reference data
[CoordinateReferenceSystem.1.1.0.json and CoordinateTransformation.1.1.0.json](https://community.opengroup.org/osdu/data/data-definitions/-/tree/master/ReferenceValues/Manifests/reference-data/LOCAL).


## 1.1 The importance of using the CRS record id

In an OSDU instance the CRS definitions are centrally managed as reference data under LOCAL governance. These records include the actionable definition (persistable reference).

Data and projects should reference the `record id` of the CRS.
Once the CRS is found that belongs to the data, store its id with the data. Since clear EPSG codes are used, the definition can always later be found and is not a practical limitation to interoperability.

It is good practice, but absolutely not necessary, to additionally store the persistable reference (PR) with the data. The ingest workflow should automatically do this by looking up the PR based on the given CRS `record id`, and adding it to the manifest when storing it. This is not working in practice (at least not in R3.M17).

The persistable reference describes the CRS mathematically so that a geodetic engine can execute coordinate operations. Hence, such a persistable reference string becomes independent of the reference data in the particular OSDU instance and the data self-described. This means any consumer will be able to understand the CRS definition even if a different catalog is used in the future context. When an CRS record id is stored with the data and also a PR, then the record id "wins". The PR is considered a snapshot of its definition at the time of loading, which may be useful if question arise in future about the geodetic referencing of the dataset.


**Notes on proper usage of the CRS `record id`:**

-   For interoperability it is essential to adhere to the `record id` convention when storing geodetic reference data in OSDU. 
        These record ids are unique, stable and permanent identifiers for the CRSs and CTs, 
        largely based on the [EPSG Dataset](https://epsg.org).
-   The OSDU CRS and CT reference data `record id` convention will be clear from the examples given, 
        and is described in detail in chapter 4 of
        the [Schema Definition Guide](https://gitlab.opengroup.org/osdu/subcommittees/data-def/work-products/schema/-/tree/master/Guides).
        See the Frame of Reference (FoR) concept, and
        specifically the agreed conventions for the `record id` for geodetic data.  Example are:
    - `osdu:reference-data\--CoordinateReferenceSystem:BoundGeographic2D:EPSG::4267_EPSG::15851`
    - `osdu:reference-data\--CoordinateReferenceSystem:Projected:EPSG::32632`

    These codes may appear cryptic to developers not used to the EPSG Dataset. Human readable names would be easier to interpret. However EPSG codes are stable in the EPSG Dataset which is the de facto global standard reference for geodetic information, while names are not guaranteed stable and are not standardized (in particular for Bound definitions which are not defined in the EPSG Dataset as such and Operators follow their own internal naming conventions). 
    The names for the first example may be looked up in the OSDU reference data or on the epsg.org website and corresponds to the geographic 2D CRS _"NAD27"_ (EPSG CRS code 4267), which is bound in this case to CT with EPSG code 15851 _"NAD27 to WGS 84 (79)"_, which is variant 79, valid for use in the contiguous USA. 
The second examle is the projected CRS _"WGS 84 / UTM zone 32N"_, with EPSG CRS code 32632.  

- When referencing a record id in an API request **a terminating colon must be added to the end** to signal the full id is provided and the latest version needs to be retrieved.

-   A CRS or CT is uniquely identified by it codeSpace + code
        combination (in many Operator databases the code alone will be unique,
        but two operators could have assigned the same code to different
        entities, theoretically). It may be confusing that sub-entities also have 
        codes, and these codes are not unique across geodetic entities (except
        for CRSs and CTs). For example, an extent has a code and that code
        may be equal to the CRS code. Hence, when searching for a code
        (or codeSpace + code) it is better to search through the specific
        entity of interest, to avoid unwanted results to be returned.
-   The code is an integer. For entities in the EPSG CodeSpace the code
        range is from 1024 to 32767 (inclusive), i.e., an EPSG code can
        have four or five digits.
-   In OSDU this integer number may be represented as string in some
        queries. There is a potential risk that when querying for
        code="2044" that also an entity with code="20440" is returned.
        Another example is code="2043" and code="2043\[6-9\]" which are
        all Projected CRSs. It was tested in v3 on 2022-06-20 that this
        worked properly for the CRS Catalog endpoints and no incorrect
        entities were returned.
-   CRS reference data are fundamental entities, but they do reference Unit of Measure.
        In R3.M13 querying for unit of measure using record id may have an issue.
        There are record id like reference-data\--UnitOfMeasure:ft: and
        reference-data\--UnitOfMeasure:ft%5BUS%5D: ("ft\[US\]" for the US
        survey foot). The query needs to be on exact match.


When ingesting data into OSDU, the original "AsIngested" coordinates are used, and **data must be associated with a transformation to WGS 84**.
Unless the data is already in WGS 84, **this requires a reference to a BoundCRS** (BoundProjected or BoundGeographic2D). 
This is explained further in the section below.


## 1.2 CRS concepts

This section covers some basic geodesy concepts for people not familiar with the terminology.

The most important thing to understand is that coordinates **always** require a CRS to be associated with them, 
or their location on the earth cannot be established.
That is true even for latitude and longitude. For example, a point with coordinates latitude 30&deg;N and longitude 90&deg;W 
will plots at different locations depending on whether the associated CRS is WGS 84 or NAD27.
The requirement to specify the associated CRS with coordinates is equally true for horizontal coordinates as for vertical coordinates.

Some information on CRSs may be found on [wikipedia](https://en.wikipedia.org/wiki/Spatial_reference_system) and in particular at the IOGP [EPSG website](https://epsg.org) and guidance notes therein.


**The term CRS**

The term CRS is an abbreviation of **Coordinate Reference System**. A CRS is comprised of two components:
* [Geodetic reference frame](https://en.wikipedia.org/wiki/Geodetic_datum) (datum)
* [Coordinate system](https://en.wikipedia.org/wiki/Coordinate_system) (CS)

The datum is a reference, either horizontal or vertical, from which distances and directions are measured. It defines the connection between the coordinate system and the earth. A geodetic datum defines the position and orientation of the reference ellipsoid relative to the earth, as well as its prime meridan. 
The [ellipsoid](https://en.wikipedia.org/wiki/Ellipsoid) is a mathematical model of earth defined by its:
* semi-major axis _a_
* semi-minor axis _b_ (or more commonly, its inverse flattening _1/f_)

A coordinate system is a set of mathematical rules that specify how points can be given coordinates. It defines the axes' units and orientation with respect to the datum (the axes' positive directions). 

Because geodetic science knows a long history and the mathematical shape is an approximation, there
are hundreds of geodetic datums, often fitted to a single country, landmass, or continent.
Examples are [NAD27](https://en.wikipedia.org/wiki/North_American_Datum), in North America, and [ED50](https://en.wikipedia.org/wiki/ED50) in Europe.

Several geodetic CRS subtypes can be created that have the same datum but different coordinate system. These subtypes are the geographic 2D CRS, geographic 3D CRS, and geocentric CRS. 
- In OSDU only the geographic 2D CRS is used. 

A geodetic CRS using grads or radians instead of degrees as unit of measure is also considered a different CRS.  
- In OSDU only geographic 2D CRSs using degree should be used to avoid unexpected issues that might well occur otherwise (although the engine should be able to handle this properly).

With the introduction of satellites, several earth-centered world-wide approximations have been defined and are constantly being refined.
[WGS 84 (World Geodetic System of 1984)](https://en.wikipedia.org/wiki/World_Geodetic_System) is the most known example and is used ubiquitously.
This is the reference used by the [Global Positioning System (GPS)](https://en.wikipedia.org/wiki/Global_Positioning_System), 
although that uses a specific realization of the WGS 84 ensemble of datums.
A WGS 84 realization is a dynamic CRS. 
Over time, tectonic plates move relatively to each other in this reference system.
This causes the coordinates of objects fixed on a plate to continually change.
These relative shifts are in the order of centimeters per year, and accumulate over time.
It depends on the requirements of the application, magnitude of plate motion, and time span of the data combined in the project
whether WGS 84 is suitable. 
In OSDU WGS 84 is used as the global system for normalization for applications like search and discovery of data, 
spatial awareness, and machine learning. 

For accurate work requiring better than 1-3m accuracy, the WGS 84 coordinates should not be used (at least not without knowing the coordinate epoch of the data and the WGS 84 realization of the datum), because the "fuzzy definition" of WGS 84 prohibits clear labeling. 
Instead, use the original ingested coordinates which are generally stored referenced to a national CRS that is fixed with the tectonic plate (in which coordinates do not change with time, or are reduced to a specific epoch).

* Eventually the geospatial references may become time dependent. 
The OSDU catalog does not contain any time-dependent elements, although the coordinate date can be stored with the data.

**Geographic 2D CRS (latitude and longitude coordinates)**

A geographic 2D CRS is a combination of a geodetic datum and a [2D ellipsoidal coordinate system](https://en.wikipedia.org/wiki/Geodetic_coordinates). These coordinates are known as latitude and longitude, and
is one of the most used coordinate formats. Latitude is the angle from equator plane, and the longitude is the angle from the prime meridian, which is for most CRSs the Greenwich meridian near London in England.
- In OSDU only geographic 2D CRSs using Greenwhich should be used to avoid unexpected issues that might well occur otherwise (although the engine should be able to handle this properly).

**The term CT**

The term CT is an abbreviation of **Coordinate Transformation**. A CT is a coordinate operation with empirically derived parameters that enacts a change of datum. In other
words, after applying a transformation, coordinates are referenced to a different geographic CRS. An example is a transformation from ED50 to WGS 84. Because CTs are empirically derived there are many transformations from ED50 to WGS 84. Each give slightly different answers, and have different accuracies and areas of applicability. 

In ISO 19111, a CT is independent from the CRS.
Coordinate Transformations are modeled in OSDU as such, and the OSDU reference data contains many of such transformations.
Some information on CT methods may be found in [wikipedia](https://en.wikipedia.org/wiki/Geographic_coordinate_conversion#Datum_transformations), and in particular in guidance notes on the [EPSG website](https://epsg.org).


**Bound CRS (a CRS combined with a specific CT)**

The choice of transformation to use in a certain area is non-trivial and typically the responsibility
of geodesists or geomatics specialists and data managers. Ordinary end users or data consumers
may not be able to make such decisions.

This is the reason why so-called **early-bound coordinate reference systems**
were introduced: An early-bound CRS pairs a regular (late-bound or un-bound) CRS 
with a coordinate transformation (CT). 
- In OSDU the CT used in a Bound CRS must be "to WGS 84"

Different early-bound CRSs
then enable the *any datum* to *any datum* case, via the hub CRS WGS 84.

As already mentioned in the introduction, this implies that all data ingested into OSDU must be associated with a Bound CRS (unless it is referenced already to WGS 84). Once that is done, the end-user does not have to worry about this association, and the data can be transformed from any CRS to any CRS (within the accuracy limitations).

-   For accurate work, a direct transformation is to be preferred over a hub transformation.
    OSDU (CRS Convert service) currently does not support direct transformations.  
    The CoordinateTransformation reference data can contain direct transformations.

**Projected CRS (map projections, easting and northing coordinates)**

All work is done on a map (or flat screen) in a [2D Cartesian coordinate system](https://en.wikipedia.org/wiki/Cartesian_coordinate_system),
i.e., both axes are measured in length using the same unit.
A projected CRS is derived from a base geographic CRS by means of applying a map projection formula.
All projected CRSs are initially
defined as late-bound or un-bound CRSs. When such a CRS is bound to a
coordinate transformation, a projected, early-bound CRS is created, called `BoundProjected`.

**Vertical CRS (height and depth coordinates)**
In the same way as for horizontal CRS, vertical CRS also consists of a datum which defines the reference surface, and a coordinate system (CS). The datum defines where the heights/depths are measured from and the
coordinate system defines the unit (meters/feet) and the direction (positive up/down).
The third dimension may be implicitly assumed as height above mean sea level (MSL). Such height coordinates, physically referenced 
to a gravity-related surface are independent of the horizontal coordinates
that are geometrically referenced to an ellipsoid.
In OSDU the geographic CRSs are 2D.
Transformations between 2D CRSs will NOT
change the vertical values (z-coordinate) since the values refer
implicitly to the same MSL reference surface.

Vertical transformations do exist, e.g., from MSL to LAT (lowest Astronomical Tide), 
or from geometrical ellipsoidal heights to gravity based heights.  
Such operations are currently not supported in the OSDU Convert service, although the Catalog service can model them.

-   Note that, similar to the horizontal case of associating data with a generic "WGS 84" without further 
    qualifying the coordinate epoch and frame, also in the vertical case labeling data with "MSL" 
    (or not associating a vertical CRS at all) results in an ambiguity in vertical location.
    This ambiguity may be in the order of a few centimeters to ~1m, 
    depending on the differences between the different vertical reference surfaces (e.g., MSL vs LAT).
-   Also note that a vertical reference point such as a drill floor is not a geodetic vertical datum surface. 
    The height of such point requires the geodetic datum to be specified. 


**Compound CRS (2D + 1D)**

Compound CRSs are not used in the OSDU.
The concept of a Compound CRS combine
a horizontal CRS (a conventional 2D CRS) with a 1D vertical CRS.

**Area of Use**

Finally, to aid the selection process, `CRS` and `CT` can be associated
`AreaOfUse` definitions, which provide a simple bounding box in WGS 84
latitude and longitude. The CRS catalog offers a list of such definitions.

**Summary of the main CRS and CT types in OSDU**

Overview of CRS "kinds" in the CoordinateReferenceSystem:1.1.0 reference data delivered by the CRS Catalog:

Item Type | Link to Item Details
----------|---------------------
`Geographic2D`      | A 2D geographic CRS (latitude, longitude).
`Projected`         | A 2D projected CRS (easting, northing).
`BoundGeographic2D` | A 2D geographic CRS, bound with a CT to WGS 84.  Does not include "WGS 84" itself.
`BoundProjected`    | A 2D projected CRS, bound with a CT to WGS 84.  Does not include projected CRSs based on "WGS 84".
`Vertical`          | A 1D vertical CRS (height or depth).


CT "kinds" in the CoordinateTransformation:1.1.0 reference data delivered by the CRS Catalog:

Item Type | Link to Item Details
----------|---------------------
`SingleCT`        | Horizontal transformation between two geographic CRSs.
`ConcatenatedCT`  | Horizontal transformation between two geographic CRSs, consisting of multiple steps (e.g., from A to B, followed by B to C)
`VerticalCT`      | Vertical Transformation (not currently supported by the conversion engine).



# 2. CRS Catalog  endpoints

The endpoints for CRS Catalog v3 are specified via the [Swagger documentation](https://community.opengroup.org/osdu/platform/system/reference/crs-catalog-service/-/blob/master/docs/api_spec/crs-catalog-openapi-v3.yaml). The endpoints are:

| **Endpoint**                | **Synopsis**                              |
| --------------------------- | ----------------------------------------- |
| GET and POST _coordinate-reference-system_ | Fetch CRS(s) details.  |
| GET and POST _coordinate-transformation_   | Fetch CT(s) details.   |
| POST _points-in-aou_               | Check if given points are inside the area of use of a CRS or CT. |
| GET _info_ | Return general information about the service configuration. |



# 3. Fetch the details of a single CRS or CT

_User story: Retrieve all properties for a single CRS or CT entity (for example) in an application where a user clicks on a button and the application fetches details._

## 3.1 Context

-   The GET function is intended as a simple endpoint **to return detailed information for a
    single CRS or CT entity**. It accepts a record id or data ID as URL
    parameter. The advantage of using the data ID is its shorter syntax.
    Examples for a data ID and record id are:
    - `dataId`    BoundGeographic2D:EPSG::4267_EPSG::15851
    - `recordId` osdu:reference-data\--CoordinateReferenceSystem:BoundGeographic2D:EPSG::4267_EPSG::15851

        - A terminating colon is not required for the record id in this simplified syntax using url parameters (at 2023-09-09 there is an open issue to accept a terminating colon as is the standard in OSDU when referencing a record by its id).

-   The response of the CRS Catalog Service shows the query that was made to retrieve the record (these endpoints are simply a wrapper to the standard Search Service).  Hence, alternatively to using this endpoint, the record can be retrieved by the Search Service or Storage Service directly. This is shown below. 



## 3.2 Simple examples

All properties are returned for the CRS and CT as shown below in the
example response. There is an argument to be made to return less detail.
However, since this is only a single record and this was the most
generic implementation it is left that way.

**Request**

*Coordinate Reference System examples*

By record-id:
```
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system?recordId=osdu:reference-data\--CoordinateReferenceSystem:Geographic2D:EPSG::4158
```

By data-ID:
```
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system?dataId=Geographic2D:EPSG::4158
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system?dataId=BoundGeographic2D:EPSG::4267_EPSG::15851
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system?dataId=Projected:EPSG::32065
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system?dataId=BoundProjected:EPSG::32065_EPSG::15851
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system?dataId=Vertical:EPSG::5714

Check to confirm that 2043 is returned and not also 20436:
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system?dataId=Projected:EPSG::2043
```


*Coordinate Transformation examples*

By record-id:
```
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-transformation?recordId=osdu:reference-data\--CoordinateTransformation:EPSG::1111
```

Get details of a single CT using data.ID
```
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-transformation?dataId=EPSG::1166
```

Get details of a single CT not to/from WGS 84 using data.ID (AGD66 to GDA94 (1) \[EPSG::1278\])
```
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-transformation?dataId=EPSG::1278
```

Get details of a single concatenated CT not to/from WGS 84 object by
data.ID (Amersfoort to ED50 (1)):
```
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-transformation?dataId=EPSG::4837
```

Get details of a vertical CT using record-id:
```
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-transformation?dataId=EPSG::5429
```


**Example Response**

<details><summary>Click to expand: Response for Geographic2D:EPSG::4158 ("Naparima 1955")</summary>

```json
{
    "searchResults": {
        "results": [
            {
                "data": {
                    "PreferredUsage.Extent.Description": "Trinidad and Tobago - Trinidad - onshore.",
                    "PersistableReference": "{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"4158\"},\"name\":\"GCS_Naparima_1955\",\"type\":\"LBC\",\"ver\":\"PE_10_9_1\",\"wkt\":\"GEOGCS[\\\"GCS_Naparima_1955\\\",DATUM[\\\"D_Naparima_1955\\\",SPHEROID[\\\"International_1924\\\",6378388.0,297.0]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433],AUTHORITY[\\\"EPSG\\\",4158]]\"}",
                    "PreferredUsage.AuthorityCode.Code": 3043,
                    "Wgs84Coordinates": {
                        "geometries": [
                            {
                                "coordinates": [
                                    [
                                        [
                                            -61.98,
                                            9.99
                                        ],
                                        [
                                            -60.85,
                                            9.99
                                        ],
                                        [
                                            -60.85,
                                            10.9
                                        ],
                                        [
                                            -61.98,
                                            10.9
                                        ],
                                        [
                                            -61.98,
                                            9.99
                                        ]
                                    ]
                                ],
                                "type": "polygon"
                            }
                        ],
                        "type": "geometrycollection"
                    },
                    "ResourceCurationStatus": null,
                    "VerticalCRS.AuthorityCode.Authority": null,
                    "Name": "Naparima 1955",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 9.99,
                    "BaseCRS.AuthorityCode.Authority": null,
                    "HorizontalCRS.Name": null,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": -61.98,
                    "PreferredUsage.Name": "Trinidad and Tobago - Trinidad - onshore",
                    "Kind": "geographic 2D",
                    "ResourceSecurityClassification": null,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "VerticalCRS.Name": null,
                    "ID": "Geographic2D:EPSG::4158",
                    "ExistenceKind": null,
                    "Usages": [
                        {
                            "Scope": {
                                "AuthorityCode": {
                                    "Authority": "EPSG",
                                    "Code": 1027
                                },
                                "Name": "Geodesy."
                            },
                            "Extent": {
                                "Description": "Trinidad and Tobago - Trinidad - onshore.",
                                "BoundingBoxWestBoundLongitude": -61.98,
                                "AuthorityCode": {
                                    "Authority": "EPSG",
                                    "Code": 3143
                                },
                                "BoundingBoxNorthBoundLatitude": 10.9,
                                "BoundingBoxEastBoundLongitude": -60.85,
                                "BoundingBoxSouthBoundLatitude": 9.99,
                                "Name": "Trinidad and Tobago - Trinidad - onshore"
                            },
                            "AuthorityCode": {
                                "Authority": "EPSG",
                                "Code": 3043
                            },
                            "Name": "Trinidad and Tobago - Trinidad - onshore"
                        }
                    ],
                    "DatumEnsemble.AuthorityCode.Authority": null,
                    "Projection.Name": null,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1027,
                    "DatumEnsemble.Name": null,
                    "HorizontalCRS.HorizontalCRSID": null,
                    "Code": "4158",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "BaseCRS.BaseCRSID": null,
                    "AttributionAuthority": null,
                    "Datum.Name": "Naparima 1955",
                    "AttributionRevision": null,
                    "TargetCRS.AuthorityCode.Authority": null,
                    "Datum.AuthorityCode.Authority": "EPSG",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "TargetCRS.Name": null,
                    "Description": "Extended to Tobago as Naparima 1972. (Note: Naparima 1972 is not used in Trinidad).",
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": -60.85,
                    "SourceCRS.SourceCRSID": null,
                    "ResourceLifecycleStatus": null,
                    "Transformation.AuthorityCode.Authority": null,
                    "TechnicalAssuranceID": null,
                    "TargetCRS.TargetCRSID": null,
                    "Source": "Workbook Resources/IOGP/Manifests/reference-data/CoordinateReferenceSystem.1.1.0.json; commit SHA a046fbd2.",
                    "Projection.AuthorityCode.Authority": null,
                    "SourceCRS.AuthorityCode.Authority": null,
                    "Datum.AuthorityCode.Code": 6158,
                    "VerticalCRS.VerticalCRSID": null,
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:dega:",
                    "PreferredUsage.Extent.AuthorityCode.Code": 3143,
                    "Transformation.Name": null,
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 10.9,
                    "CommitDate": "2022-04-02T14:45:12+0200",
                    "AttributionPublication": null,
                    "InactiveIndicator": null,
                    "CodeSpace": "EPSG",
                    "CodeAsNumber": 4158,
                    "Transformation.TransformationID": null,
                    "BaseCRS.Name": null,
                    "InformationSource": null,
                    "ResourceHomeRegionID": null,
                    "RevisionDate": "2017-06-13T00:00:00+0000",
                    "CoordinateReferenceSystemType": "GeodeticCRS",
                    "SourceCRS.Name": null,
                    "PreferredUsage.Extent.Name": "Trinidad and Tobago - Trinidad - onshore",
                    "CoordinateSystem.Name": "Ellipsoidal 2D CS. Axes: latitude, longitude. Orientations: north, east. UoM: degree",
                    "CoordinateSystem.AuthorityCode.Code": 6422,
                    "PreferredUsage.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Scope.Name": "Geodesy.",
                    "HorizontalCRS.AuthorityCode.Authority": null
                },
                "kind": "osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0",
                "source": "wks",
                "acl": {
                    "viewers": [
                        "data.default.owners@osdu.example.com"
                    ],
                    "owners": [
                        "data.default.viewers@osdu.example.com"
                    ]
                },
                "type": "reference-data--CoordinateReferenceSystem",
                "version": 1656110323137907,
                "tags": null,
                "modifyUser": "serviceprincipal@testing.com",
                "modifyTime": "2022-06-24T22:38:43.788Z",
                "createTime": "2022-06-24T22:36:44.108Z",
                "authority": "osdu",
                "namespace": "osdu:wks",
                "legal": {
                    "legaltags": [
                        "osdu-public-usa-dataset"
                    ],
                    "otherRelevantDataCountries": [
                        "US"
                    ],
                    "status": "compliant"
                },
                "createUser": "serviceprincipal@testing.com",
                "id": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4158"
            }
        ],
        "totalCount": 1
    },
    "query": "id: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4158\""
}
```

</details>



### 3.2.1 Alternative 1: retrieve a single CRS/CT record using the Search service

The CRS Catalog service GET functions essentially retrieve a
single record from storage by performing a standard query. Hence, it is also
possible to use the OSDU Search service or Storage service to accomplish
the same. The search service gives full control over the fields to be
returned as described on its tutorial page and shown below for examples
fetching a list of CRSs.

An alternative is to use the OSDU standard search service to retrieve
the full record based on the record id as follows (add the
"returnedFields" parameter to limit the amount of returned properties).

_{{osduonaws_base_url}}/api/search/v2/query_
```json
{
    "kind": "osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0",
    "query": "id: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326\""
}
```

Another test is for Projected CRS with code 2043, whereas also Projected
CRS with codes 20436 to 20439 exist. This was confirmed on 2022-06-18 to
work correctly.

_{{osduonaws_base_url}}/api/search/v2/query_
```json
{
    "kind": "osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0",
    "query": "id: \"osdu:reference-data--CoordinateReferenceSystem:Projected:EPSG::2043\""
}
```


### 3.2.2 Alternative 2: retrieve a single CRS/CT record using the Storage service

Another alternative that will yield the same full record is to use the
OSDU storage service query records endpoint.

There is no reason to use this method for CRS records. Even more so
because with this method there appears to be an issue that integers
(e.g., 4326) are returned as floating point numbers ("4326.0").


**Request** _{{osduonaws_base_url}}/api/storage/v2/query/records_
```json
{"records": [
	"{{NAMESPACE}}:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326"
]}
```

**Response**

<details><summary>(click to expand)</summary>

```
{
    "records": [
        {
            "id": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326",
            "version": 1653171019401926,
            "kind": "osdu:wks:reference-data--CoordinateReferenceSystem:1.0.0",
            "acl": {
                "viewers": [
                    "data.default.viewers@osdu.example.com"
                ],
                "owners": [
                    "data.default.owners@osdu.example.com"
                ]
            },
            "legal": {
                "legaltags": [
                    "osdu-demo-legaltag"
                ],
                "otherRelevantDataCountries": [
                    "US"
                ],
                "status": "compliant"
            },
            "data": {
                "BaseCRS": {
                    "AuthorityCode": {
                        "Authority": "EPSG",
                        "Code": 4979.0
                    },
                    "BaseCRSID": "osdu:reference-data--CoordinateReferenceSystem:Geographic3D:EPSG::4979:",
                    "Name": "WGS 84"
                },
                "Code": "4326",
                "CodeAsNumber": 4326.0,
                "CodeSpace": "EPSG",
                "CoordinateReferenceSystemType": "GeodeticCRS",
                "CoordinateSystem": {
                    "AuthorityCode": {
                        "Authority": "EPSG",
                        "Code": 6422.0
                    },
                    "Name": "Ellipsoidal 2D CS. Axes: latitude, longitude. Orientations: north, east. UoM: degree",
                    "HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:dega:"
                },
                "Datum": {
                    "AuthorityCode": {
                        "Authority": "EPSG",
                        "Code": 6326.0
                    },
                    "Name": "World Geodetic System 1984 ensemble"
                },
                "ID": "Geographic2D:EPSG::4326",
                "Kind": "geographic 2D",
                "Name": "WGS 84",
                "PersistableReference": "{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"4326\"},\"name\":\"GCS_WGS_1984\",\"type\":\"LBC\",\"ver\":\"PE_10_9_1\",\"wkt\":\"GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433],AUTHORITY[\\\"EPSG\\\",4326]]\"}",
                "PreferredUsage": {
                    "AuthorityCode": {
                        "Authority": "EPSG",
                        "Code": 3202.0
                    },
                    "Extent": {
                        "AuthorityCode": {
                            "Authority": "EPSG",
                            "Code": 1262.0
                        },
                        "BoundingBoxEastBoundLongitude": 180.0,
                        "BoundingBoxNorthBoundLatitude": 90.0,
                        "BoundingBoxSouthBoundLatitude": -90.0,
                        "BoundingBoxWestBoundLongitude": -180.0,
                        "Name": "World"
                    },
                    "Name": "World",
                    "Scope": {
                        "AuthorityCode": {
                            "Authority": "EPSG",
                            "Code": 1183.0
                        },
                        "Name": "Horizontal component of 3D system."
                    }
                },
                "RevisionDate": "2020-03-14T00:00:00+00:00",
                "Usages": [
                    {
                        "AuthorityCode": {
                            "Authority": "EPSG",
                            "Code": 3202.0
                        },
                        "Extent": {
                            "AuthorityCode": {
                                "Authority": "EPSG",
                                "Code": 1262.0
                            },
                            "BoundingBoxEastBoundLongitude": 180.0,
                            "BoundingBoxNorthBoundLatitude": 90.0,
                            "BoundingBoxSouthBoundLatitude": -90.0,
                            "BoundingBoxWestBoundLongitude": -180.0,
                            "Name": "World"
                        },
                        "Name": "World",
                        "Scope": {
                            "AuthorityCode": {
                                "Authority": "EPSG",
                                "Code": 1183.0
                            },
                            "Name": "Horizontal component of 3D system."
                        }
                    }
                ]
            },
            "meta": [],
            "tags": {}
        }
    ],
    "invalidRecords": [],
    "retryRecords": []
}
```
</details>


# 4. Fetch a list of CRSs

_User story: As a user or data manager I would like to be presented with a list of CRSs to associate with data to load or to use for project
setup._


## 4.1 Context

-   The reason for the CRS Catalog Data Domain service is to simplify
    common tasks. One of such tasks is to get a list of CRSs of a
    certain type which would require a complicated query or multiple
    queries with the Search API. Another reason is to do specific tasks
    that can not be done directly with the Search API in OSDU, e.g., to
    check if the location of a point is inside the Area of Use of a
    given CRS. Another example is that the CRS Catalog API by default
    returns all entities except for deprecated ones. The search API
    limits the results to the first ten by default, including invalid (deprecated) records.

-   OSDU is using a boundCRS approach which implies that a CRS from a
    list of **BoundProjected** or **BoundGeographic2D** entities needs
    to be associated with ingested data. OSDU considers WGS 84 and
    Projected CRSs based on WGS 84 as late bound (not as BoundCRS).
    Therefore the previous list in general needs to be extended with
    this list. Option "**returnBoundProjectedAndProjectedBasedOnWgs84**"
    accomplishes this.

-   A similar requirement exist for vertical coordinates to be
    associated with a Vertical CRS contained in the OSDU CRS reference
    data.

### 4.1.1 Parameters for the POST coordinate-reference-point endpoint

-   The list of input parameters is given in the online api spec [Swagger documentation](https://community.opengroup.org/osdu/platform/system/reference/crs-catalog-service/-/tree/master/docs/api_spec).

    -   The main parameters are:
        - **kind**: the type of CRS to return.
        - **returnBoundProjectedAndProjectedBasedOnWgs84**: A shorthand to get both these kinds of CRS.
        - **returnBoundGeographic2DAndWgs84**: A shorthand to return both these kinds.
        - **horizontalAxisUnitId**: Unit of Measure of the CRS.
        - **latitude, longitude**: Only return records valid at that location
        - **returnAllFields**: A flag to return all fields (by default a subset is returned).

    -   Inputs are combined with AND, e.g., to return a list of CRSs
        using a certain unit AND of kind projected.

-   A practical approach is to fetch a list, and perform sorting and
    filtering on the client side. This is partially driven by limitation
    of the search API. Nonetheless, the API has several parameters to
    filter.

    -   The returned sort order is unclear in OSDU. Logically it would
        be on CRS name, but the help says that alphabetic sort is not
        supported and hence the client would have to do this when presenting a choice to the end-user.

    -   Wildcard search is limited. For example to search for a CRS name
        or area in the description the full word is needed.



### 4.1.2 Performance check

Some performance indication is provided because a normal use case is to
recover all CRS records to show to a user for selection. The size of the
CRS reference data may be around 20MB due to the explicit definition
method containing a fairly large amount of properties. Performance does
not appear to be an issue:

-   To retrieve all records of CoordinateReferenceSystem:1.1.0 with all
    properties takes about 5 seconds via the standard Search service as
    well as with the CRS Catalog service.

-   Returning a subset of properties is convenient in the Search service
    and seems slightly faster overall depending on bandwidth. This
    filtering of returned Fields is not possible with the CRS Catalog
    service.

```json
{
    "kind": "osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0",
    "query": "data.Kind: BoundProjected",
    "returnedFields": ["id", "data.Name", "data.CoordinateSystem.HorizontalAxisUnitID", "data.PersistableReference"],
    "limit": 9999
}
```

-   Spatial query on location also does not take very long (seconds).
    That is partly because no complex polygon queries are available but only
    simple rectangular bounding boxes. This limits the usefulness of the latitude, longitude input parameters.

## 4.2 Retrieve the details of a single CRS

This first test is very similar to the functionality described in the previous
section. The POST endpoint was intended to execute a search and return a list of records
with a subset of properties. If a user required full
details then use the GET function in a second call, or use the option
"**returnAllFields**". 

Nonetheless, this endpoint allows us to retrieve a single record
with a unique codeSpace-code combination as follows:

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_
```json
{
    "codeSpace": "EPSG",
    "code": "32065"
}
```


*Note: "codeSpace" could be omitted if all codes are unique in the database.

An alternative request body that leads to the same response is to specify the data.ID of a record:

```json
{
    "id": "Geographic2D:EPSG::4158"
}
```

Or by using the name of the CRS

```json
{
  "name": "NAD27 / BLM 15N (ftUS)"
}
```



**Response**

```json
{
    "searchResults": {
        "results": [
            {
                "data": {
                    "PreferredUsage.Extent.Description": "United States (USA) - between 96°W and 90°W - Arkansas; Illinois; Iowa; Kansas; Louisiana; Michigan; Minnesota; Mississippi; Missouri; Nebraska; Oklahoma; Tennessee; Texas; Wisconsin; Gulf of Mexico outer continental shelf (GoM OCS) between approximately 96°W and 90°W - protraction areas East Breaks; Alaminos Canyon; Garden Banks; Keathley Canyon; Sigsbee Escarpment; Ewing Bank; Green Canyon; Walker Ridge; Amery Terrace.",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "Description": null,
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": -89.86,
                    "PreferredUsage.AuthorityCode.Code": 7179,
                    "Name": "NAD27 / BLM 15N (ftUS)",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 25.61,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": -96.01,
                    "PreferredUsage.Name": "USA - 96°W to 90°W and GoM OCS",
                    "Kind": "projected",
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:ft%5BUS%5D:",
                    "PreferredUsage.Extent.AuthorityCode.Code": 3640,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 49.38,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1249,
                    "InactiveIndicator": null,
                    "CodeSpace": "EPSG",
                    "Code": "32065",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "RevisionDate": "2010-03-05T00:00:00+0000",
                    "Datum.Name": null,
                    "PreferredUsage.Extent.Name": "USA - 96°W to 90°W and GoM OCS",
                    "CoordinateSystem.Name": "Cartesian 2D CS. Axes: easting, northing (X,Y). Orientations: east, north. UoM: ftUS.",
                    "CoordinateSystem.AuthorityCode.Code": 4497,
                    "PreferredUsage.AuthorityCode.Authority": "EPSG",
                    "Datum.AuthorityCode.Authority": null,
                    "PreferredUsage.Scope.Name": "Minerals management (including oil and gas exploration and production)."
                },
                "id": "osdu:reference-data--CoordinateReferenceSystem:Projected:EPSG::32065"
            }
        ],
        "totalCount": 1
    },
    "query": "(NOT data.InactiveIndicator: true) AND (data.CodeSpace: \"EPSG\") AND (data.Code: \"32065\")"
}
```



## 4.3 Fetch all CRSs of a specific Kind (e.g., BoundProjected)

The following **Kind** of CRSs in OSDU are of main interest:

-   *Geographic2D (in particular WGS 84 which is not considered a BoundGeographic2D in OSDU)*
-   *Projected (in particular those based on WGS 84 who are not part of BoundProjected)*
-   *BoundGeographic2D*
-   *BoundProjected*
-   *Vertical*

**Request**  _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_

```json
{
    "kind": "BoundProjected",
    "limit": 9999
}
```
Note: "limit" is defaulted to 9999 in the API such that all elements
are returned in search results (which would be the common use case).


**Response**

```json
...
        {
                "data": {
                    "PreferredUsage.Extent.Description": "Description intersection between CRS and CT 'Europe - between 0°E and 6°E - Andorra; Denmark (North Sea); Germany offshore; Netherlands offshore; Norway including Svalbard - onshore and offshore; Spain - onshore (mainland and Balearic Islands); United Kingdom (UKCS) offshore.' (CRS) and 'Norway - offshore south of 62°N - North Sea.' (CT) [1634,2334]",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "Description": null,
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": 6.01,
                    "Name": "ED50 * EPSG-Nor S62 2001 / UTM zone 31N [23031,1613]",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 56.08,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": 1.37,
                    "PreferredUsage.Name": "Usage intersection between CRS and CT 'Europe - 0°E to 6°E and ED50 by country' (CRS) and 'Norway - North Sea - offshore south of 62°N' (CT) [6394,8534]",
                    "Kind": "BoundProjected",
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:m:",
                    "PreferredUsage.Extent.AuthorityCode.Authority": null,
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 62.0,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1142,
                    "InactiveIndicator": null,
                    "CodeSpace": "OSDU",
                    "Code": "23031024",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "RevisionDate": "2022-04-04T16:54:46+0000",
                    "Datum.Name": null,
                    "PreferredUsage.Extent.Name": "Extent intersection between CRS and CT 'Europe - 0°E to 6°E and ED50 by country' (CRS) and 'Norway - North Sea - offshore south of 62°N' (CT) [1634,2334]",
                    "CoordinateSystem.Name": "Cartesian 2D CS. Axes: easting, northing (E,N). Orientations: east, north. UoM: m.",
                    "CoordinateSystem.AuthorityCode.Code": 4400,
                    "PreferredUsage.AuthorityCode.Authority": null,
                    "Datum.AuthorityCode.Authority": null,
                    "PreferredUsage.Scope.Name": "Engineering survey, topographic mapping."
                },
                "id": "osdu:reference-data--CoordinateReferenceSystem:BoundProjected:EPSG::23031_EPSG::1613"
            }
        ],
        "totalCount": 999
    },
    "query": "(NOT data.InactiveIndicator: true) AND (data.Kind: \"BoundProjected\")"
}
```
<br>
<br>


### Fetch only the two first projected CRSs

**Request**  _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_

```json
{
    "kind": "projected",
    "limit": 2
}
```

**Respone**
```json
{
    "searchResults": {
        "results": [
            {
                "data": {
                    "PreferredUsage.Extent.Description": "United States (USA) - Nevada - counties of Clark; Elko; Eureka; Lincoln; White Pine.",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "Description": null,
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": -114.03,
                    "PreferredUsage.AuthorityCode.Code": 7125,
                    "Name": "NAD27 / Nevada East",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 34.99,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": -117.01,
                    "PreferredUsage.Name": "USA - Nevada - SPCS - E",
                    "Kind": "projected",
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:ft%5BUS%5D:",
                    "PreferredUsage.Extent.AuthorityCode.Code": 2224,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 42.0,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1142,
                    "InactiveIndicator": null,
                    "CodeSpace": "EPSG",
                    "Code": "32007",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "RevisionDate": "2000-03-07T00:00:00+0000",
                    "Datum.Name": null,
                    "PreferredUsage.Extent.Name": "USA - Nevada - SPCS - E",
                    "CoordinateSystem.Name": "Cartesian 2D CS. Axes: easting, northing (X,Y). Orientations: east, north. UoM: ftUS.",
                    "CoordinateSystem.AuthorityCode.Code": 4497,
                    "PreferredUsage.AuthorityCode.Authority": "EPSG",
                    "Datum.AuthorityCode.Authority": null,
                    "PreferredUsage.Scope.Name": "Engineering survey, topographic mapping."
                },
                "id": "osdu:reference-data--CoordinateReferenceSystem:Projected:EPSG::32007"
            },
            {
                "data": {
                    "PreferredUsage.Extent.Description": "United States (USA) - Kansas - counties of Atchison; Brown; Cheyenne; Clay; Cloud; Decatur; Dickinson; Doniphan; Douglas; Ellis; Ellsworth; Geary; Gove; Graham; Jackson; Jefferson; Jewell; Johnson; Leavenworth; Lincoln; Logan; Marshall; Mitchell; Morris; Nemaha; Norton; Osborne; Ottawa; Phillips; Pottawatomie; Rawlins; Republic; Riley; Rooks; Russell; Saline; Shawnee; Sheridan; Sherman; Smith; Thomas; Trego; Wabaunsee; Wallace; Washington; Wyandotte.",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "Description": null,
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": -94.58,
                    "PreferredUsage.AuthorityCode.Code": 6612,
                    "Name": "NAD27 / Kansas North",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 38.52,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": -102.06,
                    "PreferredUsage.Name": "USA - Kansas - SPCS - N",
                    "Kind": "projected",
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:ft%5BUS%5D:",
                    "PreferredUsage.Extent.AuthorityCode.Code": 2200,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 40.01,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1142,
                    "InactiveIndicator": null,
                    "CodeSpace": "EPSG",
                    "Code": "26777",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "RevisionDate": "1995-06-02T00:00:00+0000",
                    "Datum.Name": null,
                    "PreferredUsage.Extent.Name": "USA - Kansas - SPCS - N",
                    "CoordinateSystem.Name": "Cartesian 2D CS. Axes: easting, northing (X,Y). Orientations: east, north. UoM: ftUS.",
                    "CoordinateSystem.AuthorityCode.Code": 4497,
                    "PreferredUsage.AuthorityCode.Authority": "EPSG",
                    "Datum.AuthorityCode.Authority": null,
                    "PreferredUsage.Scope.Name": "Engineering survey, topographic mapping."
                },
                "id": "osdu:reference-data--CoordinateReferenceSystem:Projected:EPSG::26777"
            }
        ],
        "totalCount": 991
    },
    "query": "(NOT data.InactiveIndicator: true) AND (data.Kind: \"projected\")"
}

```

## 4.4 Fetch all CRSs of a specific "kind" which area of use includes a specific location

To support a user with a list of CRSs appropriate for the given
location. Note that the applicability of this test is limited because
the rectangular bounding box is searched, and not the more accurate
polygon (which is not stored in OSDU). Nonetheless, this search will
filter out some CRSs that are not appropriate at the given location.

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_
```json
{
    "kind": "BoundProjected",
    "latitude": 30.0,
    "longitude": -90
}
```


**Response**

```json
...

            {
                "modifyTime": "2022-06-24T22:38:58.488Z",
                "data": {
                    "Description": null,
                    "InactiveIndicator": null,
                    "ID": "BoundProjected:EPSG::32165_EPSG::1188",
                    "Code": "32165001",
                    "Source": "Workbook Resources/IOGP/Manifests/reference-data/CoordinateReferenceSystem.1.1.0.json; commit SHA 86f383ba.",
                    "CommitDate": "2022-04-13T18:29:10+0200",
                    "Name": "NAD83 * DMA-N Am / BLM 15N (ftUS) [32165,1188]"
                },
                "kind": "osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0",
                "id": "osdu:reference-data--CoordinateReferenceSystem:BoundProjected:EPSG::32165_EPSG::1188",
                "version": 1656110338005685
            }
        ],
        "totalCount": 19
    },
    "query": "(data.Kind: \"BoundProjected\")"
}
```

## 4.5 Fetch all CRSs that use a specific horizontal unit (e.g., meters)

To create an application that only works in projected CRSs with unit
meters.

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_

```json
{
    "kind": "BoundProjected",
    "horizontalAxisUnitId": "osdu:reference-data--UnitOfMeasure:m:"
}

```


*Notes:

-   *Searching for unit "ft" has a bug (it return ft\[US\] and
    ft\[GC\]). The generic Search query suffers from this issue it
    seems.*

-   *Omitting the NAMESPACE (osdu) also works, i.e.,
    \"horizontalAxisUnitId\": \"reference-data\--UnitOfMeasure:m:\"*

**Response**

```json
...
            {
                "data": {
                    "PreferredUsage.Extent.Description": "Description intersection between CRS and CT 'Europe - between 0°E and 6°E - Andorra; Denmark (North Sea); Germany offshore; Netherlands offshore; Norway including Svalbard - onshore and offshore; Spain - onshore (mainland and Balearic Islands); United Kingdom (UKCS) offshore.' (CRS) and 'Norway - offshore south of 62°N - North Sea.' (CT) [1634,2334]",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "Description": null,
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": 6.01,
                    "Name": "ED50 * EPSG-Nor S62 2001 / UTM zone 31N [23031,1613]",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 56.08,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": 1.37,
                    "PreferredUsage.Name": "Usage intersection between CRS and CT 'Europe - 0°E to 6°E and ED50 by country' (CRS) and 'Norway - North Sea - offshore south of 62°N' (CT) [6394,8534]",
                    "Kind": "BoundProjected",
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:m:",
                    "PreferredUsage.Extent.AuthorityCode.Authority": null,
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 62.0,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1142,
                    "InactiveIndicator": null,
                    "CodeSpace": "OSDU",
                    "Code": "23031024",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "RevisionDate": "2022-04-04T16:54:46+0000",
                    "Datum.Name": null,
                    "PreferredUsage.Extent.Name": "Extent intersection between CRS and CT 'Europe - 0°E to 6°E and ED50 by country' (CRS) and 'Norway - North Sea - offshore south of 62°N' (CT) [1634,2334]",
                    "CoordinateSystem.Name": "Cartesian 2D CS. Axes: easting, northing (E,N). Orientations: east, north. UoM: m.",
                    "CoordinateSystem.AuthorityCode.Code": 4400,
                    "PreferredUsage.AuthorityCode.Authority": null,
                    "Datum.AuthorityCode.Authority": null,
                    "PreferredUsage.Scope.Name": "Engineering survey, topographic mapping."
                },
                "id": "osdu:reference-data--CoordinateReferenceSystem:BoundProjected:EPSG::23031_EPSG::1613"
            }
        ],
        "totalCount": 855
    },
    "query": "(NOT data.InactiveIndicator: true) AND (data.Kind: \"BoundProjected\") AND (data.CoordinateSystem.HorizontalAxisUnitID: \"osdu:reference-data--UnitOfMeasure:m:\")"
}
```


## 4.6 Fetch all Projected CRSs based on WGS 84 that use the unit meter

As mentioned above, a projected CRS based on WGS 84 is **not**
considered a BoundCRS in OSDU. Therefore a common task is to fetch all
BoundProjected CRSs and also all Projected CRSs and to combine the
responses.

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_
```json
{
    "kind": "Projected",
    "horizontalAxisUnitId": "osdu:reference-data--UnitOfMeasure:m:",
    "baseCRS": {"id": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326"}
}
```

- Note: It is unclear why there would not be a terminating colon for the referenced CRS record id.

**Response**
```json
{
    "searchResults": {
        "results": [
            {
...
                "data": {
                    "PreferredUsage.Extent.Description": "Between 66°E and 72°E, southern hemisphere between 80°S and equator, onshore and offshore.",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "Description": null,
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": 72.0,
                    "PreferredUsage.AuthorityCode.Code": 7621,
                    "Name": "WGS 84 / UTM zone 42S",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": -80.0,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": 66.0,
                    "PreferredUsage.Name": "World - S hemisphere - 66°E to 72°E - by country",
                    "Kind": "projected",
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:m:",
                    "PreferredUsage.Extent.AuthorityCode.Code": 2083,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 0.0,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1142,
                    "InactiveIndicator": null,
                    "CodeSpace": "EPSG",
                    "Code": "32742",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "RevisionDate": "2020-03-14T00:00:00+0000",
                    "Datum.Name": null,
                    "PreferredUsage.Extent.Name": "World - S hemisphere - 66°E to 72°E - by country",
                    "CoordinateSystem.Name": "Cartesian 2D CS. Axes: easting, northing (E,N). Orientations: east, north. UoM: m.",
                    "CoordinateSystem.AuthorityCode.Code": 4400,
                    "PreferredUsage.AuthorityCode.Authority": "EPSG",
                    "Datum.AuthorityCode.Authority": null,
                    "PreferredUsage.Scope.Name": "Engineering survey, topographic mapping."
                },
                "id": "osdu:reference-data--CoordinateReferenceSystem:Projected:EPSG::32742"
            }
        ],
        "totalCount": 200
    },
    "query": "(NOT data.InactiveIndicator: true) AND (data.Kind: \"Projected\") AND (data.CoordinateSystem.HorizontalAxisUnitID: \"osdu:reference-data--UnitOfMeasure:m:\") AND (data.BaseCRS.BaseCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326\")"
}
```


## 4.7 Fetch all BoundProjected and Projected CRSs based on WGS 84 that use the unit meter

A common scenario is to find all CRSs that data may be associated with
that needs to be ingested into OSDU. The convention in OSDU is that all
geospatial data is associated with a BoundCRS (Projected or
Geographic2D), such that coordinates can be normalized to WGS 84 using
the transformation that is part of the Bound object. However, if the
data is already in WGS 84 (or a projected CRS based on WGS 84), then the
CRS to associate the data with is not Bound. The Catalog CRS has a
parameter to fetch these entities with a single call. The next section
shows the return for projected CRSs, after that for geographic.

### 4.7.1 Projected

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_
```json
{
"returnBoundProjectedAndProjectedBasedOnWgs84": true,
"horizontalAxisUnitId": "osdu:reference-data--UnitOfMeasure:m:"
}
```


**Response**
```json
...
            },
            {
                "data": {
                    "PreferredUsage.Extent.Description": "Description intersection between CRS and CT 'Kazakhstan, Russian Federation onshore and Uzbekistan - 66°E to 72°E; Kyrgyzstan and Tajikistan - west of 72°E.' (CRS) and 'Armenia; Azerbaijan; Belarus; Estonia - onshore; Georgia - onshore; Kazakhstan; Kyrgyzstan; Latvia - onshore; Lithuania - onshore; Moldova; Russian Federation - onshore; Tajikistan; Turkmenistan; Ukraine - onshore; Uzbekistan.' (CT) [1801,2423]",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "Description": null,
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": 72.0,
                    "Name": "Pulkovo 1942 * OGP-Rus / 6-degree Gauss-Kruger zone 12 [28412,15865]",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 36.67,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": 66.0,
                    "PreferredUsage.Name": "Usage intersection between CRS and CT 'Asia - FSU onshore 66°E to 72°E' (CRS) and 'Europe - FSU onshore' (CT) [6880,11876]",
                    "Kind": "BoundProjected",
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:m:",
                    "PreferredUsage.Extent.AuthorityCode.Authority": null,
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 77.07,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1211,
                    "InactiveIndicator": null,
                    "CodeSpace": "OSDU",
                    "Code": "28412016",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "RevisionDate": "2022-04-04T16:54:46+0000",
                    "Datum.Name": null,
                    "PreferredUsage.Extent.Name": "Extent intersection between CRS and CT 'Asia - FSU onshore 66°E to 72°E' (CRS) and 'Europe - FSU onshore' (CT) [1801,2423]",
                    "CoordinateSystem.Name": "Cartesian 2D CS. Axes: northing, easting (X,Y). Orientations: north, east. UoM: m.",
                    "CoordinateSystem.AuthorityCode.Code": 4530,
                    "PreferredUsage.AuthorityCode.Authority": null,
                    "Datum.AuthorityCode.Authority": null,
                    "PreferredUsage.Scope.Name": "Topographic mapping (medium scale)."
                },
                "id": "osdu:reference-data--CoordinateReferenceSystem:BoundProjected:EPSG::28412_EPSG::15865"
            }
        ],
        "totalCount": 1055
    },
    "query": "(NOT data.InactiveIndicator: true) AND (data.CoordinateSystem.HorizontalAxisUnitID: \"osdu:reference-data--UnitOfMeasure:m:\") AND ((data.Kind: \"BoundProjected\") OR ((data.Kind: \"projected\") AND (data.BaseCRS.AuthorityCode.Code: \"4326\")))"
}
```

### 4.7.2 Geographic

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_

```json
{
    "returnBoundGeographic2DAndWgs84": true
}
```

**Response**

```json
...
            },
            {
                "data": {
                    "PreferredUsage.Extent.Description": "World.",
                    "CoordinateSystem.AuthorityCode.Authority": "EPSG",
                    "Description": null,
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": 180.0,
                    "Name": "WGS 72BE * DMA [4324,1240]",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": -90.0,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": -180.0,
                    "PreferredUsage.Name": "World (from coordinate reference system (same extent as transformation))",
                    "Kind": "BoundGeographic2D",
                    "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:dega:",
                    "PreferredUsage.Extent.AuthorityCode.Code": 1262,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 90.0,
                    "PreferredUsage.Scope.AuthorityCode.Code": 1183,
                    "InactiveIndicator": null,
                    "CodeSpace": "OSDU",
                    "Code": "4324001",
                    "CoordinateSystem.VerticalAxisUnitID": null,
                    "RevisionDate": "2022-04-04T16:54:46+0000",
                    "Datum.Name": null,
                    "PreferredUsage.Extent.Name": "World",
                    "CoordinateSystem.Name": "Ellipsoidal 2D CS. Axes: latitude, longitude. Orientations: north, east. UoM: degree",
                    "CoordinateSystem.AuthorityCode.Code": 6422,
                    "PreferredUsage.AuthorityCode.Authority": null,
                    "Datum.AuthorityCode.Authority": null,
                    "PreferredUsage.Scope.Name": "Horizontal component of 3D system."
                },
                "id": "osdu:reference-data--CoordinateReferenceSystem:BoundGeographic2D:EPSG::4324_EPSG::1240"
            }
        ],
        "totalCount": 253
    },
    "query": "(NOT data.InactiveIndicator: true) AND ((data.Kind: \"BoundGeographic2D\") OR ((data.Kind: \"geographic 2D\") AND (data.Code: \"4326\") AND (data.CodeSpace: \"EPSG\")))"
}
```

*\*Note: This is simply a query for all non-deprecated BoundGeographic2D
CRSs, and additionally WGS 84 (EPSG::4326).*

## 4.8 Less useful parameters (queries)

In the above examples the most common scenario was shown. The API has
additional parameters which are not expected to be used much. The
following sections show the limitations.

### 4.8.1 Find all Vertical CRSs based on datum Mean Sea Level (code 5100)

Note: The optional parameter for "datum" is not useful. It does not
fulfil this use case, at least not with the way the reference data are
currently defined. This is a low priority and not pursued.

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_

```json
{
  "datum": {
    "code": "5100",
    "codeSpace": "EPSG"
  }
}
```

or
```json
{
  "datum": {
    "code": "6267",
    "codeSpace": "EPSG"
  }
}
```

**Response**

```
_This works, but only a single CRS is returned. For the vertical CRS it
would work if there are multiple based on the same datum._
```



### 4.8.2 Retrieve all CRSs with "Texas" in the name

The name parameter was intended to facilitate search in the database on
partial name. In practice this is not a very useful method because
wildcard search is not supported at the start, and only full word search
is supported. It is better to retrieve a full list and then filter in
the application (for interactive applications).

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system_

```json
{
    "kind": "BoundProjected",
    "name": "Texas"
}
```


**Response**

```
Does not work (not useful)

```
_*Note: Also "NAD" or "NAD*" do not work_

### 4.8.3 Find all CRSs with Texas in the extent description

See also comments above. Similar to search for name, search on extent is
not practical because the full word has to be used.

**Request**
{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-reference-system

```json
{
    "extent": {
        "name": "Texas"
    }
}
```

**Response**

```
(Not perceived as useful)

```

## 4.9 Alternatively using the OSDU Search service directly

CRS Catalog service is well suited for requesting CRSs. It easy to use and guarantees to return all records with a single call, excludes by default vertical CRSs, excludes by default deprecated CRSs, excludes by default most properties that are not of interest, and of course is simpler to use in case complex things are needed like CRS with a specific unit. However, if a more complex query is necessery, the OSDU standard search service can be used to achieve the same. The query syntax is slightly
more complicated for some tasks, but the developer has full control of
the returned fields and can build a complex query to extract only the information needed. 

-   However, the CRS Catalog Service filters out deprecated entities and one using the Search will have to do that in a query on on the returned results.  
-   Also note that the CRS Catalog Services returns all CRS records and someone using Search may have to paginate to ensure that.

In the examples below a specific list is returned of (Bound) projected
CRSs using two calls. After the user selects a specific CRS, the record
id can be used to convert coordinates.

From the query it will be clear how it is possible to
fetch the CRS records from the OSDU reference data, and then on the client
side perform the filter on the unit.

-   An issue found in R3.M13 with the below query is that if "m" is changed to \"ft\"
    then the response includes \"ft\[US\]\". This has been logged.

-   it seems UOM are in form url encoded for square brackets like
    ..."UnitOfMeasure:ft%5BUS%5D:\".

### 4.9.1 Find all BoundProjected CRSs with a Horizontal unit of meters

**Request**  https://{{SEARCH_HOST}}/query

```json
{
    "kind": "osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0",
    "query": "data.Kind: BoundProjected AND data.CoordinateSystem.HorizontalAxisUnitID: \"osdu:reference-data--UnitOfMeasure:m:\"",
    "limit": 1,
    "returnedFields": ["id", "data.Name", "data.SourceCRS", "data.Transformation", "data.BaseCRS.BaseCRSID", "data.CoordinateSystem.HorizontalAxisUnitID", "data.PersistableReference"]
}

```


**Response**

```json
{
    "results": [
        ...
        {
            "data": {
                "SourceCRS.AuthorityCode.Authority": "EPSG",
                "PersistableReference": "{\"authCode\":{\"auth\":\"OSDU\",\"code\":\"23028006\"},\"lateBoundCRS\":{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"23028\"},\"name\":\"ED_1950_UTM_Zone_28N\",\"type\":\"LBC\",\"ver\":\"PE_10_9_1\",\"wkt\":\"PROJCS[\\\"ED_1950_UTM_Zone_28N\\\",GEOGCS[\\\"GCS_European_1950\\\",DATUM[\\\"D_European_1950\\\",SPHEROID[\\\"International_1924\\\",6378388.0,297.0]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Transverse_Mercator\\\"],PARAMETER[\\\"False_Easting\\\",500000.0],PARAMETER[\\\"False_Northing\\\",0.0],PARAMETER[\\\"Central_Meridian\\\",-15.0],PARAMETER[\\\"Scale_Factor\\\",0.9996],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0],AUTHORITY[\\\"EPSG\\\",23028]]\"},\"name\":\"ED50 * DMA-Irl Gbr / UTM zone 28N [23028,1138]\",\"singleCT\":{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"1138\"},\"name\":\"ED_1950_To_WGS_1984_6\",\"type\":\"ST\",\"ver\":\"PE_10_9_1\",\"wkt\":\"GEOGTRAN[\\\"ED_1950_To_WGS_1984_6\\\",GEOGCS[\\\"GCS_European_1950\\\",DATUM[\\\"D_European_1950\\\",SPHEROID[\\\"International_1924\\\",6378388.0,297.0]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],METHOD[\\\"Geocentric_Translation\\\"],PARAMETER[\\\"X_Axis_Translation\\\",-86.0],PARAMETER[\\\"Y_Axis_Translation\\\",-96.0],PARAMETER[\\\"Z_Axis_Translation\\\",-120.0],OPERATIONACCURACY[6.0],AUTHORITY[\\\"EPSG\\\",1138]]\"},\"type\":\"EBC\",\"ver\":\"PE_10_9_1\"}",
                "SourceCRS.Name": "ED50 / UTM zone 28N",
                "Transformation.AuthorityCode.Code": 1138,
                "SourceCRS.SourceCRSID": "osdu:reference-data--CoordinateReferenceSystem:Projected:EPSG::23028:",
                "CoordinateSystem.HorizontalAxisUnitID": "osdu:reference-data--UnitOfMeasure:m:",
                "Transformation.TransformationID": "osdu:reference-data--CoordinateTransformation:EPSG::1138:",
                "Transformation.AuthorityCode.Authority": "EPSG",
                "Transformation.Name": "ED50 to WGS 84 (6)",
                "BaseCRS.BaseCRSID": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4230:",
                "SourceCRS.AuthorityCode.Code": 23028,
                "Name": "ED50 * DMA-Irl Gbr / UTM zone 28N [23028,1138]"
            },
            "id": "osdu:reference-data--CoordinateReferenceSystem:BoundProjected:EPSG::23028_EPSG::1138"
        }
    ],
    "aggregations": null,
    "totalCount": 855
}
```


### 4.9.2 Find all Projected CRSs based on WGS 84 with a Horizontal unit of meters

**Request** _{{osduonaws_base_url}}/api/search/v2/query_

```json
{
    "kind": "osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0",
    "query": "data.Kind: Projected AND data.BaseCRS.AuthorityCode.Code: 4326 AND data.CoordinateSystem.HorizontalAxisUnitID: \"reference-data--UnitOfMeasure:m:\"",
    "limit": 1,
    "returnedFields": ["id", "data.Name", "data.BaseCRS.BaseCRSID", "data.CoordinateSystem.HorizontalAxisUnitID", "data.PersistableReference"]
}
```


**Response**

```json
{
    "results": [
        ...
        {
            "data": {
                "PersistableReference": "{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"6081\"},\"name\":\"WGS_1984_EPSG_Arctic_zone_4-30\",\"type\":\"LBC\",\"ver\":\"PE_10_9_1\",\"wkt\":\"PROJCS[\\\"WGS_1984_EPSG_Arctic_zone_4-30\\\",GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Lambert_Conformal_Conic\\\"],PARAMETER[\\\"False_Easting\\\",30500000.0],PARAMETER[\\\"False_Northing\\\",4500000.0],PARAMETER[\\\"Central_Meridian\\\",58.0],PARAMETER[\\\"Standard_Parallel_1\\\",73.66666666666669],PARAMETER[\\\"Standard_Parallel_2\\\",77.0],PARAMETER[\\\"Latitude_Of_Origin\\\",75.36440330555556],UNIT[\\\"Meter\\\",1.0],AUTHORITY[\\\"EPSG\\\",6081]]\"}",
                "CoordinateSystem.HorizontalAxisUnitID": "{{NAMESPACE}}:reference-data--UnitOfMeasure:m:",
                "BaseCRS.BaseCRSID": "{{NAMESPACE}}:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326:",
                "Name": "WGS 84 / EPSG Arctic zone 4-30"
            },
            "id": "osdu:reference-data--CoordinateReferenceSystem:Projected:EPSG::6081"
        }
    ],
    "aggregations": null,
    "totalCount": 200
}
```


# 5. Getting a list of CTs

*User story: As a user, I would like to select from available
transformations, in particular to execute a direct transformation
between two datums, not through the hub CRS (WGS 84).*

## 5.1 Context

-   A CT is a coordinate operation that enacts a change of datum, i.e.,
    the sourceCRS and targetCRS have a different base geographic2D CRS
    in the horizontal case.
-   The following types of CT are stored in OSDU reference data:
    - *SingleCT*
    - *ConcatenatedCT*
    - *VerticalCT (#// vertical transforms are not supported by the conversion engine)*
-   Point motion operation CTs are not supported in OSDU (for dynamic
    CRS, accounting for the effect of plate motion on coordinates).
-   The OSDU supports and assumes BoundCRSs to be associated with
    ingested data. This is used to normalize to WGS 84.
-   Vertical CTs may be populated in OSDU (e.g., \"Alicante height to
    EVRF2000 height (1)\", EPSG::5249) but note that vertical
    transformations cannot be executed by the engine. Hence ingested
    data are not normalized in the vertical to a common datum, but
    assumed to be MSL heights.
-   All CTs in OSDU are considered to be reversible (as in the meaning
    defined in ISO 19111). One should not store non-reversible methods
    in OSDU.
-   OSDU APIs currently do not support direct transformations. As such
    retrieving a list of CTs currently has limited purpose in the OSDU,
    except to support late-binding applications on the platform. However
    also in that case it is possible such application would retrieve a
    list of all BoundCRSs and extract the associated transformations
    from those objects.

### 5.1.1 Parameters for the POST coordinate-transformation endpoint

-   The list of input parameters is given in the online api spec [Swagger documentation](https://community.opengroup.org/osdu/platform/system/reference/crs-catalog-service/-/tree/master/docs/api_spec). The main parameters of importance are:

    - **kind**: "Transformation", "ConcatenatedOperation", "VerticalTransformation", "ExcludeVertical" (default) or "All". "All" is a special value which indicates returning records of all kinds. By default only horizontal transformations are returned (because
    vertical transformations are typically not used)
    - **sourceCRS, targetCRS**: As SourceCRS and TargetCRS are interchangeable, the service will search for transformations between the given systems.
    - **latitude, longitude**: Only return records valid at that location.
    - **returnAllFields**: A flag to return all fields (by default a subset is returned).    


## 5.2 Find all (horizontal) CTs valid at a specific location

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-transformation_
```json
{
    "kind": ExcludeVertical,
    "latitude": 30.0,
    "longitude": -90
}
```
*\*Note: "kind" defaults to "ExcludeVertical" but shown here for clarity.*

**Response**
```json
...
            },
            {
                "data": {
                    "PreferredUsage.Extent.Description": "United States (USA) - CONUS including EEZ - onshore and offshore - Alabama; Arizona; Arkansas; California; Colorado; Connecticut; Delaware; Florida; Georgia; Idaho; Illinois; Indiana; Iowa; Kansas; Kentucky; Louisiana; Maine; Maryland; Massachusetts; Michigan; Minnesota; Mississippi; Missouri; Montana; Nebraska; Nevada; New Hampshire; New Jersey; New Mexico; New York; North Carolina; North Dakota; Ohio; Oklahoma; Oregon; Pennsylvania; Rhode Island; South Carolina; South Dakota; Tennessee; Texas; Utah; Vermont; Virginia; Washington; West Virginia; Wisconsin; Wyoming. US Gulf of Mexico (GoM) OCS.",
                    "TargetCRS.Name": "WGS 84",
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": -65.69,
                    "SourceCRS.SourceCRSID": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4267:",
                    "CoordinateTransformationType": "Transformation",
                    "PreferredUsage.AuthorityCode.Code": 11862,
                    "TargetCRS.TargetCRSID": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326:",
                    "Name": "NAD27 to WGS 84 (79)",
                    "SourceCRS.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 23.81,
                    "Accuracy": 5.0,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": -129.16,
                    "PreferredUsage.Name": "USA - CONUS including EEZ",
                    "Kind": "Transformation",
                    "PreferredUsage.Extent.AuthorityCode.Code": 2374,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 49.38,
                    "Method.AuthorityCode.Authority": "EPSG",
                    "Method.Name": "NADCON",
                    "PreferredUsage.Scope.AuthorityCode.Code": 1252,
                    "InactiveIndicator": null,
                    "CodeSpace": "EPSG",
                    "Code": "15851",
                    "TargetCRS.AuthorityCode.Code": 4326,
                    "SourceCRS.AuthorityCode.Code": 4267,
                    "Method.AuthorityCode.Code": 9613,
                    "SourceCRS.Name": "NAD27",
                    "PreferredUsage.Extent.Name": "USA - CONUS including EEZ",
                    "TargetCRS.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Scope.Name": "(null/copy) Approximation for medium and low accuracy applications assuming equality between plate-fixed static and earth-fixed dynamic CRSs, ignoring static/dynamic CRS differences."
                },
                "id": "osdu:reference-data--CoordinateTransformation:EPSG::15851"
            }
        ],
        "totalCount": 14
    },
    "query": "(NOT data.InactiveIndicator: true) AND (NOT data.Kind: \"VerticalTransformation\")"
}
```


## 5.3 Find all CTs between two (horizontal) CRSs

By defining a sourceCRS and a targetCRS, it's possible to find all CTs between those two CRSs. To find the available transformations between for example Kousseri (epsg:4198) and WGS 72BE(4324), you can do 

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-transformation_
```json
{
    "sourceCRS": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4198:",
    "targetCRS": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4324:"
}
```

*\*Note: Operations in OSDU are (all) reversible but stored only once.
Hence, to find CTs between CRS A and CRS B, two queries would be
required, i.e., first to find all from A to B and second all from B to
A. The complex OR query constructed in the CRS Catalog Helper service
does this switch automatically.*

**Response**
```json
{
    "searchResults": {
        "results": [
            {
                "data": {
                    "PreferredUsage.Extent.Description": "Cameroon - N'Djamena area.",
                    "TargetCRS.Name": "WGS 72BE",
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": 15.09,
                    "SourceCRS.SourceCRSID": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4198:",
                    "CoordinateTransformationType": "Transformation",
                    "PreferredUsage.AuthorityCode.Code": 8727,
                    "TargetCRS.TargetCRSID": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4324:",
                    "Name": "Kousseri to WGS 72BE (1)",
                    "SourceCRS.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 11.7,
                    "Accuracy": 5.0,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": 14.17,
                    "PreferredUsage.Name": "Cameroon - N'Djamena area",
                    "Kind": "Transformation",
                    "PreferredUsage.Extent.AuthorityCode.Code": 2591,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 12.77,
                    "Method.AuthorityCode.Authority": "EPSG",
                    "Method.Name": "Geocentric translations (geog2D domain)",
                    "PreferredUsage.Scope.AuthorityCode.Code": 1216,
                    "InactiveIndicator": null,
                    "CodeSpace": "EPSG",
                    "Code": "1806",
                    "TargetCRS.AuthorityCode.Code": 4324,
                    "SourceCRS.AuthorityCode.Code": 4198,
                    "Method.AuthorityCode.Code": 9603,
                    "SourceCRS.Name": "Kousseri",
                    "PreferredUsage.Extent.Name": "Cameroon - N'Djamena area",
                    "TargetCRS.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Scope.Name": "Oil and gas exploration."
                },
                "id": "osdu:reference-data--CoordinateTransformation:EPSG::1806"
            }
        ],
        "totalCount": 1
    },
    "query": "(NOT data.InactiveIndicator: true) AND (NOT data.Kind: \"VerticalTransformation\") AND ((data.SourceCRS.SourceCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4198:\") OR (data.TargetCRS.TargetCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4198:\")) AND ((data.SourceCRS.SourceCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4324:\") OR (data.TargetCRS.TargetCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4324:\"))"
}
```


## 5.4 Find all vertical CTs
To list all vertical tranformations do:

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/coordinate-transformation_
```json
{
    "kind": "VerticalTransformation"
}
```

**Response**
```json
{
    "searchResults": {
        "results": [
            {
                "data": {
                    "PreferredUsage.Extent.Description": "Gibraltar - onshore; Spain - mainland onshore.",
                    "TargetCRS.Name": "EVRF2000 height",
                    "PreferredUsage.Extent.BoundingBoxEastBoundLongitude": 3.39,
                    "SourceCRS.SourceCRSID": "osdu:reference-data--CoordinateReferenceSystem:Vertical:EPSG::5782:",
                    "CoordinateTransformationType": "Transformation",
                    "PreferredUsage.AuthorityCode.Code": 9397,
                    "TargetCRS.TargetCRSID": "osdu:reference-data--CoordinateReferenceSystem:Vertical:EPSG::5730:",
                    "Name": "Alicante height to EVRF2000 height (1)",
                    "SourceCRS.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Scope.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxSouthBoundLatitude": 35.95,
                    "Accuracy": 0.1,
                    "PreferredUsage.Extent.BoundingBoxWestBoundLongitude": -9.37,
                    "PreferredUsage.Name": "Spain and Gibraltar - onshore",
                    "Kind": "VerticalTransformation",
                    "PreferredUsage.Extent.AuthorityCode.Code": 4188,
                    "PreferredUsage.Extent.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Extent.BoundingBoxNorthBoundLatitude": 43.82,
                    "Method.AuthorityCode.Authority": "EPSG",
                    "Method.Name": "Vertical Offset and Slope",
                    "PreferredUsage.Scope.AuthorityCode.Code": 1059,
                    "InactiveIndicator": null,
                    "CodeSpace": "EPSG",
                    "Code": "5429",
                    "TargetCRS.AuthorityCode.Code": 5730,
                    "SourceCRS.AuthorityCode.Code": 5782,
                    "Method.AuthorityCode.Code": 1046,
                    "SourceCRS.Name": "Alicante height",
                    "PreferredUsage.Extent.Name": "Spain and Gibraltar - onshore",
                    "TargetCRS.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.AuthorityCode.Authority": "EPSG",
                    "PreferredUsage.Scope.Name": "Change of height to a different vertical reference surface."
                },
                "id": "osdu:reference-data--CoordinateTransformation:EPSG::5429"
            }
        ],
        "totalCount": 1
    },
    "query": "(NOT data.InactiveIndicator: true) AND (data.Kind: \"VerticalTransformation\")"
}
```


## 5.5 Find all deprecated CTs (or CRSs)

Deprecated CRSs (or CTs) in OSDU are indicated by
"data.InactiveIndicator": true. By default OSDU should not contain
deprecated entities, and users should in general not be interested in
deprecated CRSs (because EPSG deprecates systems only if there is
something serious in error). Therefore by default depreciated entities
are not returned by the CRS Catalog service.

**Request** _{{osduonaws_base_url}}/api/search/v2/query_

```json
{
    "includeDeprecated": "True"
}
```


# 6. Check if a point is inside of the area of use of a CRS or CT

*User story: As a user, I would like to know if the coordinates are
outside AOU so that it will help to identify if the user enters a typo
(or random value) for coordinates. For example, it will help to
recognize if projected coordinates are swapped (X vs Y) and to detect if
geographic coordinates are used with a projected CRS (because typically
small numbers close to (0,0) are not valid projected coordinates). I
would like to know if my coordinates are "close" to the AOU or "far"
outside because that helps determining usage of an entity slightly
outside its normal area, which is not uncommon (e.g., to work a project
in a UTM zone, and the project area goes somewhat outside the normal
area of use).*

## 6.1 Context

-   All CRS and CTs in the EPSG data model have a Usage (or multiple
    Usages), which is an intersection of a Scope and Extent
    (previously named "Area of Use" in ISO 19111).  An Extent has a name,
    description, BBOX and associated polygon, see also the Figure below.
    The BBOX is rectangular in geographic space (WGS 84).

-   To deal with possibly multiple usages for a single entity, OSDU has introduced a PreferredUsage. This is the one to use and not the list
    of one or more Usages. (Note: For BoundCRSs reference data in OSDU the extent is the 
    intersection of the PreferredUsages of CRS and CT (to make it the
    smallest applicable area of use where the BoundCRS is valid for both
    its CRS and CT components.

-   OSDU only carries the BBOX (not the polygon). Note: The Usage extent
    polygon can be used to see if a CRS is applicable at a certain
    location or for a certain dataset, but it is most powerful for
    resolving CT multiplicity between datums, i.e., determining which
    transformation is suitable for a certain geographic area.  This does
    not work well with BBOXs (e.g., the Mexican BBOX and the USA BBOX
    overlap in the US GOM, whereas the polygons do not).

-   OSDU has no plans to add polygons to the reference data. Implementation would not be
    straightforward to use when stored as reference data (this is a consequence of the design decision of
    storing the explicit definition as PersistableReference in the
    database). For polygons to work efficiently they would have to be
    binary stored somehow with GIS functionality to perform efficient
    intersections with data. Nonetheless, the BBOX have some power to avoid
    gross errors associating data with incorrect CRS or CT.

-   Entity Extent BBOXs are stored in WGS 84 coordinates using 2 decimal
    places (\~100 m resolution).  It is not required to
    convert the geographic coordinates to WGS 84 before comparison, because
    the biggest location difference between various geographic datums is
    in the order of 1 km and that is within the resolution we generally are
    interested in.  However, note that this fails if the unconverted
    lat,lon are in grads instead of degrees, as well as in case the base
    geographic CRS does not use Greenwich as its prime meridian (both
    cases are not common and the client is to decide how to handle this,
    e.g., to transform coordinates to WGS 84 before making the request;
    a drawback of doing this could be if a grid file method is used that
    is not appropriate for the location and leads to an exception that
    needs to be handled).

-   This endpoint is intended for application developers to validate if a
    given coordinate is in the Extent of the associated CRS and/or CT.
    If projected coordinates are given as input they should be converted
    to geographic by the client.  The client is in general not required
    to transform input coordinates to WGS 84 (as explained above),
    though could do this of course (particularly if they are associated
    with a BoundCRS).  Data is sometimes stored by Operators with a CRS
    or CT that is not normally used at the data location. This is
    typically done to keep working in the same single CRS in a project,
    also slightly outside of its normal area of use (or sometimes
    entities are defined strictly for onshore usage while Operator also
    utilizes it offshore).

This endpoint is in a sense the "inverse" of returning a list of
entities based on a given lat,lon. That endpoint could be used
potentially to find out if the given "id" is in the list but this is not
the recommended implementation. Perhaps some geospatial query can be
used, but proposed here is simply a comparison of the given location vs.
the retrieved BBOX for the "id".

![Example extent for Cuba](CrsCatalog-Fig1-aou.png 'Example extent for Cuba')

*Figure 1: Example EPSG Extent polygon and Bounding Box for CT SIRGAS 2000 to WGS 84 (1) [EPSG::15894].*

.


| **X1: inside polygon**    | ** X2: Outside polygon, Inside BBOX** | ** X3: Outside BBOX (and polygon)**    |
| --------------- | ----------------------- | ---------------------- |
| Lat "Y": 1      | Lat "Y": -50            | Lat "Y": -50           |
| Lon "X": -75    | Lon "X": -30            | Lon "X": -20           |
|                 |                         |                        |
| Distance : 0    | Distance : 0 (it is too hard to properly compute the distance to the polygon) | Distance : <XX> (computed per below to edge of BBOX) |



## 6.2 Examples

_NOTE 2023-09-09: As tested on AWS M16 the record id does not have a terminating colon in these requests, while the OSDU standard is that is required when referencing by a record id. Currently there is an issue open to accept either with or without the terminating colon._

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/points-in-aou_
```json
{
"recordId": "osdu:reference-data--CoordinateTransformation:EPSG::15851",
    "points": [
        {
            "latitude": 40,
            "longitude": -105
        },
        {
            "latitude": 25,
            "longitude": -90
        },
        {
            "latitude": 55,
            "longitude": -100
        }
    ]
}
```

**Response**
```json
{
"bboxFailedPoints": [
    {
    "point": {
        "latitude": 55.0,
        "longitude": -100.0
        },
    "index": 2,
    "approximateKmDistanceOutside": 618
    }
    ],
    "maxDistKmOutsideBBox": 618
}
```




<details><summary>Payload example 2 (click to expand)</summary>

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/points-in-aou_
```json
{
"recordId": "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4204",
    "points": [
        {
            "latitude": 16,
            "longitude": 34
        },
        {
            "latitude": 16.5,
            "longitude": 34.1
        },
        {
            "latitude": 16.5,
            "longitude": 35.8
        },
        {
            "latitude": 80.1,
            "longitude": 90
        }
    ]
}
```

**Response**
```json
{
"bboxFailedPoints": [
{
"point": {
"latitude": 16.0,
"longitude": 34.0
},
"index": 0,
"approximateKmDistanceOutside": 68
},
{
"point": {
"latitude": 16.5,
"longitude": 34.1
},
"index": 1,
"approximateKmDistanceOutside": 43
},
{
"point": {
"latitude": 80.1,
"longitude": 90.0
},
"index": 3,
"approximateKmDistanceOutside": 5313
}
],
"maxDistKmOutsideBBox": 5313
}
```
</details>


<details><summary>Payload example 3 (click to expand)</summary>

**Request** _{{osduonaws_base_url}}/api/crs/catalog/v3/points-in-aou_
```json
{
"recordId": "osdu:reference-data--CoordinateReferenceSystem:BoundProjected:EPSG::32065_EPSG::15851",
    "points": [
        {
            "latitude": 35,
            "longitude": -93
        },
        {
            "latitude": 25,
            "longitude": 90
        },
        {
            "latitude": 25,
            "longitude": -90
        },
        {
            "latitude": 45,
            "longitude": -92
        }
    ]
}
```

**Response**
```json
{
    "bboxFailedPoints": [
    {
    "point": {
        "latitude": 25.0,
        "longitude": 90.0
    },
    "index": 1,
    "approximateKmDistanceOutside": 17346
    },
    {
    "point": {
        "latitude": 25.0,
        "longitude": -90.0
    },
    "index": 2,
    "approximateKmDistanceOutside": 67
    }
    ],
    "maxDistKmOutsideBBox": 17346
}
```
</details>


## 6.3 Recommended usage by caller

The recommended distance criterion to be used in the caller is 150 km
(~1.5 degree).  The reason for that particular distance is that
typically one would not use a CRS or CT farther out, even if the CRS or
CT is strictly only valid onshore but used offshore.  A criterion of 3
degrees (half a UTM zone, or about 300 km) could be used, but a
disadvantage of that choice is that small easting coordinates (close to
0) and negative easting coordinates would be allowed, while we\'d want
to detect if for example geographic coordinates (small numbers) are used
by accident with a projected CRS.  It is very unlikely (though not
impossible) that all coordinates are completely outside the bbox by more
than 150 km.

An additional criterion of 1000 km can be used in an application, where
a notification is given when the point is up to 150 km outside of the
bbox (yellow traffic light), and a stronger warning is generated if the
location is within 150-1000 km (e.g., ask "are you sure?"), and a
critical error (red traffic light) is given when it is more than 1000 km
outside of the bounding box (preventing loading of data).

![Example extent for Cuba](CrsCatalog-Fig2-aou.jpg 'Example extent for Cuba')

*Figure 2: Example of the polygon and bounding box for the NAD27 / Cuba
grid.  This CRS is officially only used onshore. But suppose that we
have a seismic line with coordinates that are offshore (all outside of
area of use as defined by the multipart polygon), both inside and
outside of the bounding box.  This function computes how far
(approximately) the farthest point is outside of the bbox.*
