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

import org.opengroup.osdu.core.common.model.search.Point;
import org.opengroup.osdu.core.common.model.search.SpatialFilter;
import org.opengroup.osdu.crs.util.AppException;

import java.util.Arrays;
import java.util.List;


public class SpatialQueryBuilder {
    public static SpatialFilter buildSpatialQuery(Double latitude, Double longitude) {
        SpatialFilter spatialFilter = null;

        if (latitude != null && longitude != null) {
            spatialFilter = new SpatialFilter();
            spatialFilter.setField("data.Wgs84Coordinates");
            SpatialFilter.ByWithinPolygon byWithinPolygon = new SpatialFilter.ByWithinPolygon();
            List<Point> points = Arrays.asList(new Point(latitude, longitude));
            byWithinPolygon.setPoints(points);
            spatialFilter.setByWithinPolygon(byWithinPolygon);
        } else if (latitude != null || longitude != null) {
            throw AppException.createBadRequest("Must supply both latitude and longitude when specifying either one");
        }

        return spatialFilter;
    }
}