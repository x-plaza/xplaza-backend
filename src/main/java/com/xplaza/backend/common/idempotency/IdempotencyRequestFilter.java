/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.idempotency;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Wraps mutating requests on idempotency-protected routes with both a
 * body-caching request wrapper and a body-capturing response wrapper. This runs
 * before the {@link IdempotencyInterceptor} so the interceptor can:
 *
 * <ul>
 * <li>read the request body multiple times (to compute the request hash and let
 * the controller deserialize it);</li>
 * <li>capture the handler's response body and persist it against the
 * idempotency key in {@code afterCompletion}.</li>
 * </ul>
 *
 * <p>
 * For non-mutating requests, requests without an {@code Idempotency-Key}
 * header, and unprotected routes the filter is a no-op so it adds no overhead
 * to the hot read path.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 50)
public class IdempotencyRequestFilter extends OncePerRequestFilter {

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    if (!isMutating(request) || !isProtectedRoute(request)) {
      return true;
    }
    String key = request.getHeader("Idempotency-Key");
    return key == null || key.isBlank();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    var wrappedReq = new IdempotencyInterceptor.CachingHttpServletRequest(request);
    var wrappedRes = new IdempotencyInterceptor.CapturingHttpServletResponse(response);
    chain.doFilter(wrappedReq, wrappedRes);
  }

  private boolean isMutating(HttpServletRequest req) {
    var m = req.getMethod();
    return "POST".equals(m) || "PATCH".equals(m) || "DELETE".equals(m) || "PUT".equals(m);
  }

  private boolean isProtectedRoute(HttpServletRequest req) {
    var p = req.getRequestURI();
    return p.startsWith("/api/v1/payments")
        || p.startsWith("/api/v1/checkout")
        || p.startsWith("/api/v1/orders")
        || p.startsWith("/api/v1/customer/orders");
  }
}
