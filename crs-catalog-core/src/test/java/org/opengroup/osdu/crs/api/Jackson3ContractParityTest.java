/*
 * Copyright 2017-2026, The Open Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opengroup.osdu.crs.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.core.common.http.HttpConfiguration;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.search.Point;
import org.opengroup.osdu.core.common.model.search.QueryResponse;
import org.opengroup.osdu.crs.logging.AuditLogger;
import org.opengroup.osdu.crs.model.TestUtils;
import org.opengroup.osdu.crs.model.request.AreaOfUseRequest;
import org.opengroup.osdu.crs.model.request.CRSRequest;
import org.opengroup.osdu.crs.model.request.CTRequest;
import org.opengroup.osdu.crs.model.request.CompoundCRSRequest;
import org.opengroup.osdu.crs.model.request.CompoundCTRequest;
import org.opengroup.osdu.crs.model.request.CoordinateReferenceSystemsQuery;
import org.opengroup.osdu.crs.model.request.CoordinateTransformationsQuery;
import org.opengroup.osdu.crs.model.request.EarlyBoundCRSRequest;
import org.opengroup.osdu.crs.model.request.InPolygonQuery;
import org.opengroup.osdu.crs.model.request.LateBoundCRSRequest;
import org.opengroup.osdu.crs.model.request.SearchRequest;
import org.opengroup.osdu.crs.model.request.SingleCTRequest;
import org.opengroup.osdu.crs.model.response.PointsInAouSearchPoint;
import org.opengroup.osdu.crs.model.response.PointsInAouSearchResult;
import org.opengroup.osdu.crs.model.response.SearchResponse;
import org.opengroup.osdu.crs.util.AppError;
import org.springframework.test.util.ReflectionTestUtils;
import tools.jackson.databind.json.JsonMapper;

/**
 * Spring Boot 4 writes and reads the API with the Jackson 3 mapper from os-core-common's {@link HttpConfiguration};
 * before, it was the Jackson 2 mapper configured below. This serializes real responses (built from the test catalog
 * through the controller, in every representation mode) and reads every request body with both, and fails on any
 * difference in content. Property order is not compared: Jackson 3 sorts alphabetically and JSON objects are
 * unordered.
 */
class Jackson3ContractParityTest {

    private static final com.fasterxml.jackson.databind.ObjectMapper JACKSON2 =
            new com.fasterxml.jackson.databind.ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .setTimeZone(TimeZone.getDefault());

    private static final JsonMapper JACKSON3 = new HttpConfiguration().jsonObjectMapper();

    private static CrsCatalogApi api;

    @BeforeAll
    static void setUp() throws Exception {
        api = new CrsCatalogApi(TestUtils.createTestCatalog("TestCatalog.json"));
        ReflectionTestUtils.setField(api, "auditLogger", mock(AuditLogger.class));
        ReflectionTestUtils.setField(api, "log", mock(JaxRsDpsLog.class));
    }

    @Test
    void responsesShouldHaveTheSameWireFormat() throws Exception {
        Map<String, Object> responses = new LinkedHashMap<>();
        responses.put("catalog", api.getCatalog());
        responses.put("catalogAttributes", api.getCatalogAttributes());
        for (String mode : List.of("persistableReference", "essence", "essence,persistableReference")) {
            responses.put("area " + mode, api.getAreasOfUse(0, 1000, mode));
            responses.put("crs " + mode, api.getAllCRSes(0, 1000, mode));
            responses.put("lateBoundCRS " + mode, api.getLateBoundCRSes(0, 1000, mode));
            responses.put("earlyBoundCRS " + mode, api.getEarlyBoundCRSes(0, 1000, mode));
            responses.put("compoundCRS " + mode, api.getCompoundCRSes(0, 1000, mode));
            responses.put("ct " + mode, api.getAllCTs(0, 1000, mode));
            responses.put("singleCT " + mode, api.getSingleCTs(0, 1000, mode));
            responses.put("compoundCT " + mode, api.getCompoundCTs(0, 1000, mode));
            responses.put("searchArea " + mode, api.searchAreasOfUse(new SearchRequest("*"), -180, -90, 180, 90, 0, 1000, mode));
            responses.put("searchCRS " + mode, api.searchCRSes(new SearchRequest("*"), -180, -90, 180, 90, 0, 1000, mode));
            responses.put("searchCT " + mode, api.searchCTs(new SearchRequest("*"), -180, -90, 180, 90, 0, 1000, mode));
        }
        responses.put("v3 searchResponse", v3SearchResponse());
        responses.put("v3 pointsInAou", pointsInAouResult());
        responses.put("error", AppError.builder().code(400).reason("Bad Request").message("message")
                .debuggingInfo("hidden").build());

        List<String> differences = new ArrayList<>();
        for (Map.Entry<String, Object> response : responses.entrySet()) {
            String jackson2 = JACKSON2.writeValueAsString(response.getValue());
            String jackson3 = JACKSON3.writeValueAsString(response.getValue());
            if (!JACKSON2.readTree(jackson2).equals(JACKSON2.readTree(jackson3))) {
                differences.add(response.getKey() + "\n  jackson2: " + abbreviate(jackson2)
                        + "\n  jackson3: " + abbreviate(jackson3));
            }
        }
        assertTrue(JACKSON2.writeValueAsString(responses.get("crs essence")).length() > 10_000,
                "Expected the test catalog to produce non-trivial responses");
        assertEquals(List.of(), differences, String.join("\n", differences));
    }

    @Test
    void requestBodiesShouldBeReadTheSameWay() throws Exception {
        String lateBound = TestUtils.readFile("LateBoundCRSEssence.json");
        String earlyBound = TestUtils.readFile("EarlyBoundCRSEssence.json");
        String compoundCrs = TestUtils.readFile("CompoundCRSEssence.json");
        String singleCt = TestUtils.readFile("SingleCTEssence.json");
        String compoundCt = TestUtils.readFile("CompoundCTEssence.json");
        String area = TestUtils.readFile("AreaOfUseEssence.json");

        List<Map.Entry<String, Class<?>>> bodies = new ArrayList<>();
        bodies.add(Map.entry(essence(lateBound), LateBoundCRSRequest.class));
        bodies.add(Map.entry(persistableReference(lateBound), LateBoundCRSRequest.class));
        bodies.add(Map.entry(essence(earlyBound), EarlyBoundCRSRequest.class));
        bodies.add(Map.entry(essence(compoundCrs), CompoundCRSRequest.class));
        bodies.add(Map.entry(persistableReference(compoundCrs), CompoundCRSRequest.class));
        bodies.add(Map.entry(essence(lateBound), CRSRequest.class));
        bodies.add(Map.entry(essence(compoundCrs), CRSRequest.class));
        bodies.add(Map.entry(essence(singleCt), SingleCTRequest.class));
        bodies.add(Map.entry(essence(compoundCt), CompoundCTRequest.class));
        bodies.add(Map.entry(essence(singleCt), CTRequest.class));
        bodies.add(Map.entry(essence(area), AreaOfUseRequest.class));
        bodies.add(Map.entry("{\"query\":\"name:*America*\",\"unknown\":1}", SearchRequest.class));
        bodies.add(Map.entry("{\"codeSpace\":\"EPSG\",\"code\":\"4326\",\"returnBoundGeographic2DAndWgs84\":true,"
                + "\"baseCRS\":{\"id\":\"x\"},\"datum\":{\"name\":\"WGS 84\"},\"extent\":{\"name\":\"World\"},"
                + "\"latitude\":1.5,\"longitude\":2.5,\"offset\":10,\"limit\":20,\"returnedFields\":[\"id\"]}", CoordinateReferenceSystemsQuery.class));
        bodies.add(Map.entry("{\"kind\":\"All\",\"sourceCRS\":\"a\",\"targetCRS\":\"b\",\"includeDeprecated\":true,"
                + "\"returnAllFields\":true}", CoordinateTransformationsQuery.class));
        bodies.add(Map.entry("{\"recordId\":\"r\",\"dataId\":\"d\",\"points\":[{\"latitude\":1.0,\"longitude\":2.0}]}", InPolygonQuery.class));

        List<String> differences = new ArrayList<>();
        for (Map.Entry<String, Class<?>> body : bodies) {
            String expected = JACKSON2.writeValueAsString(JACKSON2.readValue(body.getKey(), body.getValue()));
            String actual = JACKSON2.writeValueAsString(JACKSON3.readValue(body.getKey(), body.getValue()));
            if (!JACKSON2.readTree(expected).equals(JACKSON2.readTree(actual))) {
                differences.add(body.getValue().getSimpleName() + "\n  jackson2: " + abbreviate(expected)
                        + "\n  jackson3: " + abbreviate(actual));
            }
        }
        assertEquals(List.of(), differences, String.join("\n", differences));
    }

    private static String essence(String json) {
        return "{\"essence\":" + json + "}";
    }

    private static String persistableReference(String json) throws Exception {
        return "{\"persistableReference\":" + JACKSON2.writeValueAsString(json) + "}";
    }

    private static SearchResponse v3SearchResponse() {
        Map<String, Object> data = new HashMap<>();
        data.put("Name", "WGS 84");
        data.put("InactiveIndicator", null);
        Map<String, Object> record = new HashMap<>();
        record.put("id", "osdu:reference-data--CoordinateReferenceSystem:Geographic2D:EPSG::4326");
        record.put("data", data);
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setResults(List.of(record));
        queryResponse.setTotalCount(1);
        return new SearchResponse(queryResponse, "data.Code:\"4326\"");
    }

    private static PointsInAouSearchResult pointsInAouResult() {
        PointsInAouSearchPoint point = new PointsInAouSearchPoint();
        point.setPoint(new Point(1.0, 2.0));
        point.setIndex(0);
        point.setApproximateKmDistanceOutside(12);
        PointsInAouSearchResult result = new PointsInAouSearchResult();
        result.addBboxFailedPoint(point);
        result.setMaxDistKmOutsideBBox(12);
        return result;
    }

    private static String abbreviate(String json) {
        return json.length() > 2000 ? json.substring(0, 2000) + "..." : json;
    }
}
