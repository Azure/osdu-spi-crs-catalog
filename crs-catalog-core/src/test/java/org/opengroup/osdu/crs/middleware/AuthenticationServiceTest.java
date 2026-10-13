package org.opengroup.osdu.crs.middleware;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.opengroup.osdu.core.common.entitlements.EntitlementsFactory;
import org.opengroup.osdu.core.common.entitlements.IEntitlementsService;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.core.common.model.entitlements.EntitlementsException;
import org.opengroup.osdu.core.common.model.entitlements.Groups;
import org.opengroup.osdu.core.common.model.http.DpsHeaders;
import org.opengroup.osdu.crs.util.AppException;
import org.powermock.reflect.Whitebox;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService sut;

    @Mock
    private JaxRsDpsLog logger;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @BeforeEach
    public void init() {
        Whitebox.setInternalState(sut, "entitlementsUrl", "entitlementsUrl");
    }

    @Test
    public void shouldHandleEntitlementsException() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        Enumeration<String> headerNames = Mockito.mock(Enumeration.class);
        Mockito.when(headerNames.hasMoreElements()).thenReturn(false);
        Mockito.when(httpServletRequest.getHeaderNames()).thenReturn(headerNames);

        sut.initEntitlementsFactory();
        boolean result = sut.isAuthorized(httpServletRequest, httpServletResponse);

        Assertions.assertFalse(result);
        Mockito.verify(logger).warning("User not authenticated. Response: HttpResponse(headers=null, body=, contentType=, responseCode=0, exception=org.apache.http.client.ClientProtocolException, request=entitlementsUrl/groups, httpMethod=GET, latency=0)");
        Mockito.verify(handlerExceptionResolver).resolveException(Mockito.eq(httpServletRequest),
                Mockito.eq(httpServletResponse), Mockito.eq(null), Mockito.any(AppException.class));
    }

    @Test
    public void shouldHandleNPEFromEntitlementsService() throws EntitlementsException {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        Enumeration<String> headerNames = Mockito.mock(Enumeration.class);
        Mockito.when(headerNames.hasMoreElements()).thenReturn(false);
        Mockito.when(httpServletRequest.getHeaderNames()).thenReturn(headerNames);
        EntitlementsFactory entitlementsFactory = Mockito.mock(EntitlementsFactory.class);
        Whitebox.setInternalState(sut, "entitlementsFactory", entitlementsFactory);
        IEntitlementsService entitlementsService = Mockito.mock(IEntitlementsService.class);
        Mockito.when(entitlementsFactory.create(Mockito.any(DpsHeaders.class))).thenReturn(entitlementsService);
        Mockito.when(entitlementsService.getGroups()).thenThrow(new NullPointerException());

        boolean result = sut.isAuthorized(httpServletRequest, httpServletResponse);

        Assertions.assertFalse(result);
        Mockito.verify(logger).warning("User not authenticated. Null pointer exception: null");
        Mockito.verify(handlerExceptionResolver).resolveException(Mockito.eq(httpServletRequest),
                Mockito.eq(httpServletResponse), Mockito.eq(null), Mockito.any(AppException.class));
    }

    @Test
    public void shouldVerifyAuthenticationSuccessfully() throws EntitlementsException {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        Enumeration<String> headerNames = Mockito.mock(Enumeration.class);
        Mockito.when(headerNames.hasMoreElements()).thenReturn(false);
        Mockito.when(httpServletRequest.getHeaderNames()).thenReturn(headerNames);
        EntitlementsFactory entitlementsFactory = Mockito.mock(EntitlementsFactory.class);
        Whitebox.setInternalState(sut, "entitlementsFactory", entitlementsFactory);
        IEntitlementsService entitlementsService = Mockito.mock(IEntitlementsService.class);
        Mockito.when(entitlementsFactory.create(Mockito.any(DpsHeaders.class))).thenReturn(entitlementsService);
        Groups groups = new Groups();
        groups.setMemberEmail("email");
        Mockito.when(entitlementsService.getGroups()).thenReturn(groups);

        boolean result = sut.isAuthorized(httpServletRequest, httpServletResponse);

        Assertions.assertTrue(result);
        Mockito.verify(logger).info("User authenticated | User: email");
        Mockito.verifyNoMoreInteractions(handlerExceptionResolver);
    }
}
