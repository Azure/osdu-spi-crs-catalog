package org.opengroup.osdu.crs.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.opengroup.osdu.core.common.model.search.SpatialFilter;
import org.opengroup.osdu.crs.model.request.BaseCRS;
import org.opengroup.osdu.crs.model.request.CoordinateReferenceSystemsQuery;
import org.opengroup.osdu.crs.model.request.Datum;
import org.opengroup.osdu.crs.model.request.Extent;

@ExtendWith(MockitoExtension.class)
public class CoordinateReferenceSystemsQueryTest {

	@InjectMocks
	private CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery;

	@Test
	public void testConstructQuery(){
		// arrange
		coordinateReferenceSystemsQuery.setCodeSpace("EPSG");
		coordinateReferenceSystemsQuery.setName("Cadastre");
		coordinateReferenceSystemsQuery.setId("Geographic3D:EPSG::4472");
		coordinateReferenceSystemsQuery.setCode("4472");
		coordinateReferenceSystemsQuery.setKind("geographic 3D");
		BaseCRS baseCRS = new BaseCRS();
		baseCRS.setId("osdu:reference-data--CoordinateReferenceSystem:Geocentric:EPSG::4473:");
		baseCRS.setName("Cadastre");
		coordinateReferenceSystemsQuery.setBaseCRS(baseCRS);
		Datum datum = new Datum();
		datum.setCode("1037");
		datum.setName("Cadastre");
		datum.setCodeSpace("EPSG");
		coordinateReferenceSystemsQuery.setDatum(datum);
		Extent extent = new Extent();
		extent.setDescription("Mayonette");
		coordinateReferenceSystemsQuery.setExtent(extent);
		coordinateReferenceSystemsQuery.setCoordinateReferenceSystemType("GeodeticCRS");

		String expectedQuery = "(NOT data.InactiveIndicator: true) AND (data.CodeSpace: \"EPSG\") AND (data.Code: \"4472\") AND (data.Name: \"Cadastre\") AND (data.ID: \"Geographic3D:EPSG::4472\") AND (data.Kind: \"geographic 3D\") AND (data.CoordinateReferenceSystemType: \"GeodeticCRS\") AND (data.BaseCRS.BaseCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geocentric:EPSG::4473:\") AND (data.BaseCRS.Name: \"Cadastre\") AND (data.Datum.Name: \"Cadastre\") AND (data.Datum.AuthorityCode.Authority: \"EPSG\") AND (data.Datum.AuthorityCode.Code: \"1037\") AND (data.PreferredUsage.Extent.Description: \"Mayonette\")";

		// act
		String query = coordinateReferenceSystemsQuery.constructQuery();

		// assert
		Assertions.assertEquals(expectedQuery, query);
	}

	@Test
	public void testConstructQueryIncludeDeprecated(){
		// arrange
		coordinateReferenceSystemsQuery.setIncludeDeprecated(true);
		// act
		String query = coordinateReferenceSystemsQuery.constructQuery();
		// assert
		Assertions.assertTrue(query.isEmpty());
	}
	@Test
	public void testConstructQueryBoundProjected(){
		// arrange
		coordinateReferenceSystemsQuery.setCodeSpace("EPSG");
		coordinateReferenceSystemsQuery.setReturnBoundProjectedAndProjectedBasedOnWgs84(true);
		String expectedQuery = "(NOT data.InactiveIndicator: true) AND (data.CodeSpace: \"EPSG\") AND ((data.Kind: \"BoundProjected\") OR ((data.Kind: \"projected\") AND (data.BaseCRS.AuthorityCode.Code: \"4326\")))";

		// act
		String query = coordinateReferenceSystemsQuery.constructQuery();

		// assert
		Assertions.assertEquals(expectedQuery, query);
	}

	@Test
	public void testConstructQueryBoundGeographic(){
		// arrange
		coordinateReferenceSystemsQuery.setCodeSpace("EPSG");
		coordinateReferenceSystemsQuery.setReturnBoundGeographic2DAndWgs84(true);
		String expectedQuery = "(NOT data.InactiveIndicator: true) AND (data.CodeSpace: \"EPSG\") AND ((data.Kind: \"BoundGeographic2D\") OR ((data.Kind: \"geographic 2D\") AND (data.Code: \"4326\") AND (data.CodeSpace: \"EPSG\")))";
		// act
		String query = coordinateReferenceSystemsQuery.constructQuery();

		// assert
		Assertions.assertEquals(expectedQuery, query);
	}

	@Test
	public void testSpatialFilter(){
		// arrange
		double latitude = 10.56;
		double longitude = 88.8;
		coordinateReferenceSystemsQuery.setLatitude(latitude);
		coordinateReferenceSystemsQuery.setLongitude(longitude);

		// act
		SpatialFilter spatialFilter = coordinateReferenceSystemsQuery.constructSpatialFilter();

		// assert
		Assertions.assertNotNull(spatialFilter.getByWithinPolygon());
		Assertions.assertEquals(1, spatialFilter.getByWithinPolygon().getPoints().size());
		Assertions.assertEquals(latitude, spatialFilter.getByWithinPolygon().getPoints().get(0).getLatitude(), 0d);
		Assertions.assertEquals(longitude,spatialFilter.getByWithinPolygon().getPoints().get(0).getLongitude(), 0d);
	}
}
