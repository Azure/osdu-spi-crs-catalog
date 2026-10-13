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

package org.opengroup.osdu.crs.model.geometry;

import lombok.Data;
import org.opengroup.osdu.core.common.model.search.Point;
import org.opengroup.osdu.crs.util.AppException;

import java.util.List;


@Data
public class Rectangle {
	private Point max;
	private Point min;

	public Rectangle(List<Point> points){
		verify(points);
		max = points.get(0);
		min = points.get(0);

		for(Point point : points) {
			if(point.getLatitude() > max.getLatitude() && point.getLongitude() > max.getLongitude()){
				max = point;
			} else if(point.getLatitude() < min.getLatitude() && point.getLongitude() < min.getLongitude()){
				min = point;
			}
		}
	}

	private void verify(List<Point> points){
		if(points.size() != 5){
			throw AppException.createBadRequest(
            "Record polygon must be bounding box with 5 points (the last one being the same as first to close polygon)".formatted());
		}
	}

}
