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
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.core.common.model.search.*;
import org.opengroup.osdu.core.common.search.ISearchFactory;
import org.opengroup.osdu.core.common.search.ISearchService;
import org.opengroup.osdu.crs.model.request.ISearchQuery;
import org.opengroup.osdu.crs.model.response.SearchResponse;
import org.opengroup.osdu.crs.util.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchWrapperService {

    @Inject
    ISearchFactory searchFactory;

    @Inject
    DpsHeaders dpsHeaders;

    @Inject
    JaxRsDpsLog logger;

    ISearchService searchService;

    private static String schemaAuthority;

    public static String getCoordinateReferenceSystemKind() {
        return "%s:wks:reference-data--CoordinateReferenceSystem:1.*.*".formatted(schemaAuthority);
    }

    public static String getCoordinateTransformationKind() {
        return "%s:wks:reference-data--CoordinateTransformation:1.*.*".formatted(schemaAuthority);
    }

    public static String getCtAndCrsKind() {
        return "%s:wks:reference-data--*:1.*.*".formatted(schemaAuthority);
    }

    @PostConstruct
    public void postInit() {
        searchService = searchFactory.create(dpsHeaders);
    }

    @Value("${osdu.crs.catalog.schema-authority:osdu}")
    public void setSchemaAuthority(String privateName) {
        SearchWrapperService.schemaAuthority = privateName;
    }

    public SearchResponse getSingleCrsOrCt(String recordId, String dataId, String kind) {
        String query;
        if (recordId != null && dataId != null) {
            query = "id: \"%s\" && data.ID: \"%s\"".formatted(recordId, dataId);
        } else if (recordId != null) {
            query = "id: \"%s\"".formatted(recordId);
        } else if (dataId != null) {
            query = "data.ID: \"%s\"".formatted(dataId);
        } else {
            throw AppException.createBadRequest("Must supply either recordId or dataId as request param");
        }

        QueryRequest queryRequest = new QueryRequest();

        queryRequest.setQuery(query);
        queryRequest.setKind(kind);

        return new SearchResponse(sendToSearch(queryRequest), query);
    }

    public SearchResponse search(ISearchQuery searchQuery, String kind) {
        QueryRequest queryRequest = new QueryRequest();

        String query = searchQuery.constructQuery();
        queryRequest.setQuery(query);
        queryRequest.setKind(kind);

        List<String> returnedFields = searchQuery.getReturnedFields();
        if (returnedFields.size() > 0) {
            queryRequest.setReturnedFields(returnedFields);
        }

        SpatialFilter spatialFilter = searchQuery.constructSpatialFilter();
        if (spatialFilter != null) {
            queryRequest.setSpatialFilter(searchQuery.constructSpatialFilter());
        }

        if (searchQuery.getLimit() != null) {
            queryRequest.setLimit(searchQuery.getLimit());
        }

        if (searchQuery.getOffset() != null) {
            queryRequest.setFrom(searchQuery.getOffset());
        }

        return new SearchResponse(sendToSearch(queryRequest), query);
    }
    public SearchResponse searchWithCursor(ISearchQuery searchQuery, String kind) {
        CursorQueryRequest cursorqueryRequest = new CursorQueryRequest ();

        String query = searchQuery.constructQuery();
        cursorqueryRequest.setQuery(query);
        cursorqueryRequest.setKind(kind);

        List<String> returnedFields = searchQuery.getReturnedFields();
        if (returnedFields.size() > 0) {
            cursorqueryRequest.setReturnedFields(returnedFields);
        }

        SpatialFilter spatialFilter = searchQuery.constructSpatialFilter();
        if (spatialFilter != null) {
            cursorqueryRequest.setSpatialFilter(searchQuery.constructSpatialFilter());
        }

        if (searchQuery.getLimit() != null) {
            cursorqueryRequest.setLimit(searchQuery.getLimit());
        }

        return new SearchResponse(sendToSearchWithCursor(cursorqueryRequest), query);
    }
    private QueryResponse sendToSearch(QueryRequest queryRequest) {
        QueryResponse queryResponse = null;
        try {
            logger.debug("Sending query to search service: %s".formatted(queryRequest.toString()));
            queryResponse = searchService.search(queryRequest);
            List<Map<String, Object>> results = queryResponse.getResults();
            List<Map<String, Object>> searchResultList = results != null ? new ArrayList<>(results) : new ArrayList<>();
            if (queryRequest.getFrom() <= 0) {
                int default_Count = searchResultList.size();
                long totalCount = queryResponse.getTotalCount();
                int resultLimit = queryRequest.getLimit() > 0
                        ? queryRequest.getLimit()
                        : Integer.MAX_VALUE;
                while (default_Count < totalCount && default_Count < resultLimit) {
                    queryRequest.setFrom(default_Count);
                    queryResponse = searchService.search(queryRequest);
                    List<Map<String, Object>> pageResults = queryResponse.getResults();
                    if (pageResults.isEmpty()) {
                        break;
                    }
                    searchResultList.addAll(pageResults);
                    default_Count += pageResults.size();
                }
            }
            queryResponse.setResults(dedupeByRecordId(searchResultList));
            logger.debug("Received response from search service: %s".formatted(queryResponse.toString()));
        } catch (SearchException e) {
            handleSearchError("Failed to call search service", e);
        }
        return queryResponse;
    }
    private CursorQueryResponse sendToSearchWithCursor(CursorQueryRequest queryRequest) {
        CursorQueryResponse cursorqueryResponse = null;
        try {
            logger.debug("Sending query to search service: %s".formatted(queryRequest.toString()));
           
            cursorqueryResponse = searchService.searchCursor(queryRequest);
            List<Map<String, Object>> results = cursorqueryResponse.getResults();
            List<Map<String, Object>> searchResultList = results != null ? new ArrayList<>(results) : new ArrayList<>();
            String cursor_value = cursorqueryResponse.getCursor();
            int default_Count = searchResultList.size();
            long totalCount = cursorqueryResponse.getTotalCount();
            int resultLimit = queryRequest.getLimit() > 0
                    ? queryRequest.getLimit()
                    : Integer.MAX_VALUE;
            while (default_Count < totalCount && default_Count < resultLimit) {
                queryRequest.setCursor(cursor_value);
                cursorqueryResponse = searchService.searchCursor(queryRequest);
                List<Map<String, Object>> pageResults = cursorqueryResponse.getResults();
                if (pageResults.isEmpty()) {
                    break;
                }
                searchResultList.addAll(pageResults);
                default_Count += pageResults.size();
                cursor_value = cursorqueryResponse.getCursor();
            }
            cursorqueryResponse.setResults(dedupeByRecordId(searchResultList));
            logger.debug("Received response from search service: %s".formatted(cursorqueryResponse.toString()));
        } catch (SearchException e) {
            handleSearchError("Failed to call search service", e);
        }
        return cursorqueryResponse;
    }

    /**
     * Kind wildcards (1.*.*) can return the same record id under multiple schema
     * versions. Keep one row per id, preferring the higher kind version.
     */
    static List<Map<String, Object>> dedupeByRecordId(List<Map<String, Object>> results) {
        Map<String, Map<String, Object>> byId = new LinkedHashMap<>();
        for (Map<String, Object> row : results) {
            if (row == null) {
                continue;
            }
            Object idObj = row.get("id");
            if (!(idObj instanceof String id) || id.isEmpty()) {
                byId.put("__anon_" + byId.size(), row);
                continue;
            }
            Map<String, Object> existing = byId.get(id);
            if (existing == null || preferIncomingKind(row, existing)) {
                byId.put(id, row);
            }
        }
        return new ArrayList<>(byId.values());
    }

    private static boolean preferIncomingKind(Map<String, Object> incoming, Map<String, Object> existing) {
        return compareKindVersions(asString(incoming.get("kind")), asString(existing.get("kind"))) > 0;
    }

    private static String asString(Object value) {
        return value instanceof String s ? s : "";
    }

    /**
     * Compare trailing major.minor.patch of OSDU kinds. Returns positive when
     * {@code left} is newer than {@code right}.
     */
    static int compareKindVersions(String leftKind, String rightKind) {
        int[] left = parseKindVersion(leftKind);
        int[] right = parseKindVersion(rightKind);
        for (int i = 0; i < 3; i++) {
            if (left[i] != right[i]) {
                return Integer.compare(left[i], right[i]);
            }
        }
        return 0;
    }

    private static int[] parseKindVersion(String kind) {
        int[] version = {0, 0, 0};
        if (kind == null || kind.isEmpty()) {
            return version;
        }
        int lastColon = kind.lastIndexOf(':');
        if (lastColon < 0 || lastColon == kind.length() - 1) {
            return version;
        }
        String[] parts = kind.substring(lastColon + 1).split("\\.");
        for (int i = 0; i < Math.min(3, parts.length); i++) {
            try {
                version[i] = Integer.parseInt(parts[i]);
            } catch (NumberFormatException ignored) {
                version[i] = 0;
            }
        }
        return version;
    }

    private void handleSearchError(String errorMsg, SearchException e) {
        logger.error(errorMsg, e);
        e.printStackTrace();

        int responseCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (e.getResponse() != null) {
            int originalResponseCode = e.getResponse().getResponseCode();
            if (originalResponseCode == HttpStatus.TOO_MANY_REQUESTS.value()) {
                responseCode = originalResponseCode;
            }
        }

        throw new AppException(responseCode, errorMsg, e.getMessage());
    }
}
