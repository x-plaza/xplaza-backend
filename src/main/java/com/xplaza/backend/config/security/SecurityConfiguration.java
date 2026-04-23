/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.config.security;

import java.util.List;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.xplaza.backend.common.ratelimit.RateLimitFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Value("${xplaza.cors.allowed-origins:http://localhost:3000}")
  private String allowedOrigins;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    var configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(allowedOrigins.split(",")));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept-Language",
        "X-Refresh-Token", "Idempotency-Key", "X-Requested-With"));
    configuration.setExposedHeaders(List.of("X-RateLimit-Remaining", "Retry-After", "Location"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain defaultSecurityFilterChain(JwtRequestFilter jwtFilter,
      RateLimitFilter rateLimitFilter,
      ObjectProvider<ClientRegistrationRepository> oauthRepoProvider,
      HttpSecurity http) throws Exception {
    // CSRF protection is intentionally disabled.
    //
    // This filter chain authenticates every request via a stateless JWT
    // (`Authorization: Bearer …`) — no Spring session cookie is issued (see
    // SessionCreationPolicy.STATELESS above) and form login / HTTP basic are
    // also disabled. Without an ambient credential the browser carries no
    // implicit authority that a third-party origin could ride on, so the CSRF
    // attack model does not apply. CORS is locked down to the allowed-origins
    // list above for an additional layer of defence.
    //
    // OAuth2 authorization-code login (when enabled below) carries its own
    // state parameter end-to-end, which Spring Security validates.
    //
    // This is the configuration recommended by the Spring Security reference
    // for token-only REST APIs:
    // https://docs.spring.io/spring-security/reference/features/exploits/csrf.html
    //
    // CodeQL flags `java/spring-disabled-csrf-protection` regardless of
    // session policy; alerts are dismissed as `won't fix` with this comment as
    // justification. Re-enable CSRF if the API ever starts issuing session
    // cookies or accepting form submissions from browsers.
    var chain = http
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(authz -> authz
            // public infra
            .requestMatchers(
                "/actuator/health", "/actuator/health/**", "/actuator/info",
                "/actuator/prometheus",
                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                "/h2-console/**")
            .permitAll()
            // public auth
            .requestMatchers(
                "/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/register",
                "/api/v1/auth/forgot-password", "/api/v1/auth/reset-password",
                "/api/v1/customer/auth/**",
                "/api/v1/webhooks/**",
                "/api/v1/oauth2/**", "/login/oauth2/**", "/oauth2/**")
            .permitAll()
            // public catalog
            .requestMatchers(HttpMethod.GET,
                "/api/v1/products/**",
                "/api/v1/categories/**",
                "/api/v1/brands/**",
                "/api/v1/shops/**",
                "/api/v1/cms/**",
                "/api/v1/search/**",
                "/api/v1/reviews/product/**")
            .permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .headers(h -> h.frameOptions(f -> f.sameOrigin()));

    if (oauthRepoProvider.getIfAvailable() != null) {
      chain.oauth2Login(Customizer.withDefaults());
    }
    return chain.build();
  }
}
