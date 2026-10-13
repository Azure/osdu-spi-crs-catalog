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

package org.opengroup.osdu.crs.service;

import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.search.Point;
import org.opengroup.osdu.core.common.model.search.Polygon;
import org.opengroup.osdu.crs.model.request.InPolygonQuery;
import org.opengroup.osdu.crs.model.response.PointsInAouSearchPoint;
import org.opengroup.osdu.crs.model.response.PointsInAouSearchResult;
import org.opengroup.osdu.crs.model.response.SearchResponse;
import org.opengroup.osdu.crs.util.AppException;
import org.opengroup.osdu.crs.util.PointDistanceCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PointsInAouService {

    @Inject
    private SearchWrapperService searchWrapperService;

    @Inject
    private JaxRsDpsLog logger;

    public PointsInAouSearchResult searchPointsInAou(InPolygonQuery inPolygonQuery) {
        List<Polygon> polygons = getRecordPolygons(inPolygonQuery);

        double maxDistanceOutKm = 0;
        int index = 0;
        PointsInAouSearchResult pointsInAouSearchResult = new PointsInAouSearchResult();
        for (Point testPoint : inPolygonQuery.getPoints()) {
            Double kmDistance = Double.POSITIVE_INFINITY;
            for (Polygon polygon : polygons) {
                Double dist = PointDistanceCalculator.kmDistance(testPoint, polygon.getPoints());
                if (dist < kmDistance) {
                    kmDistance = dist;
                }
            }
            if (kmDistance > 0.5) { // Not using zero as the threshold because the error is roughly 1 km
                logger.debug("Point outside polygon: %s, %s".formatted(testPoint.toString(), kmDistance));
                addFailedPoint(index, testPoint, kmDistance, pointsInAouSearchResult);
                if (kmDistance > maxDistanceOutKm) {
                    maxDistanceOutKm = kmDistance;
                }
            } else {
                logger.debug("Point inside polygon: %s".formatted(testPoint.toString()));
            }
            index++;
        }

        setMaxDistanceOutKm(maxDistanceOutKm, pointsInAouSearchResult);

        return pointsInAouSearchResult;
    }

    private void setMaxDistanceOutKm(Double maxDistanceOutKm, PointsInAouSearchResult pointsInAouSearchResult) {
        pointsInAouSearchResult.setMaxDistKmOutsideBBox(roundDouble(maxDistanceOutKm));
    }

    private void addFailedPoint(int indexFailedPoint, Point failedPoint, Double kmDistance, PointsInAouSearchResult pointsInAouSearchResult) {
        PointsInAouSearchPoint pointsInAouSearchPoint = new PointsInAouSearchPoint();
        pointsInAouSearchPoint.setPoint(failedPoint);
        pointsInAouSearchPoint.setIndex(indexFailedPoint);
        pointsInAouSearchPoint.setApproximateKmDistanceOutside(roundDouble(kmDistance));
        pointsInAouSearchResult.addBboxFailedPoint(pointsInAouSearchPoint);
    }

    private Integer roundDouble(double value) {
        return (int) Math.round(value);
    }

    private List<Polygon> getRecordPolygons(InPolygonQuery inPolygonQuery) {
        SearchResponse searchResponse = searchWrapperService.search(inPolygonQuery, SearchWrapperService.getCtAndCrsKind());
        List<Map<String, Object>> searchResults = searchResponse.getSearchResults().getResults();
        if (searchResults.size() != 1) {
            throw AppException.createBadRequest(String.format(
                    "Received %s results from search, should only get one. Query: %s"
                    , searchResults.size(), searchResponse.getQuery()));
        }

        String geometryType;
        Map<String, Object> geometry;
        try {
            Map<String, Object> searchResult = searchResults.get(0);
            Map<String, Object> searchResultData = (Map) searchResult.get("data");
            Map<String, Object> wgs84Coordinates = (Map) searchResultData.get("Wgs84Coordinates");
            List<Object> geometries = (List) wgs84Coordinates.get("geometries");
            if (geometries.size() != 1) {
                throw AppException.createBadRequest(String.format(
                        "Record Wgs84Coordinates should only have one feature for polygon. Query: %s"
                        , searchResponse.getQuery()));
            }
            geometry = (Map) geometries.get(0);
            geometryType = (String) geometry.get("type");
        } catch (ClassCastException e) {
            logger.error("Failed to deserialize search result", e);
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to deserialize search result, see logs", e.getMessage());
        }

        List<Polygon> polygons = new ArrayList<>();
        String query = searchResponse.getQuery();

        if ("polygon".equalsIgnoreCase(geometryType)) {
            List<List<List<Double>>> coordinates = (List) geometry.get("coordinates");
            logger.debug("Successfully retrieved polygon");
            polygons.add(buildPolygonFromRing(coordinates.get(0), query));
        } else if ("multipolygon".equalsIgnoreCase(geometryType)) {
            List<List<List<List<Double>>>> multiCoordinatesList = (List) geometry.get("coordinates");
            logger.debug("Successfully retrieved multipolygon");
            for (List<List<List<Double>>> polygonCoordinates : multiCoordinatesList) {
                polygons.add(buildPolygonFromRing(polygonCoordinates.get(0), query));
            }
        } else {
            throw AppException.createBadRequest(String.format(
                    "Unsupported geometry type '%s'. Query: %s", geometryType, query));
        }
        return polygons;
    }

    private Polygon buildPolygonFromRing(List<List<Double>> ring, String query) {
        List<Point> points = new ArrayList<>();
        for (List<Double> coordinate : ring) {
            if (coordinate.size() != 2) {
                throw AppException.createBadRequest(String.format(
                        "Record has coordinate value with invalid amount of points. Query: %s", query));
            }
            logger.debug("Polygon coordinate: %s, %s".formatted(coordinate.get(1), coordinate.get(0)));
            points.add(new Point(coordinate.get(1), coordinate.get(0)));
        }
        return new Polygon(points);
    }

}
