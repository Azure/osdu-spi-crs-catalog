package org.opengroup.osdu.crs.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.core.common.model.search.*;
import org.opengroup.osdu.core.common.search.ISearchFactory;
import org.opengroup.osdu.core.common.search.ISearchService;
import org.opengroup.osdu.crs.model.request.BaseCRS;
import org.opengroup.osdu.crs.model.request.CoordinateReferenceSystemsQuery;
import org.opengroup.osdu.crs.model.request.Datum;
import org.opengroup.osdu.crs.model.request.Extent;
import org.opengroup.osdu.crs.model.response.SearchResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class SearchWrapperServiceTest {

	@InjectMocks
	private SearchWrapperService searchWrapperService;

	@Mock
	ISearchFactory searchFactory;

	@Mock
	DpsHeaders dpsHeaders;

	@Mock
	JaxRsDpsLog logger;

	@Mock
	ISearchService searchService;

	@Test
	public void testSearch() throws SearchException {
		// arrange
		CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery = new CoordinateReferenceSystemsQuery();
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
		coordinateReferenceSystemsQuery.setLongitude(10.2);
		coordinateReferenceSystemsQuery.setLatitude(10.1);

		String expectedQuery = "(NOT data.InactiveIndicator: true) AND (data.CodeSpace: \"EPSG\") AND (data.Code: \"4472\") AND (data.Name: \"Cadastre\") AND (data.ID: \"Geographic3D:EPSG::4472\") AND (data.Kind: \"geographic 3D\") AND (data.CoordinateReferenceSystemType: \"GeodeticCRS\") AND (data.BaseCRS.BaseCRSID: \"osdu:reference-data--CoordinateReferenceSystem:Geocentric:EPSG::4473:\") AND (data.BaseCRS.Name: \"Cadastre\") AND (data.Datum.Name: \"Cadastre\") AND (data.Datum.AuthorityCode.Authority: \"EPSG\") AND (data.Datum.AuthorityCode.Code: \"1037\") AND (data.PreferredUsage.Extent.Description: \"Mayonette\")";

		SpatialFilter expectedSpatialFilter = new SpatialFilter();
		SpatialFilter.ByWithinPolygon byWithinPolygon = new SpatialFilter.ByWithinPolygon();
		byWithinPolygon.setPoints(Arrays.asList(new Point(10.1, 10.2)));
		expectedSpatialFilter.setByWithinPolygon(byWithinPolygon);

		QueryRequest expectedQueryRequest = new QueryRequest();
		expectedQueryRequest.setQuery(expectedQuery);
		expectedQueryRequest.setSpatialFilter(expectedSpatialFilter);

		Mockito.when(searchService.search(Mockito.any()))
				.thenReturn(new QueryResponse());

		// act
		SearchResponse searchResponse = searchWrapperService.search(coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateReferenceSystemKind());

		// assert
		ArgumentCaptor<QueryRequest> queryRequestArg = ArgumentCaptor.forClass(QueryRequest.class);
		Mockito.verify(searchService).search(queryRequestArg.capture());
		QueryRequest actualQueryRequest = queryRequestArg.getValue();
		Assertions.assertEquals(expectedQueryRequest.getQuery(), actualQueryRequest.getQuery());
		Assertions.assertEquals(expectedQueryRequest.getSpatialFilter().getByWithinPolygon().getPoints().get(0).getLongitude(),
				actualQueryRequest.getSpatialFilter().getByWithinPolygon().getPoints().get(0).getLongitude(), 0d);
		Assertions.assertEquals(expectedQueryRequest.getSpatialFilter().getByWithinPolygon().getPoints().get(0).getLongitude(),
				actualQueryRequest.getSpatialFilter().getByWithinPolygon().getPoints().get(0).getLongitude(), 0d);
	}

	@Test
	void testSearchWithCursorPaginatesAllResults() throws SearchException {
		CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery = new CoordinateReferenceSystemsQuery();
		coordinateReferenceSystemsQuery.setIncludeDeprecated(true);
		coordinateReferenceSystemsQuery.setLimit(5000);

		Mockito.when(searchFactory.create(dpsHeaders)).thenReturn(searchService);
		searchWrapperService.postInit();

		Map<String, Object> record1 = new HashMap<>();
		record1.put("id", "record-1");
		Map<String, Object> record2 = new HashMap<>();
		record2.put("id", "record-2");

		CursorQueryResponse firstPage = new CursorQueryResponse();
		firstPage.setResults(Collections.singletonList(record1));
		firstPage.setTotalCount(2);
		firstPage.setCursor("cursor-page-2");

		CursorQueryResponse secondPage = new CursorQueryResponse();
		secondPage.setResults(Collections.singletonList(record2));
		secondPage.setTotalCount(2);
		secondPage.setCursor("");

		Mockito.when(searchService.searchCursor(Mockito.any()))
				.thenReturn(firstPage, secondPage);

		SearchResponse searchResponse = searchWrapperService.searchWithCursor(
				coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateReferenceSystemKind());

		Assertions.assertEquals(2, searchResponse.getCursorSearchResults().getResults().size());
		Mockito.verify(searchService, Mockito.times(2)).searchCursor(Mockito.any());
	}

	@Test
	void testSearchPaginatesAllResults() throws SearchException {
		CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery = new CoordinateReferenceSystemsQuery();
		coordinateReferenceSystemsQuery.setIncludeDeprecated(true);
		coordinateReferenceSystemsQuery.setLimit(5000);

		Mockito.when(searchFactory.create(dpsHeaders)).thenReturn(searchService);
		searchWrapperService.postInit();

		Map<String, Object> record1 = new HashMap<>();
		record1.put("id", "record-1");
		Map<String, Object> record2 = new HashMap<>();
		record2.put("id", "record-2");

		QueryResponse firstPage = new QueryResponse();
		firstPage.setResults(Collections.singletonList(record1));
		firstPage.setTotalCount(2);

		QueryResponse secondPage = new QueryResponse();
		secondPage.setResults(Collections.singletonList(record2));
		secondPage.setTotalCount(2);

		Mockito.when(searchService.search(Mockito.any()))
				.thenReturn(firstPage, secondPage);

		SearchResponse searchResponse = searchWrapperService.search(
				coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateReferenceSystemKind());

		Assertions.assertEquals(2, searchResponse.getSearchResults().getResults().size());
		Mockito.verify(searchService, Mockito.times(2)).search(Mockito.any());
	}

	@Test
	void testSearchStopsOnEmptyPage() throws SearchException {
		CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery = new CoordinateReferenceSystemsQuery();
		coordinateReferenceSystemsQuery.setIncludeDeprecated(true);
		coordinateReferenceSystemsQuery.setLimit(5000);

		Mockito.when(searchFactory.create(dpsHeaders)).thenReturn(searchService);
		searchWrapperService.postInit();

		Map<String, Object> record1 = new HashMap<>();
		record1.put("id", "record-1");

		QueryResponse firstPage = new QueryResponse();
		firstPage.setResults(Collections.singletonList(record1));
		firstPage.setTotalCount(5);

		QueryResponse emptyPage = new QueryResponse();
		emptyPage.setResults(Collections.emptyList());
		emptyPage.setTotalCount(5);

		Mockito.when(searchService.search(Mockito.any()))
				.thenReturn(firstPage, emptyPage);

		SearchResponse searchResponse = searchWrapperService.search(
				coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateReferenceSystemKind());

		Assertions.assertEquals(1, searchResponse.getSearchResults().getResults().size());
		Mockito.verify(searchService, Mockito.times(2)).search(Mockito.any());
	}

	@Test
	void testSearchWithCursorStopsOnEmptyPage() throws SearchException {
		CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery = new CoordinateReferenceSystemsQuery();
		coordinateReferenceSystemsQuery.setIncludeDeprecated(true);
		coordinateReferenceSystemsQuery.setLimit(5000);

		Mockito.when(searchFactory.create(dpsHeaders)).thenReturn(searchService);
		searchWrapperService.postInit();

		Map<String, Object> record1 = new HashMap<>();
		record1.put("id", "record-1");

		CursorQueryResponse firstPage = new CursorQueryResponse();
		firstPage.setResults(Collections.singletonList(record1));
		firstPage.setTotalCount(3);
		firstPage.setCursor("cursor-page-2");

		CursorQueryResponse emptyPage = new CursorQueryResponse();
		emptyPage.setResults(Collections.emptyList());
		emptyPage.setTotalCount(3);
		emptyPage.setCursor("");

		Mockito.when(searchService.searchCursor(Mockito.any()))
				.thenReturn(firstPage, emptyPage);

		SearchResponse searchResponse = searchWrapperService.searchWithCursor(
				coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateReferenceSystemKind());

		Assertions.assertEquals(1, searchResponse.getCursorSearchResults().getResults().size());
		Mockito.verify(searchService, Mockito.times(2)).searchCursor(Mockito.any());
	}

	@Test
	void testSearchDedupesSameRecordIdPreferringNewerKind() throws SearchException {
		CoordinateReferenceSystemsQuery coordinateReferenceSystemsQuery = new CoordinateReferenceSystemsQuery();
		coordinateReferenceSystemsQuery.setIncludeDeprecated(true);
		coordinateReferenceSystemsQuery.setLimit(5000);

		Mockito.when(searchFactory.create(dpsHeaders)).thenReturn(searchService);
		searchWrapperService.postInit();

		Map<String, Object> older = new HashMap<>();
		older.put("id", "osdu:reference-data--CoordinateTransformation:EPSG::1111");
		older.put("kind", "osdu:wks:reference-data--CoordinateTransformation:1.1.0");
		Map<String, Object> newer = new HashMap<>();
		newer.put("id", "osdu:reference-data--CoordinateTransformation:EPSG::1111");
		newer.put("kind", "osdu:wks:reference-data--CoordinateTransformation:1.2.0");

		QueryResponse page = new QueryResponse();
		page.setResults(Arrays.asList(older, newer));
		page.setTotalCount(2);

		Mockito.when(searchService.search(Mockito.any())).thenReturn(page);

		SearchResponse searchResponse = searchWrapperService.search(
				coordinateReferenceSystemsQuery, SearchWrapperService.getCoordinateTransformationKind());

		Assertions.assertEquals(1, searchResponse.getSearchResults().getResults().size());
		Assertions.assertEquals(
				"osdu:wks:reference-data--CoordinateTransformation:1.2.0",
				searchResponse.getSearchResults().getResults().get(0).get("kind"));
	}

	@Test
	void testCompareKindVersions() {
		Assertions.assertTrue(SearchWrapperService.compareKindVersions(
				"osdu:wks:reference-data--CoordinateTransformation:1.2.0",
				"osdu:wks:reference-data--CoordinateTransformation:1.1.0") > 0);
		Assertions.assertEquals(0, SearchWrapperService.compareKindVersions(
				"osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0",
				"osdu:wks:reference-data--CoordinateReferenceSystem:1.1.0"));
	}

	@Test
	void testDedupeByRecordIdKeepsDistinctIds() {
		Map<String, Object> a = new HashMap<>();
		a.put("id", "record-a");
		a.put("kind", "osdu:wks:x:1.0.0");
		Map<String, Object> b = new HashMap<>();
		b.put("id", "record-b");
		b.put("kind", "osdu:wks:x:1.0.0");
		Assertions.assertEquals(2, SearchWrapperService.dedupeByRecordId(Arrays.asList(a, b)).size());
	}

}
