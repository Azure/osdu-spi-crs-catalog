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

package org.opengroup.osdu.crs.swagger;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opengroup.osdu.core.common.info.VersionInfoBuilder;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.crs.api.CrsCatalogApi;
import org.opengroup.osdu.crs.api.CrsCatalogApiV3;
import org.opengroup.osdu.crs.api.HealthCheck;
import org.opengroup.osdu.crs.api.InfoApi;
import org.opengroup.osdu.crs.api.InfoApiV3;
import org.opengroup.osdu.crs.logging.AuditLogger;
import org.opengroup.osdu.crs.middleware.GlobalExceptionMapper;
import org.opengroup.osdu.crs.model.interfaces.Catalog;
import org.opengroup.osdu.crs.service.PointsInAouService;
import org.opengroup.osdu.crs.service.SearchWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = OpenApiSecurityTest.TestConfiguration.class)
@TestPropertySource("classpath:swagger.properties")
@MockitoBean(types = {
        VersionInfoBuilder.class, Catalog.class, AuditLogger.class, JaxRsDpsLog.class,
        SearchWrapperService.class, PointsInAouService.class
})
class OpenApiSecurityTest {

    @Autowired
    private WebApplicationContext context;

    @ParameterizedTest
    @CsvSource({
            "v2, /v2/catalog",
            "v3, /v3/coordinate-reference-system"
    })
    void shouldGeneratePublicInfoOperationsWithoutRemovingCatalogSecurity(
            String group, String protectedPath) throws Exception {
        MockMvcBuilders.webAppContextSetup(context).build()
                .perform(get("/api-docs/" + group).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.security[0].Authorization").isArray())
                .andExpect(jsonPath("$.components.securitySchemes.Authorization.type").value("http"))
                .andExpect(jsonPath("$.components.securitySchemes.Authorization.scheme").value("bearer"))
                .andExpect(jsonPath("$.paths['/" + group + "/info'].get.security").isArray())
                .andExpect(jsonPath("$.paths['/" + group + "/info'].get.security").isEmpty())
                .andExpect(jsonPath("$.paths['/" + group + "/info'].get.parameters").doesNotExist())
                .andExpect(jsonPath("$.paths['" + protectedPath + "'].get.security[0].Authorization").isArray())
                .andExpect(jsonPath("$.paths['" + protectedPath
                        + "'].get.parameters[?(@.name == 'data-partition-id' && @.in == 'header')].required")
                        .value(contains(true)));
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    @Import({
            SwaggerConfiguration.class, SwaggerConfigurationProperties.class,
            InfoApi.class, InfoApiV3.class, CrsCatalogApi.class, CrsCatalogApiV3.class,
            HealthCheck.class, GlobalExceptionMapper.class
    })
    static class TestConfiguration {
    }
}
