package org.opengroup.osdu.crs.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.search.Point;
import org.opengroup.osdu.core.common.model.search.QueryResponse;
import org.opengroup.osdu.crs.model.response.PointsInAouSearchResult;
import org.opengroup.osdu.crs.model.request.InPolygonQuery;
import org.opengroup.osdu.crs.model.response.SearchResponse;
import org.opengroup.osdu.crs.util.AppException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PointsInAouServiceTest {

	@InjectMocks
	private PointsInAouService pointsInAouService;

	@Mock
	private SearchWrapperService searchWrapperService;

	@Mock
	private JaxRsDpsLog logger;

	@Test
	public void testSimpleDistanceCalculation(){
		// arrange
		Point test = new Point();
		test.setLatitude(7.40);
		test.setLongitude(10.23);

		InPolygonQuery inPolygonQuery = new InPolygonQuery();
		inPolygonQuery.setPoints(Arrays.asList(test));
		inPolygonQuery.setDataId("test-data-id");
		inPolygonQuery.setRecordId("test-record-id");

		List<Double> coor1 = Arrays.asList(1.00, 2.78);
		List<Double> coor2 = Arrays.asList(9.11, 2.78);
		List<Double> coor3 = Arrays.asList(9.11, 6.01);
		List<Double> coor4 = Arrays.asList(1.00, 6.01);
		List<Double> coor5 = Arrays.asList(1.00, 2.78);
		List<List<Double>> coordinates = Arrays.asList(coor1, coor2, coor3, coor4, coor5);
		List<List<List<Double>>> nestedCoordinates = Arrays.asList(coordinates);
		Map<String, Object> geometry = new HashMap<>();
		geometry.put("coordinates", nestedCoordinates);
		geometry.put("type", "polygon");
		List<Object> geometries = Arrays.asList(geometry);
		Map<String, Object> wgs84Coordinates = new HashMap<>();
		wgs84Coordinates.put("geometries", geometries);
		Map<String, Object> data = new HashMap<>();
		data.put("Wgs84Coordinates", wgs84Coordinates);
		Map<String, Object> d = new HashMap<>();
		d.put("data", data);
		List<Map<String, Object>> results = Arrays.asList(d);
		QueryResponse queryResponse = new QueryResponse();
		queryResponse.setResults(results);
		SearchResponse searchResponse = new SearchResponse(queryResponse, "");
		Mockito.when(searchWrapperService.search(Mockito.any(), Mockito.any()))
				.thenReturn(searchResponse);

		// act
		PointsInAouSearchResult result = pointsInAouService.searchPointsInAou(inPolygonQuery);

		// assert
		Assertions.assertEquals(1, result.getBboxFailedPoints().size());
		Assertions.assertEquals(0, result.getBboxFailedPoints().get(0).getIndex().intValue());
		Assertions.assertEquals(196, result.getBboxFailedPoints().get(0).getApproximateKmDistanceOutside().intValue());
		Assertions.assertEquals(196, result.getMaxDistKmOutsideBBox(), 0d);
	}

	@Test
	public void testMultipleDistanceCalculations(){
		// arrange
		Point test1 = new Point();
		test1.setLatitude(7.40);
		test1.setLongitude(10.23);

		Point test2 = new Point();
		test2.setLatitude(89);
		test2.setLongitude(-179);

		Point test3 = new Point();
		test3.setLatitude(0);
		test3.setLongitude(0);

		Point test4 = new Point();
		test4.setLatitude(-77.55465765761);
		test4.setLongitude(-100.1);

		Point test5 = new Point();
		test5.setLatitude(20.78);
		test5.setLongitude(100.00000001);

		InPolygonQuery inPolygonQuery = new InPolygonQuery();
		inPolygonQuery.setPoints(Arrays.asList(test1, test2, test3, test4, test5));
		inPolygonQuery.setDataId("test-data-id");
		inPolygonQuery.setRecordId("test-record-id");

		List<Double> coor1 = Arrays.asList(100.00, 20.78);
		List<Double> coor2 = Arrays.asList(9.11, 20.78);
		List<Double> coor3 = Arrays.asList(9.11, -6.01);
		List<Double> coor4 = Arrays.asList(100.00, -6.01);
		List<Double> coor5 = Arrays.asList(100.00, 20.78);
		List<List<Double>> coordinates = Arrays.asList(coor1, coor2, coor3, coor4, coor5);
		List<List<List<Double>>> nestedCoordinates = Arrays.asList(coordinates);
		Map<String, Object> geometry = new HashMap<>();
		geometry.put("coordinates", nestedCoordinates);
		geometry.put("type", "polygon");
		List<Object> geometries = Arrays.asList(geometry);
		Map<String, Object> wgs84Coordinates = new HashMap<>();
		wgs84Coordinates.put("geometries", geometries);
		Map<String, Object> data = new HashMap<>();
		data.put("Wgs84Coordinates", wgs84Coordinates);
		Map<String, Object> d = new HashMap<>();
		d.put("data", data);
		List<Map<String, Object>> results = Arrays.asList(d);
		QueryResponse queryResponse = new QueryResponse();
		queryResponse.setResults(results);
		SearchResponse searchResponse = new SearchResponse(queryResponse, "");
		Mockito.when(searchWrapperService.search(Mockito.any(), Mockito.any()))
				.thenReturn(searchResponse);

		// act
		PointsInAouSearchResult result = pointsInAouService.searchPointsInAou(inPolygonQuery);

		// assert
		Assertions.assertEquals(3, result.getBboxFailedPoints().size());
		Assertions.assertEquals(1, result.getBboxFailedPoints().get(0).getIndex().intValue());
		Assertions.assertEquals(7506, result.getBboxFailedPoints().get(0).getApproximateKmDistanceOutside().intValue());
		Assertions.assertEquals(2, result.getBboxFailedPoints().get(1).getIndex().intValue());
		Assertions.assertEquals(1002, result.getBboxFailedPoints().get(1).getApproximateKmDistanceOutside().intValue());
		Assertions.assertEquals(3, result.getBboxFailedPoints().get(2).getIndex().intValue());
		Assertions.assertEquals(8285, result.getBboxFailedPoints().get(2).getApproximateKmDistanceOutside().intValue());
		Assertions.assertEquals(8285, result.getMaxDistKmOutsideBBox().intValue());
	}


	@Test
	public void testMultiPolygonDistanceCalculation(){
		// arrange
		Point test1 = new Point();
		test1.setLatitude(25.00);
		test1.setLongitude(47.00);

		Point test2 = new Point();
		test2.setLatitude(61.00);
		test2.setLongitude(89.00);

		Point test3 = new Point();
		test3.setLatitude(35.00);
		test3.setLongitude(65.00);

		InPolygonQuery inPolygonQuery = new InPolygonQuery();
		inPolygonQuery.setPoints(Arrays.asList(test1, test2, test3));
		inPolygonQuery.setDataId("test-data-id");
		inPolygonQuery.setRecordId("test-record-id");

		List<Double> coor1 = Arrays.asList(40.00, 20.00);
		List<Double> coor2 = Arrays.asList(50.00, 20.00);
		List<Double> coor3 = Arrays.asList(50.00, 30.00);
		List<Double> coor4 = Arrays.asList(40.00, 30.00);
		List<Double> coor5 = Arrays.asList(40.00, 20.00);
		List<List<Double>> coordinates1 = Arrays.asList(coor1, coor2, coor3, coor4, coor5);
		List<Double> coor6 = Arrays.asList(80.00, 60.00);
		List<Double> coor7 = Arrays.asList(90.00, 60.00);
		List<Double> coor8 = Arrays.asList(90.00, 70.00);
		List<Double> coor9 = Arrays.asList(80.00, 70.00);
		List<Double> coor10 = Arrays.asList(80.00, 60.00);
		List<List<Double>> coordinates2 = Arrays.asList(coor6, coor7, coor8, coor9, coor10);
		List<List<List<List<Double>>>> nestedCoordinates = Arrays.asList(
				Arrays.asList(coordinates1),
				Arrays.asList(coordinates2));
		Map<String, Object> geometry = new HashMap<>();
		geometry.put("coordinates", nestedCoordinates);
		geometry.put("type", "MultiPolygon");
		List<Object> geometries = Arrays.asList(geometry);
		Map<String, Object> wgs84Coordinates = new HashMap<>();
		wgs84Coordinates.put("geometries", geometries);
		Map<String, Object> data = new HashMap<>();
		data.put("Wgs84Coordinates", wgs84Coordinates);
		Map<String, Object> d = new HashMap<>();
		d.put("data", data);
		List<Map<String, Object>> results = Arrays.asList(d);
		QueryResponse queryResponse = new QueryResponse();
		queryResponse.setResults(results);
		SearchResponse searchResponse = new SearchResponse(queryResponse, "");
		Mockito.when(searchWrapperService.search(Mockito.any(), Mockito.any()))
			   .thenReturn(searchResponse);

		// act
		PointsInAouSearchResult result = pointsInAouService.searchPointsInAou(inPolygonQuery);

		// assert
		Assertions.assertEquals(1, result.getBboxFailedPoints().size());
		Assertions.assertEquals(2, result.getBboxFailedPoints().get(0).getIndex().intValue());
		Assertions.assertEquals(1459, result.getBboxFailedPoints().get(0).getApproximateKmDistanceOutside().intValue());
		Assertions.assertEquals(1459, result.getMaxDistKmOutsideBBox(), 0d);
	}

	@Test
	void testAntimeridianMultiPolygonDistanceCalculation() {
		Point insideEast = new Point();
		insideEast.setLatitude(-40.0);
		insideEast.setLongitude(175.0);

		Point insideWest = new Point();
		insideWest.setLatitude(-40.0);
		insideWest.setLongitude(-175.0);

		Point outsideSouth = new Point();
		outsideSouth.setLatitude(-55.0);
		outsideSouth.setLongitude(159.6);

		Point outsideEast = new Point();
		outsideEast.setLatitude(-40.0);
		outsideEast.setLongitude(-170.2);

		InPolygonQuery inPolygonQuery = new InPolygonQuery();
		inPolygonQuery.setPoints(Arrays.asList(insideEast, insideWest, outsideSouth, outsideEast));
		inPolygonQuery.setRecordId("test-record-id");

		List<List<Double>> eastRing = Arrays.asList(
				Arrays.asList(160.6, -55.95),
				Arrays.asList(180.0, -55.95),
				Arrays.asList(180.0, -25.88),
				Arrays.asList(160.6, -25.88),
				Arrays.asList(160.6, -55.95));
		List<List<Double>> westRing = Arrays.asList(
				Arrays.asList(-180.0, -55.95),
				Arrays.asList(-171.2, -55.95),
				Arrays.asList(-171.2, -25.88),
				Arrays.asList(-180.0, -25.88),
				Arrays.asList(-180.0, -55.95));
		List<List<List<List<Double>>>> nestedCoordinates = Arrays.asList(
				Arrays.asList(eastRing),
				Arrays.asList(westRing));
		Map<String, Object> geometry = new HashMap<>();
		geometry.put("coordinates", nestedCoordinates);
		geometry.put("type", "MultiPolygon");
		List<Object> geometries = Arrays.asList(geometry);
		Map<String, Object> wgs84Coordinates = new HashMap<>();
		wgs84Coordinates.put("geometries", geometries);
		Map<String, Object> data = new HashMap<>();
		data.put("Wgs84Coordinates", wgs84Coordinates);
		Map<String, Object> d = new HashMap<>();
		d.put("data", data);
		QueryResponse queryResponse = new QueryResponse();
		queryResponse.setResults(Arrays.asList(d));
		SearchResponse searchResponse = new SearchResponse(queryResponse, "");
		Mockito.when(searchWrapperService.search(Mockito.any(), Mockito.any()))
				.thenReturn(searchResponse);

		PointsInAouSearchResult result = pointsInAouService.searchPointsInAou(inPolygonQuery);

		Assertions.assertEquals(2, result.getBboxFailedPoints().size());
		Assertions.assertEquals(2, result.getBboxFailedPoints().get(0).getIndex().intValue());
		Assertions.assertEquals(63, result.getBboxFailedPoints().get(0).getApproximateKmDistanceOutside().intValue());
		Assertions.assertEquals(3, result.getBboxFailedPoints().get(1).getIndex().intValue());
		Assertions.assertEquals(84, result.getBboxFailedPoints().get(1).getApproximateKmDistanceOutside().intValue());
		Assertions.assertEquals(84, result.getMaxDistKmOutsideBBox().intValue());
	}

	@Test
	void testUnsupportedGeometryTypeThrowsBadRequest() {
		Point test = new Point();
		test.setLatitude(7.40);
		test.setLongitude(10.23);

		InPolygonQuery inPolygonQuery = new InPolygonQuery();
		inPolygonQuery.setPoints(Arrays.asList(test));
		inPolygonQuery.setRecordId("test-record-id");

		List<Double> coor1 = Arrays.asList(1.00, 2.78);
		List<Double> coor2 = Arrays.asList(9.11, 2.78);
		List<Double> coor3 = Arrays.asList(9.11, 6.01);
		List<Double> coor4 = Arrays.asList(1.00, 6.01);
		List<Double> coor5 = Arrays.asList(1.00, 2.78);
		List<List<Double>> coordinates = Arrays.asList(coor1, coor2, coor3, coor4, coor5);
		Map<String, Object> geometry = new HashMap<>();
		geometry.put("coordinates", coordinates);
		geometry.put("type", "LineString");
		List<Object> geometries = Arrays.asList(geometry);
		Map<String, Object> wgs84Coordinates = new HashMap<>();
		wgs84Coordinates.put("geometries", geometries);
		Map<String, Object> data = new HashMap<>();
		data.put("Wgs84Coordinates", wgs84Coordinates);
		Map<String, Object> d = new HashMap<>();
		d.put("data", data);
		QueryResponse queryResponse = new QueryResponse();
		queryResponse.setResults(Arrays.asList(d));
		SearchResponse searchResponse = new SearchResponse(queryResponse, "test-query");
		Mockito.when(searchWrapperService.search(Mockito.any(), Mockito.any()))
				.thenReturn(searchResponse);

		Assertions.assertThrows(AppException.class,
				() -> pointsInAouService.searchPointsInAou(inPolygonQuery));
	}
}
