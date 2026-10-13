package org.opengroup.osdu.crs.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.opengroup.osdu.core.common.model.search.SpatialFilter;
import org.opengroup.osdu.crs.model.request.CoordinateTransformationsQuery;

@ExtendWith(MockitoExtension.class)
public class CoordinateTransformationsQueryTest {

    @InjectMocks
    private CoordinateTransformationsQuery coordinateTransformationsQuery;

    @Test
    public void testConstructQuery() {
        // arrange
        coordinateTransformationsQuery.setCodeSpace("EPSG");
        coordinateTransformationsQuery.setName("Ain el");
        coordinateTransformationsQuery.setSourceCRS("osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG");
        coordinateTransformationsQuery.setTargetCRS("osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326:");

        String expectedQuery = "(NOT data.InactiveIndicator: true) AND (NOT data.Kind: \"VerticalTransformation\") AND (data.CodeSpace: \"EPSG\") AND (data.Name: \"Ain el\") AND ((data.SourceCRS.SourceCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG\") OR (data.TargetCRS.TargetCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG\")) AND ((data.SourceCRS.SourceCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326:\") OR (data.TargetCRS.TargetCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326:\"))";
        // act
        String query = coordinateTransformationsQuery.constructQuery();

        // assert
        Assertions.assertEquals(expectedQuery, query);
    }

    @Test
    public void testConstructQueryVertical() {
        // arrange
        coordinateTransformationsQuery.setCodeSpace("EPSG");
        coordinateTransformationsQuery.setKind("VerticalTransformation");

        String expectedQuery = "(NOT data.InactiveIndicator: true) AND (data.Kind: \"VerticalTransformation\") AND (data.CodeSpace: \"EPSG\")";

        // act
        String query = coordinateTransformationsQuery.constructQuery();

        // assert
        Assertions.assertEquals(expectedQuery, query);
    }

    @Test
    public void testConstructQueryAll() {
        // arrange
        coordinateTransformationsQuery.setCodeSpace("EPSG");
        coordinateTransformationsQuery.setKind("All");

        String expectedQuery = "(NOT data.InactiveIndicator: true) AND (data.CodeSpace: \"EPSG\")";

        // act
        String query = coordinateTransformationsQuery.constructQuery();

        // assert
        Assertions.assertEquals(expectedQuery, query);
    }

    @Test
    public void testSpatialFilter() {
        // arrange
        double latitude = 10.56;
        double longitude = 88.8;
        coordinateTransformationsQuery.setLatitude(latitude);
        coordinateTransformationsQuery.setLongitude(longitude);

        // act
        SpatialFilter spatialFilter = coordinateTransformationsQuery.constructSpatialFilter();

        // assert
        Assertions.assertNotNull(spatialFilter.getByWithinPolygon());
        Assertions.assertEquals(1, spatialFilter.getByWithinPolygon().getPoints().size());
        Assertions.assertEquals(latitude, spatialFilter.getByWithinPolygon().getPoints().get(0).getLatitude(), 0d);
        Assertions.assertEquals(longitude, spatialFilter.getByWithinPolygon().getPoints().get(0).getLongitude(), 0d);
    }

}
