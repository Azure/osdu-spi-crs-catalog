package org.opengroup.osdu.crs.swagger;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.opengroup.osdu.crs.api.CrsCatalogApiV3;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.opengroup.osdu.core.common.openapi.OpenApiContractProperties;
import org.opengroup.osdu.crs.api.CrsCatalogApi;
import org.opengroup.osdu.crs.api.HealthCheck;
import org.opengroup.osdu.crs.api.InfoApi;
import org.opengroup.osdu.crs.api.InfoApiV3;
import org.opengroup.osdu.crs.model.request.CoordinateReferenceSystemsQuery;
import org.opengroup.osdu.crs.model.request.CoordinateTransformationsQuery;
import org.opengroup.osdu.crs.model.request.InPolygonQuery;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.method.HandlerMethod;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigurationTest {
    @Test
    void v3SchemasUseJsonScalarTypesAndAnObjectExtent() throws Exception {
        var converters = new ModelConverters(true);
        for (Class<?> type : new Class<?>[]{
                CoordinateReferenceSystemsQuery.class,
                CoordinateTransformationsQuery.class,
                InPolygonQuery.class}) {
            var schema = converters.read(type).get(type.getSimpleName());
            var properties = Json31.mapper().valueToTree(schema).path("properties");
            assertEquals("integer", properties.path("offset").path("type").asText());
            assertEquals("integer", properties.path("limit").path("type").asText());
            if (type != InPolygonQuery.class) {
                assertEquals("number", properties.path("latitude").path("type").asText());
                assertEquals("boolean", properties.path("includeDeprecated").path("type").asText());
                assertEquals("boolean", properties.path("returnAllFields").path("type").asText());
            }
            if (type == CoordinateReferenceSystemsQuery.class) {
                assertEquals("#/components/schemas/Extent", properties.path("extent").path("$ref").asText());
            }
        }
    }

    @Test
    void declaresFrameworkErrorsOnTheAffectedEndpoints() {
        for (Class<?> controller : new Class<?>[]{CrsCatalogApi.class, CrsCatalogApiV3.class}) {
            for (var method : controller.getDeclaredMethods()) {
                var responses = method.getAnnotation(ApiResponses.class);
                if (responses == null) {
                    continue;
                }
                boolean post = method.isAnnotationPresent(PostMapping.class);
                var byCode = Arrays.stream(responses.value()).collect(Collectors.toMap(
                        io.swagger.v3.oas.annotations.responses.ApiResponse::responseCode, response -> response));
                assertEquals(post, byCode.containsKey("415"), method.getName());
                for (String code : post ? new String[]{"400", "415"} : new String[]{"400"}) {
                    assertTrue(Arrays.stream(byCode.get(code).content()).anyMatch(content ->
                            MediaType.APPLICATION_PROBLEM_JSON_VALUE.equals(content.mediaType())
                                    && "AppError".equals(content.schema().implementation().getSimpleName())),
                            method.getName() + " HTTP " + code);
                }
            }
        }
    }
    @Test
    void requiresPartitionOnlyForProtectedOperations() throws Exception {
        SwaggerConfiguration config = new SwaggerConfiguration();
        var customizer = config.operationCustomizer(new DefaultListableBeanFactory()
                .getBeanProvider(OpenApiContractProperties.class));
        for (Class<?> type : new Class<?>[]{InfoApi.class, InfoApiV3.class}) {
            Operation info = customizer.customize(new Operation(),
                    new HandlerMethod(type.getConstructor().newInstance(), type.getMethod("info")));
            assertNull(info.getParameters());
        }
        for (String methodName : new String[]{"livenessCheck", "readinessCheck"}) {
            Operation health = customizer.customize(new Operation(), new HandlerMethod(
                    HealthCheck.class.getConstructor().newInstance(), HealthCheck.class.getMethod(methodName)));
            assertNull(health.getParameters());
        }
        Operation catalog = customizer.customize(new Operation(), new HandlerMethod(
                new CrsCatalogApi(null), CrsCatalogApi.class.getMethod("getCatalog")));
        assertEquals("data-partition-id", catalog.getParameters().get(0).getName());
        assertTrue(catalog.getParameters().get(0).getRequired());
    }
}
