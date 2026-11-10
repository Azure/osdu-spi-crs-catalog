/*
 Copyright 2020-2024 Google LLC
 Copyright 2020-2024 EPAM Systems, Inc

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package org.opengroup.osdu.crs.security;

import tools.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.opengroup.osdu.crs.middleware.AuthenticationRequestFilter;
import org.opengroup.osdu.crs.middleware.AuthenticationService;
import org.opengroup.osdu.crs.util.AppError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthSecurityConfig implements AccessDeniedHandler, AuthenticationEntryPoint {

  private AuthenticationRequestFilter authFilter;

  private static final JsonMapper OBJECT_MAPPER = new JsonMapper();

  private static final String[] AUTH_WHITELIST = {
    "/",
    "/_ah/*",
    "/v2/_ah/liveness_check",
    "/v2/_ah/readiness_check",
    "/v2/api-docs",
    "/api-docs/**",
    "/api-docs.yaml",
    "/v3/api-docs",
    "/configuration/ui",
    "/swagger-resources/**",
    "/configuration/security",
    "/swagger-ui.html",
    "/swagger-ui/**",
    "/v2/info",
    "/v3/info",
    "/webjars/**",
    "/csrf",
    "/api/crs/catalog/actuator",
    "/api/crs/catalog/actuator/**",
    "/api/crs/catalog/actuator/health",
    "/swagger/**",
    "/health/**",
    "/api-docs.yaml/**"
  };

  // AuthenticationRequestFilter is not a recognized bean, so construct it manually
  public AuthSecurityConfig(AuthenticationService authenticationService) {
    authFilter = new AuthenticationRequestFilter(authenticationService);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
        .authorizeHttpRequests(
            authorize ->
                authorize.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated())
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(AUTH_WHITELIST);
  }

  @Override
  public void handle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AccessDeniedException e)
      throws IOException {
    writeUnauthorizedError(httpServletResponse);
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    writeUnauthorizedError(response);
  }

  private static void writeUnauthorizedError(HttpServletResponse response) throws IOException {
    AppError appError =
        AppError.builder()
            .code(HttpStatus.UNAUTHORIZED.value())
            .message("The user is not authorized to perform this action")
            .reason("Unauthorized")
            .build();
    String body = OBJECT_MAPPER.writeValueAsString(appError);

    PrintWriter out = response.getWriter();
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    out.print(body);
    out.flush();
  }
}
