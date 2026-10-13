// Copyright © Amazon
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http:#www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.crs.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.core.common.model.search.SpatialFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
@Schema(description = "Body for searching on CRSs")
public class CoordinateReferenceSystemsQuery implements ISearchQuery {
    @Schema(description = "Corresponds to CodeSpace field on CRS records", type = "string")
    private String codeSpace;
    @Schema(description = "Corresponds to Code field on CRS records", type = "string")
    private String code;
    @Schema(description = "Corresponds to Code field on CRS records", type = "string")
    private String name;
    @Schema(description = "Corresponds to ID field on CRS records", type = "string")
    private String id;
    @Schema(description = "Corresponds to Kind field on CRS records", type = "string")
    private String kind;
    @Schema(description = "Type of CRS, e.g., BoundCRS, ProjectedCRS, GeodeticCRS, VerticalCRS", type = "string")
    private String coordinateReferenceSystemType;
    @Schema(description = "Whether or not to only return bound projected type or projected type based on wgs84. If true, it only returns CRS 1. with Kind as \"BoundProjected\" or 2. with Kind as \"projected\" and BaseCRS.AuthorityCode.Code as 4326", type = "boolean")
    private boolean returnBoundProjectedAndProjectedBasedOnWgs84;
    @Schema(description ="Whether or not to only return bound geopraphic 2d type. If true, it only returns CRS 1. with Kind as \"BoundGeographic2D\" or 2. with Kind as \"geographic 2D\" and Code as 4326 and CodeSpace as \"EPSG\" " , type = "boolean")
    private boolean returnBoundGeographic2DAndWgs84;
    @Schema(description ="BaseCRS")
    private BaseCRS baseCRS;
    @Schema(description ="Datum")
    private Datum datum;
    @Schema(description ="Description of extent. Corresponds to PreferredUsage.Extent.Description in the record. All entities with PreferredUsage.Extent.Description containing the search string will be returned. Wildcard is not supported.", type = "string")
    private Extent extent;
    @Schema(description = "Corresponds to PersistableReference field on CRS records. All entities with PersistableReference containing the search string will be returned.", type = "string")
    private String persistableReferenceSearch;
    @Schema(description = "Corresponds to CoordinateSystem.HorizontalAxisUnitID field on CRS records, e.g., \"osdu:reference-data--UnitOfMeasure:dega:\".", type = "string")
    private String horizontalAxisUnitId;
    @Schema(description = "Corresponds to CoordinateSystem.VerticalAxisUnitID field on CRS records", type = "string")
    private String verticalAxisUnitId;
    @Schema(description ="Latitude point for searching on CRS bounding box", type = "number")
    private Double latitude;
    @Schema(description ="Longitude point for searching on CRS bounding box", type = "number")
    private Double longitude;
    @Schema(description ="Whether to return deprecated records marked with InactiveIndicator=true. Default is false", type = "boolean")
    private Boolean includeDeprecated = false;
    @Schema(description ="Corresponds to offset on search service", type = "integer")
    private Integer offset;
    @Schema(description ="Corresponds to limit on search service. Default is to return all found entities.", type = "integer")
    private Integer limit = 10000;
    @Schema(description ="Whether to return all fields in the record. Default is false and only a subset is returned", type = "boolean")
    private Boolean returnAllFields = false;
    @Schema(description ="Return all fields in the record")
    private List<String> returnedFields;

    public String constructQuery() {
        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        if (!includeDeprecated) {
            queryBuilder.addQuery("NOT data.InactiveIndicator: true");
        }
        queryBuilder.addPhraseQuery("data.CodeSpace", codeSpace);
        queryBuilder.addPhraseQuery("data.Code", code);
        queryBuilder.addPhraseQuery("data.Name", name);
        queryBuilder.addPhraseQuery("data.ID", id);
        queryBuilder.addPhraseQuery("data.Kind", kind);
        queryBuilder.addPhraseQuery("data.CoordinateReferenceSystemType", coordinateReferenceSystemType);
        queryBuilder.addPhraseQuery("data.CoordinateSystem.HorizontalAxisUnitID", horizontalAxisUnitId);
        queryBuilder.addPhraseQuery("data.CoordinateSystem.VerticalAxisUnitID", verticalAxisUnitId);

        if (baseCRS != null) {
            queryBuilder.addPhraseQuery("data.BaseCRS.BaseCRSID", baseCRS.getId());
            queryBuilder.addPhraseQuery("data.BaseCRS.Name", baseCRS.getName());
        }

        if (datum != null) {
            queryBuilder.addPhraseQuery("data.Datum.Name", datum.getName());
            queryBuilder.addPhraseQuery("data.Datum.AuthorityCode.Authority", datum.getCodeSpace());
            queryBuilder.addPhraseQuery("data.Datum.AuthorityCode.Code", datum.getCode());
        }

        if (extent != null) {
            queryBuilder.addPhraseQuery("data.PreferredUsage.Extent.Description", extent.getDescription());
        }

        if (returnBoundProjectedAndProjectedBasedOnWgs84) {
            queryBuilder.addQuery(
                    SearchQueryBuilder.connectMultipleQueries("OR",
                            SearchQueryBuilder.buildPhraseQuery("data.Kind", "BoundProjected"),
                            SearchQueryBuilder.connectMultipleQueries("AND",
                                    SearchQueryBuilder.buildPhraseQuery("data.Kind", "projected"),
                                    SearchQueryBuilder.buildPhraseQuery("data.BaseCRS.AuthorityCode.Code", "4326"))));
        }

        if (returnBoundGeographic2DAndWgs84) {
            queryBuilder.addQuery(
                    SearchQueryBuilder.connectMultipleQueries("OR",
                            SearchQueryBuilder.buildPhraseQuery("data.Kind", "BoundGeographic2D"),
                            SearchQueryBuilder.connectMultipleQueries("AND",
                                    SearchQueryBuilder.buildPhraseQuery("data.Kind", "geographic 2D"),
                                    SearchQueryBuilder.buildPhraseQuery("data.Code", "4326"),
                                    SearchQueryBuilder.buildPhraseQuery("data.CodeSpace", "EPSG"))));
        }

        return queryBuilder.connectQuery("AND");
    }

    public SpatialFilter constructSpatialFilter() {
        return SpatialQueryBuilder.buildSpatialQuery(latitude, longitude);
    }

    public List<String> getReturnedFields() {
        if (!returnAllFields) {
            return Arrays.asList(
                    "id", "data.Name", "data.Code", "data.InactiveIndicator", "data.Kind", "data.CodeSpace",
                    "data.Datum", "data.CoordinateSystem", "data.PreferredUsage", "data.Description",
                    "data.RevisionDate");
        }
        return new ArrayList<>();
    }
}