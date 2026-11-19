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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import tools.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.opengroup.osdu.core.common.model.search.Point;
import org.opengroup.osdu.core.common.model.search.SpatialFilter;
import org.opengroup.osdu.crs.config.NullableStringDeserializer;
import org.opengroup.osdu.crs.config.RequestPointDeserializer;
import org.opengroup.osdu.crs.config.StrictIntegerDeserializer;
import org.springframework.util.StringUtils;

@Data
@Schema(description = "Coordinate Transformations")
public class InPolygonQuery implements ISearchQuery {
	@Schema(description = "Record of CRS or CT to check points against", type = "string")
	@JsonSetter(nulls = Nulls.FAIL)
	@JsonDeserialize(using = NullableStringDeserializer.class)
	private String recordId;
	@Schema(description = "Searches on id for CRS records", type = "string")
	@JsonSetter(nulls = Nulls.FAIL)
	@JsonDeserialize(using = NullableStringDeserializer.class)
	private String dataId;
	@Schema(description = "List of points to check against CRS or CT bounding boxes")
	@JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
	@NotEmpty
	@JsonDeserialize(contentUsing = RequestPointDeserializer.class)
	private List<Point> points = new ArrayList<>();
	@Schema(description = "Corresponds to offset on search service")
	@JsonSetter(nulls = Nulls.FAIL)
	@JsonDeserialize(using = StrictIntegerDeserializer.class)
	private Integer offset;
	@Schema(description = "Corresponds to limit on search service. Default is to return all found entities.")
	@JsonSetter(nulls = Nulls.FAIL)
	@JsonDeserialize(using = StrictIntegerDeserializer.class)
	private Integer limit;
	@Schema(description = "List of specific fields to return.")
	@JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.FAIL)
	@JsonDeserialize(contentUsing = NullableStringDeserializer.class)
	private List<String> returnedFields = new ArrayList<>(Arrays.asList("id", "data.Wgs84Coordinates"));

    @JsonIgnore
    @AssertTrue(message = "Supply a non-blank recordId or dataId")
    public boolean isIdentifierProvided() {
        return StringUtils.hasText(recordId) || StringUtils.hasText(dataId);
    }
	@Override
	public String constructQuery() {
		if(recordId != null && dataId != null){
			return "%s && %s".formatted(
					SearchQueryBuilder.buildPhraseQuery("id", recordId),
					SearchQueryBuilder.buildPhraseQuery("data.ID", dataId));
		}
        if (recordId != null) {
            return SearchQueryBuilder.buildPhraseQuery("id", recordId);
        }
        if (dataId != null) {
            return SearchQueryBuilder.buildPhraseQuery("data.ID", dataId);
        }
        return "";
	}

	@Override
	public SpatialFilter constructSpatialFilter() {
		// returning null in order to not use spatial filter in Search service query
		return null;
	}
}
