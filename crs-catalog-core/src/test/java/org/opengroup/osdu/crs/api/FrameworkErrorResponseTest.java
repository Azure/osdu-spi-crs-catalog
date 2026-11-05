package org.opengroup.osdu.crs.api;

import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.crs.middleware.GlobalExceptionMapper;
import org.opengroup.osdu.crs.model.request.InPolygonQuery;
import org.opengroup.osdu.crs.model.request.SearchRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FrameworkErrorResponseTest {
    @Test
    void frameworkErrorsUseAppErrorForProblemJson() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new Controller())
                .setControllerAdvice(new GlobalExceptionMapper()).build();
        assertAppError(mvc, get("/parameter"), 400);
        assertAppError(mvc, get("/parameter").param("offset", "2147483648"), 400);
        assertAppError(mvc, post("/body").contentType(MediaType.APPLICATION_JSON).content("{"), 400);
        assertAppError(mvc, post("/points").contentType(MediaType.APPLICATION_JSON).content("{}"), 400);
        assertAppError(mvc, post("/body").content("{}"), 415);
        assertAppError(mvc, get("/missing"), 404);
        mvc.perform(post("/parameter"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().exists("Allow"))
                .andExpect(jsonPath("$.code").value(405));
    }

    private void assertAppError(MockMvc mvc, MockHttpServletRequestBuilder request, int code) throws Exception {
        mvc.perform(request)
                .andExpect(status().is(code))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.code").value(code))
                .andExpect(jsonPath("$.reason").isString())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.type").doesNotExist())
                .andExpect(jsonPath("$.title").doesNotExist());
    }

    @RestController
    static class Controller {
        @GetMapping("/parameter")
        int parameter(@RequestParam("offset") int offset) { return offset; }

        @PostMapping(value = "/body", consumes = MediaType.APPLICATION_JSON_VALUE)
        SearchRequest body(@RequestBody SearchRequest request) { return request; }

        @PostMapping(value = "/points", consumes = MediaType.APPLICATION_JSON_VALUE)
        InPolygonQuery points(@Valid @RequestBody InPolygonQuery request) { return request; }
    }
}
