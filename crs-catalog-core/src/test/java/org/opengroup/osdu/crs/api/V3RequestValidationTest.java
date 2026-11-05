package org.opengroup.osdu.crs.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.opengroup.osdu.crs.middleware.GlobalExceptionMapper;
import org.opengroup.osdu.crs.service.PointsInAouService;
import org.opengroup.osdu.crs.service.SearchWrapperService;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class V3RequestValidationTest {
    @Mock SearchWrapperService search;
    @Mock PointsInAouService points;
    @InjectMocks CrsCatalogApiV3 controller;
    MockMvc mvc;
    ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        var converter = new MappingJackson2HttpMessageConverter(mapper);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionMapper())
                .setMessageConverters(converter).build();
    }

    @Test
    void rejectsReportedNullExtentDescription() throws Exception {
        reject("coordinate-reference-system", """
                {"codeSpace":"","code":"","name":"","id":"","kind":"",
                 "coordinateReferenceSystemType":"","returnBoundProjectedAndProjectedBasedOnWgs84":false,
                 "returnBoundGeographic2DAndWgs84":false,"baseCRS":{"id":"","name":""},
                 "datum":{"name":"","codeSpace":"","code":""},"extent":{"name":"","description":null},
                 "persistableReferenceSearch":"","horizontalAxisUnitId":"","verticalAxisUnitId":"",
                 "latitude":0,"longitude":0,"includeDeprecated":false,"offset":0,"limit":0,
                 "returnAllFields":false,"returnedFields":[]}
                """);
        verifyNoInteractions(search, points);
    }

    @Test
    void rejectsInvalidNestedStringFieldsBeforeCallingSearch() throws Exception {
        for (String[] fields : new String[][]{{"extent", "name", "description"},
                {"baseCRS", "id", "name"}, {"datum", "name", "codeSpace", "code"}}) {
            for (int i = 1; i < fields.length; i++) {
                for (String value : new String[]{"null", "false", "1", "1.5", "[]", "{}"}) {
                    reject("coordinate-reference-system",
                            "{\"" + fields[0] + "\":{\"" + fields[i] + "\":" + value + "}}");
                }
            }
        }
        verifyNoInteractions(search, points);
    }

    @Test
    void acceptsOmittedAndStringNestedFields() throws Exception {
        for (String body : new String[]{"{\"extent\":{},\"baseCRS\":{},\"datum\":{}}",
                "{\"extent\":{\"name\":\"\",\"description\":\"Europe\"},\"baseCRS\":{\"id\":\"\",\"name\":\"WGS84\"},\"datum\":{\"name\":\"WGS84\",\"codeSpace\":\"EPSG\",\"code\":\"6326\"}}"}) {
            mvc.perform(post("/v3/coordinate-reference-system").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isOk());
        }
        verify(search, times(2)).searchWithCursor(any(), any());
    }

    @Test
    void rejectsEmptyStringsAndNullsInTypedFieldsBeforeQueryConstruction() throws Exception {
        for (String route : new String[]{"coordinate-reference-system", "coordinate-transformation"}) {
            for (String field : new String[]{"includeDeprecated", "returnAllFields", "offset", "limit", "latitude", "longitude"}) {
                for (String value : new String[]{"\"\"", "\"false\"", "null"}) {
                    reject(route, "{\"" + field + "\":" + value + "}");
                }
            }
            reject(route, "{\"returnedFields\":[null]}");
            reject(route, "{\"kind\":null}");
            reject(route, "null");
            reject(route, "");
        }
        verifyNoInteractions(search, points);
    }

    @Test
    void rejectsInvalidScalarTypesWithAnUnmodifiedMapper() throws Exception {
        reject("coordinate-transformation", "{\"limit\":1.5}");
        reject("coordinate-transformation", "{\"includeDeprecated\":0}");
        reject("coordinate-transformation", "{\"code\":true}");
        reject("coordinate-transformation", "{\"returnedFields\":[false]}");
        reject("coordinate-reference-system", "{\"returnedFields\":[42]}");
        verifyNoInteractions(search, points);
        assertEquals("42", mapper.readValue("{\"value\":42}", UnrelatedRequest.class).value);
        assertEquals(10, mapper.readValue("{\"limit\":\"10\"}", UnrelatedRequest.class).limit);
    }

    static class UnrelatedRequest {
        public String value;
        public int limit;
    }

    @Test
    void acceptsTypedBooleansAndNumbersAndPreservesDefaults() throws Exception {
        for (String route : new String[]{"coordinate-reference-system", "coordinate-transformation"}) {
            mvc.perform(post("/v3/" + route).contentType(MediaType.APPLICATION_JSON)
                    .content("{\"includeDeprecated\":false,\"returnAllFields\":true,\"offset\":0,\"limit\":10,\"latitude\":1.5,\"longitude\":2}"))
                    .andExpect(status().isOk());
            mvc.perform(post("/v3/" + route).contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isOk());
        }
        verify(search, times(2)).search(any(), any());
        verify(search, times(2)).searchWithCursor(any(), any());
    }

    @Test
    void pointsRequestsNeedAnIdentifierAndPointsBeforeCallingSearch() throws Exception {
        for (String body : new String[]{"{}", "{\"recordId\":\"\",\"dataId\":\"\",\"points\":[]}",
                "{\"recordId\":\"record\",\"points\":[]}",
                "{\"recordId\":\" \",\"points\":[{\"latitude\":1,\"longitude\":2}]}",
                "{\"recordId\":\"record\",\"points\":[null]}"}) {
            reject("points-in-aou", body);
        }
        verifyNoInteractions(search, points);
    }

    @Test
    void acceptsPointsWithEitherIdentifier() throws Exception {
        for (String field : new String[]{"recordId", "dataId"}) {
            mvc.perform(post("/v3/points-in-aou").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"" + field + "\":\"record\",\"points\":[{\"latitude\":1,\"longitude\":2}]}"))
                    .andExpect(status().isOk());
        }
        verify(points, times(2)).searchPointsInAou(any());
    }

    @Test
    void validatesCoordinateTokensOnlyForPointsInAouRequests() throws Exception {
        for (String coordinate : new String[]{"latitude", "longitude"}) {
            String other = coordinate.equals("latitude") ? "longitude" : "latitude";
            for (String value : new String[]{"null", "\"1\"", "false", "{}", "[]"}) {
                reject("points-in-aou", "{\"recordId\":\"record\",\"points\":[{\""
                        + coordinate + "\":" + value + ",\"" + other + "\":1}]}");
            }
        }
        reject("points-in-aou", "{\"recordId\":\"record\",\"points\":[{}]}");
        verifyNoInteractions(search, points);
    }

    private void reject(String route, String body) throws Exception {
        mvc.perform(post("/v3/" + route).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }
}
