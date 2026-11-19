package org.opengroup.osdu.crs.api;

import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.crs.middleware.GlobalExceptionMapper;
import org.opengroup.osdu.crs.model.interfaces.Catalog;
import org.opengroup.osdu.crs.model.request.SearchRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchRequestTypeTest {
    @Test
    void allSearchRoutesRejectNonObjectBodiesBeforeCallingCatalog() throws Exception {
        Catalog catalog = mock(Catalog.class);
        var mvc = MockMvcBuilders.standaloneSetup(new CrsCatalogApi(catalog))
                .setControllerAdvice(new GlobalExceptionMapper()).build();
        for (String route : new String[]{"area", "crs", "ct"}) {
            for (String body : new String[]{"\"AAA\"", "\"\"", "false", "true", "0", "1.5", "[]", "null"}) {
                mvc.perform(post("/v2/search/" + route)
                                .contentType(MediaType.APPLICATION_JSON).content(body))
                        .andExpect(status().isBadRequest());
            }
        }
        verifyNoInteractions(catalog);
    }

    @Test
    void allSearchRoutesRejectNonStringNonNullQueriesBeforeCallingCatalog() throws Exception {
        Catalog catalog = mock(Catalog.class);
        var mvc = MockMvcBuilders.standaloneSetup(new CrsCatalogApi(catalog))
                .setControllerAdvice(new GlobalExceptionMapper()).build();
        for (String route : new String[]{"area", "crs", "ct"}) {
            for (String value : new String[]{"false", "true", "0", "1.5", "[]", "{}"}) {
                mvc.perform(post("/v2/search/" + route)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"query\":" + value + "}"))
                        .andExpect(status().isBadRequest());
            }
        }
        verifyNoInteractions(catalog);
    }

    @Test
    void preservesNullOmittedEmptyAndStringQueries() throws Exception {
        JsonMapper mapper = new JsonMapper();
        assertNull(mapper.readValue("{\"query\":null}", SearchRequest.class).getQuery());
        assertNull(mapper.readValue("{}", SearchRequest.class).getQuery());
        assertEquals("", mapper.readValue("{\"query\":\"\"}", SearchRequest.class).getQuery());
        assertEquals("name:WGS", mapper.readValue("{\"query\":\"name:WGS\"}", SearchRequest.class).getQuery());
    }
}
