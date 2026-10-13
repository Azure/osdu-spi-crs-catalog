package org.opengroup.osdu.crs.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opengroup.osdu.crs.model.request.CoordinateReferenceSystemsQuery;
import org.opengroup.osdu.crs.model.request.CoordinateTransformationsQuery;
import org.opengroup.osdu.crs.model.request.InPolygonQuery;
import org.opengroup.osdu.crs.model.response.PointsInAouSearchResult;
import org.opengroup.osdu.crs.model.response.SearchResponse;
import org.opengroup.osdu.crs.service.PointsInAouService;
import org.opengroup.osdu.crs.service.SearchWrapperService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CrsCatalogApiV3Test {

    @Mock
    private SearchWrapperService searchWrapperService;

    @Mock
    private PointsInAouService pointsInAouService;

    @InjectMocks
    private CrsCatalogApiV3 crsCatalogApiV3;

    @Mock
    private SearchResponse searchResponse;

    @Mock
    private PointsInAouSearchResult pointsInAouSearchResult;

    @Mock
    private CoordinateTransformationsQuery coordinateTransformationsQuery;

    @Mock
    private CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery;

    private String recordId = "test_record_id";
    private String dataId = "test_data_id";

    @Test
    void shouldReturnCoordinateTransformation() {
        when(searchWrapperService.getSingleCrsOrCt(recordId, dataId, SearchWrapperService.getCoordinateTransformationKind()))
                .thenReturn(searchResponse);

        SearchResponse response = crsCatalogApiV3.getCoordinateTransformation(recordId, dataId);

        assertEquals(searchResponse, response);
    }

    @Test
    void shouldReturnCoordinateTransformationsWithOffset() {
        coordinateTransformationsQuery.setOffset(0);
        when(searchWrapperService.search(coordinateTransformationsQuery, SearchWrapperService.getCoordinateTransformationKind()))
                .thenReturn(searchResponse);

        SearchResponse response = crsCatalogApiV3.getCoordinateTransformations(coordinateTransformationsQuery);

        assertEquals(searchResponse, response);
    }

    @Test
    void shouldReturnCoordinateReferenceSystem() {
        when(searchWrapperService.getSingleCrsOrCt(recordId, dataId, SearchWrapperService.getCoordinateReferenceSystemKind()))
                .thenReturn(searchResponse);

        SearchResponse response = crsCatalogApiV3.getCoordinateReferenceSystem(recordId, dataId);

        assertEquals(searchResponse, response);
    }

    @Test
    void shouldReturnCoordinateReferenceSystemsWithOffset() {
        coordinateReferenceSystemsQuery.setOffset(0);

        when(searchWrapperService.search(coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateReferenceSystemKind()))
                .thenReturn(searchResponse);

        SearchResponse response = crsCatalogApiV3.getCoordinateReferenceSystems(coordinateReferenceSystemsQuery);

        assertEquals(searchResponse, response);
    }

    @Test
    void shouldReturnPointsInAouSearchResult() {
        InPolygonQuery query = new InPolygonQuery();
        query.setRecordId("testRecordId:");

        when(pointsInAouService.searchPointsInAou(any(InPolygonQuery.class)))
                .thenReturn(pointsInAouSearchResult);

        PointsInAouSearchResult response = crsCatalogApiV3.getAouInfo(query);

        assertEquals(pointsInAouSearchResult, response);
    }

}
