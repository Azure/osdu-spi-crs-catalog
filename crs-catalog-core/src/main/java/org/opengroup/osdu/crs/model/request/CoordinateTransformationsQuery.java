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
@Schema(description = "Body for searching on CTs")
public class CoordinateTransformationsQuery implements ISearchQuery {
    @Schema(description = "Corresponds to CodeSpace field on CT records", type = "string")
    private String codeSpace;
    @Schema(description = "Corresponds to Name field on CT records. All entities with name containing the search string will be returned. Wildcard is not supported", type = "string")
    private String name;
    @Schema(description = "Corresponds to ID field on CT records", type = "string")
    private String id;
    @Schema(description = "Corresponds to Code field on CT records", type = "string")
    private String code;
    @Schema(description = "Corresponds to the Kind field on CT records. Default is to not return CT records with Kind \"VerticalTransformation\". Viable options include \"Transformation\", \"ConcatenatedOperation\", \"VerticalTransformation\", \"ExcludeVertical\" (default) and \"All\". \"All\" is a special value which indicates returning records of all kinds.", type = "string")
    private String kind = "ExcludeVertical";
    @Schema(description = "Corresponds to SourceCRS.SourceCRSID (record id), e.g., \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4198:\". As SourceCRS and TargetCRS are interchangeable, it will also search TargetCRS.TargetCRSID", type = "string")
    private String sourceCRS;
    @Schema(description = "Corresponds to TargetCRS.TargetCRSID (record id), e.g., \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4198:\". As SourceCRS and TargetCRS are interchangeable, it will also search SourceCRS.SourceCRSID", type = "string")
    private String targetCRS;
    @Schema(description = "Returns all entities for which the (Latitude, Longitude) is inside the Extent bounding box. Must also provide longitude", type = "number")
    private Double latitude;
    @Schema(description = "See latitude", type = "number")
    private Double longitude;
    @Schema(description = "Whether to return deprecated records marked with InactiveIndicator=true. Default is false.", type = "boolean")
    private Boolean includeDeprecated = false;
    @Schema(description = "Corresponds to offset on search service", type = "integer")
    private Integer offset;
    @Schema(description = "Corresponds to limit on search service. Default is to return all found entities.", type = "integer")
    private Integer limit = 10000;
    @Schema(description = "Whether to return all fields in the record. Default is false and only a subset is returned", type = "boolean")
    private Boolean returnAllFields = false;
    @Schema(description = "List of specific fields to return. Overridden when returnAllFields is true.")
    List<String> returnedFields = Arrays.asList(
            "id", "data.Name", "data.Code", "data.InactiveIndicator", "data.Kind", "data.Accuracy",
            "data.Method", "data.CodeSpace", "data.CoordinateTransformationType", "data.SourceCRS",
            "data.TargetCRS", "data.PreferredUsage"
    );

    public String constructQuery() {
        SearchQueryBuilder queryBuilder = new SearchQueryBuilder();
        if (!includeDeprecated) {
            queryBuilder.addQuery("NOT data.InactiveIndicator: true");
        }
        switch (kind.toLowerCase()) {
            case "excludevertical":
                queryBuilder.addNegativePhraseQuery("data.Kind", "VerticalTransformation");
                break;
            case "all":
                break;
            default:
                queryBuilder.addPhraseQuery("data.Kind", kind);
        }
        queryBuilder.addPhraseQuery("data.CodeSpace", codeSpace);
        queryBuilder.addPhraseQuery("data.Name", name);
        queryBuilder.addPhraseQuery("data.ID", id);
        queryBuilder.addPhraseQuery("data.Code", code);
        queryBuilder.addMultiFieldPhraseQuery(sourceCRS, "data.SourceCRS.SourceCRSID", "data.TargetCRS.TargetCRSID");
        queryBuilder.addMultiFieldPhraseQuery(targetCRS, "data.SourceCRS.SourceCRSID", "data.TargetCRS.TargetCRSID");
        return queryBuilder.connectQuery("AND");
    }

    public SpatialFilter constructSpatialFilter() {
        return SpatialQueryBuilder.buildSpatialQuery(latitude, longitude);
    }

    public List<String> getReturnedFields() {
        if (!returnAllFields) {
            return Arrays.asList(
                    "id", "data.Name", "data.Code", "data.InactiveIndicator", "data.Kind", "data.Accuracy",
                    "data.Method", "data.CodeSpace", "data.CoordinateTransformationType", "data.SourceCRS",
                    "data.TargetCRS", "data.PreferredUsage"
            );
        }
        return new ArrayList<>();
    }
}
