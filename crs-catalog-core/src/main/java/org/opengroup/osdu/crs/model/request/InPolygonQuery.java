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
import org.opengroup.osdu.core.common.model.search.Point;
import org.opengroup.osdu.core.common.model.search.SpatialFilter;
import org.opengroup.osdu.crs.model.request.ISearchQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Schema(description = "Coordinate Transformations")
public class InPolygonQuery implements ISearchQuery {
	@Schema(description = "Record of CRS or CT to check points against", type = "string")
	private String recordId;
	@Schema(description = "Searches on id for CRS records", type = "string")
	private String dataId;
	@Schema(description = "List of points to check against CRS or CT bounding boxes")
	private List<Point> points = new ArrayList<>();
	@Schema(description = "Corresponds to offset on search service", type = "integer")
	private Integer offset;
	@Schema(description = "Corresponds to limit on search service. Default is to return all found entities.", type = "integer")
	private Integer limit;
	@Schema(description = "List of specific fields to return.")
	private List<String> returnedFields = new ArrayList<>(Arrays.asList("id", "data.Wgs84Coordinates"));
	@Override
	public String constructQuery() {
		String query = "";
		if(recordId != null && dataId != null){
			query = "id: \"%s\" && data.ID: \"%s\"".formatted(recordId, dataId);
		} else if(recordId != null){
			query = "id: \"%s\"".formatted(recordId);
		} else if(dataId != null){
			query = "data.ID: \"%s\"".formatted(dataId);
		}
		return query;
	}

	@Override
	public SpatialFilter constructSpatialFilter() {
		// returning null in order to not use spatial filter in Search service query
		return null;
	}
}
