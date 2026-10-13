package org.opengroup.osdu.crs.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfigurationProperties {

    private String apiTitle;
    private String apiDescription;
    private String apiVersion;
    private String apiContactName;
    private String apiContactEmail;
    private String apiLicenseName;
    private String apiLicenseUrl;
    private boolean apiServerFullUrlEnabled;

}