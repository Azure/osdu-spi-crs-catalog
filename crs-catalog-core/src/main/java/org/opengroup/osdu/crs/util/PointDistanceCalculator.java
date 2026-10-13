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

package org.opengroup.osdu.crs.util;

import org.opengroup.osdu.core.common.model.search.Point;
import org.opengroup.osdu.crs.model.geometry.Rectangle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PointDistanceCalculator {

    private static final double cycleDegree = 360.0;

    private static Double adjustLongitude(Double value) {
        if (value < -180.0) {
            return value + cycleDegree;
        }
        if (value > 180.0) {
            return value - cycleDegree;
        }
        return value;
    }

    public static Double longitudeDistance(double longitude, double westBoundLongitude, double eastBoundLongitude) {
        double westDistance = westBoundLongitude - longitude;
        double eastDistance = longitude - eastBoundLongitude;
        if (westDistance <= 0 && eastDistance <= 0) {
            return 0.0;
        }
        if (westDistance < 0) {
            westDistance += cycleDegree;
        } else {
            eastDistance += cycleDegree;
        }
        return Math.min(westDistance, eastDistance);
    }

    public static Double latitudeDistance(double latitude, double southBoundLatitude, double northBoundLatitude) {
        return Collections.max(Arrays.asList(
                southBoundLatitude - latitude,
                latitude - northBoundLatitude,
                0.0));
    }

    // Could be extended to work with polygons with these considerations: calculate distance to edge (if v=xy then n=(-y,x)), calculate distances to vertices,
    // and then do it for each edge since a polygon's points could make complex shapes.
    // Keeping rectangle assumption for now because it's a bit faster and no requirement for polygon distance yet.
    public static Double kmDistance(Point point, List<Point> polygon) {
        // Assumption for the input bounding box: the bounding box west longitude is smaller than the bounding box east longitude (not across anti-meridian)
        Rectangle rectangle = new Rectangle(polygon);

        double deltaLongitude = longitudeDistance(adjustLongitude(point.getLongitude()), rectangle.getMin().getLongitude(), rectangle.getMax().getLongitude());
        double deltaLatitude = latitudeDistance(point.getLatitude(), rectangle.getMin().getLatitude(), rectangle.getMax().getLatitude());

        return Math.sqrt(Math.pow(110.0 * deltaLatitude, 2.0) + Math.pow(110.0 * deltaLongitude * Math.cos(Math.toRadians(point.getLatitude())), 2.0));
    }
}
