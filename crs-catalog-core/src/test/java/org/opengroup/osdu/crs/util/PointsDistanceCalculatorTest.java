package org.opengroup.osdu.crs.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.opengroup.osdu.core.common.model.search.Point;

import java.util.Arrays;
import java.util.List;


public class PointsDistanceCalculatorTest {

    private final List<Point> polygon = Arrays.asList(new Point(40.00, 20.00),
                                                      new Point(50.00, 20.00),
                                                      new Point(50.00, 30.00),
                                                      new Point(40.00, 30.00),
                                                      new Point(40.00, 20.00));

    @Test
    public void testLongitudeDistance() {
        final double eps = 1E-3;
        double westBound = -179.0;
        double eastBound = -160.0;

        List<Double> longitude = Arrays.asList(179.0, -179.1, -159.0, -170.0);
        List<Double> distance = Arrays.asList(2.0, 0.1, 1.0, 0.0);
        for (int i = 0; i < longitude.size(); i++) {
            Assertions.assertEquals(distance.get(i), PointDistanceCalculator.longitudeDistance(longitude.get(i), westBound, eastBound).doubleValue(), eps);
        }

        westBound = 10.0;
        eastBound = 20.0;
        longitude = Arrays.asList(179.0, -179.1, -169.9, 11.0);
        distance = Arrays.asList(159.0, 160.9, 170.1, 0.0);
        for (int i = 0; i < longitude.size(); i++) {
            Assertions.assertEquals(distance.get(i), PointDistanceCalculator.longitudeDistance(longitude.get(i), westBound, eastBound).doubleValue(), eps);
        }

        westBound = 170.0;
        eastBound = 178.0;
        longitude = Arrays.asList(179.0, -179.1, -159.0, 178.0);
        distance = Arrays.asList(1.0, 2.9, 23.0, 0.0);
        for (int i = 0; i < longitude.size(); i++) {
            Assertions.assertEquals(distance.get(i), PointDistanceCalculator.longitudeDistance(longitude.get(i), westBound, eastBound), eps);
        }
    }

    @Test
    public void testLatitudeDistance() {
        final double eps = 1E-3;
        double southBound = -50.0;
        double northBound = 10.0;
        List<Double> latitude = Arrays.asList(-90.0, -70.0, -50.0, -10.0, -30.0, 0.0, 10.0, 30.0, 70.0, 90.0);
        List<Double> distance = Arrays.asList(40.0, 20.0, 0.0, 0.0, 0.0, 0.0, 0.0, 20.0, 60.0, 80.0);
        for (int i = 0; i < latitude.size(); i++) {
            Assertions.assertEquals(distance.get(i), PointDistanceCalculator.latitudeDistance(latitude.get(i), southBound, northBound), eps);
        }
    }

    @Test
    public void testKmDistanceZeroDistance() {
        List<Double> latitude = Arrays.asList(45.0, 40.0, 50.0, 40.0, 50.0);
        List<Double> longitude = Arrays.asList(25.0, 20.0, 20.0, 30.0, 30.0);
        for (int i = 0; i < latitude.size(); i++) {
            Assertions.assertEquals(0, PointDistanceCalculator.kmDistance(new Point(latitude.get(i), longitude.get(i)), polygon), 0.0);
        }
    }

    @Test
    public void testKmDistance() {
        final double eps = 1E-3;
        List<Double> latitude = Arrays.asList(39.0, 45.0, 0.0, 10.0, -30.0, 70.0, 90.0, -90.0);
        List<Double> longitude = Arrays.asList(25.0, 31.0, 0.0, 10.0, -30.0, 170.0, -90.0, 73.0);
        List<Double> distance = Arrays.asList(110.0, 77.7817, 4919.350, 3473.257, 9054.143, 5708.104, 4400.0, 14300.0);
        for (int i = 0; i < latitude.size(); i++) {
            Assertions.assertEquals(distance.get(i), PointDistanceCalculator.kmDistance(new Point(latitude.get(i), longitude.get(i)), polygon), eps);
        }
    }
}