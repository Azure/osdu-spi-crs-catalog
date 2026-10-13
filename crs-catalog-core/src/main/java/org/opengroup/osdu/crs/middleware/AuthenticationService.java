package org.opengroup.osdu.crs.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.opengroup.osdu.core.common.entitlements.EntitlementsAPIConfig;
import org.opengroup.osdu.core.common.entitlements.EntitlementsFactory;
import org.opengroup.osdu.core.common.entitlements.IEntitlementsService;
import org.opengroup.osdu.core.common.http.json.HttpResponseBodyMapper;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.entitlements.EntitlementsException;
import org.opengroup.osdu.core.common.model.entitlements.Groups;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.crs.util.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    @Inject
    private JaxRsDpsLog logger;

    @Inject
    private HandlerExceptionResolver handlerExceptionResolver;

    @Value("${osdu.entitlement.url}")
    private String entitlementsUrl;

    private EntitlementsFactory entitlementsFactory;

    @PostConstruct
    public void initEntitlementsFactory() {
        HttpResponseBodyMapper httpResponseBodyMapper = new HttpResponseBodyMapper(new ObjectMapper());
        EntitlementsAPIConfig config = EntitlementsAPIConfig.builder().rootUrl(entitlementsUrl).build();
        entitlementsFactory = new EntitlementsFactory(config, httpResponseBodyMapper);
    }


    public boolean isAuthorized(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        MultiValueMap<String, String> requestHeaders = httpHeaders(httpServletRequest);
        DpsHeaders dpsHeaders = DpsHeaders.createFromEntrySet(requestHeaders.entrySet());
        dpsHeaders.addCorrelationIdIfMissing();
        IEntitlementsService service = entitlementsFactory.create(dpsHeaders);
        try {
            Groups groups = service.getGroups();
            String message = "User authenticated | User: %s".formatted(groups.getMemberEmail());
            logger.info(message);
            putAuthenticationIntoContext(groups);
            httpServletResponse.addHeader(DpsHeaders.CORRELATION_ID, dpsHeaders.getCorrelationId());
        } catch (EntitlementsException e) {
            String message = "User not authenticated. Response: %s".formatted(e.getHttpResponse()).formatted(e);
            logger.warning(message);
            AppException unauthorized =  AppException.createUnauthorized("Error: " + e.getMessage());
            handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, unauthorized);
            return false;
        }
        catch (NullPointerException e) { // Common library throws null pointer exception when auth permission is denied.
            String message = "User not authenticated. Null pointer exception: %s".formatted(e.getMessage());
            logger.warning(message);
            AppException unauthorized = AppException.createUnauthorized("Error: " + e.getMessage());
            handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, unauthorized);
            return false;
        }
        return true;
    }

    private HttpHeaders httpHeaders(@NonNull HttpServletRequest httpRequest) {
        return Collections
                .list(httpRequest.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h -> Collections.list(httpRequest.getHeaders(h)),
                        (oldValue, newValue) -> newValue,
                        HttpHeaders::new
                ));
    }

    private void putAuthenticationIntoContext(Groups groups) {
        AuthenticationToken authentication = new AuthenticationToken(groups, Collections.emptyList());
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
