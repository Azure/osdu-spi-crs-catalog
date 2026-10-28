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

package org.opengroup.osdu.crs.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class WebConfigurationTest {

    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new ProbeController())
            .addFilters(new WebConfiguration().trailingSlashFilter().getFilter())
            .build();

    @ParameterizedTest
    @ValueSource(strings = {"/v3/coordinate-transformation", "/v3/coordinate-transformation/"})
    void shouldPreservePostBodyWithEitherTrailingSlash(String path) throws Exception {
        mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content("{\"operation\":\"x\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("post:{\"operation\":\"x\"}"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/v3/coordinate-reference-system", "/v3/coordinate-reference-system/"})
    void shouldPreserveQueryWithEitherTrailingSlash(String path) throws Exception {
        mvc.perform(get(path).queryParam("dataId", "EPSG::4326"))
                .andExpect(status().isOk())
                .andExpect(content().string("get:EPSG::4326"));
    }

    @RestController
    @RequestMapping("/v3")
    static class ProbeController {
        @PostMapping("/coordinate-transformation")
        public String post(@RequestBody String body) {
            return "post:" + body;
        }

        @GetMapping("/coordinate-reference-system")
        public String get(@RequestParam("dataId") String dataId) {
            return "get:" + dataId;
        }
    }
}
