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

package org.opengroup.osdu.crs.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.core.common.model.search.Point;

@Data
@Schema(description = "A point that didn't land in the bounding box for area of use search")
public class PointsInAouSearchPoint {
	@Schema(description = "Lat, long point")
	private Point point;
	@Schema(description = "A zero-based index of the point in the input \"points\" array")
	private Integer index;
	@Schema(description = "Kilometers outside the record's bounding box")
	private Integer approximateKmDistanceOutside;
}
