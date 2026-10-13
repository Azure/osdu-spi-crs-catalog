package org.opengroup.osdu.crs.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.opengroup.osdu.core.common.model.search.SpatialFilter;
import org.opengroup.osdu.crs.model.request.InPolygonQuery;

@ExtendWith(MockitoExtension.class)
public class InPolygonQueryTest {

	@InjectMocks
	private InPolygonQuery inPolygonQuery;

	@Test
	public void testConstructQuery(){
		// arrange
		inPolygonQuery.setRecordId("test-record-id");
		inPolygonQuery.setDataId("data-id");

		String expectedQuery = "id: \"test-record-id\" && data.ID: \"data-id\"";

		// act
		String query = inPolygonQuery.constructQuery();

		// assert
		Assertions.assertEquals(expectedQuery, query);
	}

	@Test
	public void testSpatialFilter(){
		// act
		SpatialFilter spatialFilter = inPolygonQuery.constructSpatialFilter();

		// assert
		Assertions.assertNull(spatialFilter);
	}

}
