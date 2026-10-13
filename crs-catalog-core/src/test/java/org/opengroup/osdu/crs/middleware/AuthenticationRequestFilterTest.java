package org.opengroup.osdu.crs.middleware;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class AuthenticationRequestFilterTest {
    @InjectMocks
    private AuthenticationRequestFilter sut;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    public void shouldContinueFilteringWhenAuthenticated() throws Exception {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        Mockito.when(authenticationService.isAuthorized(httpServletRequest, httpServletResponse)).thenReturn(true);
        sut.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        Mockito.verify(authenticationService).isAuthorized(httpServletRequest, httpServletResponse);
        Mockito.verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    public void shouldStopFilteringWhenNotAuthenticated() throws Exception {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        Mockito.when(authenticationService.isAuthorized(httpServletRequest, httpServletResponse)).thenReturn(false);
        sut.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        Mockito.verify(authenticationService).isAuthorized(httpServletRequest, httpServletResponse);
        Mockito.verifyNoMoreInteractions(filterChain);
    }
}
