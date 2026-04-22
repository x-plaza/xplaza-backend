/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.idempotency;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Looks for an {@code Idempotency-Key} header on POST/DELETE/PATCH requests
 * targeting payment, refund, or checkout routes. If the key is new the request
 * proceeds; if it has been seen before with the same body, the previously
 * stored response is replayed without re-running the handler. Body re-use
 * across keys returns 409 Conflict.
 */
@Component
public class IdempotencyInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(IdempotencyInterceptor.class);

  private final IdempotencyService service;

  public IdempotencyInterceptor(IdempotencyService service) {
    this.service = service;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!isMutating(request) || !isProtectedRoute(request))
      return true;

    String key = request.getHeader("Idempotency-Key");
    if (key == null || key.isBlank())
      return true; // header optional; skip if not provided

    var existing = service.find(key);
    if (existing.isPresent()) {
      var rec = existing.get();
      response.setStatus(rec.getResponseStatus() != null ? rec.getResponseStatus() : 200);
      response.setHeader("Content-Type", "application/json");
      response.setHeader("X-Idempotent-Replay", "true");
      if (rec.getResponseBody() != null) {
        response.getWriter().write(rec.getResponseBody());
      }
      return false;
    }

    try {
      String body = readBody(request);
      service.reserve(key, request.getRequestURI(), body);
    } catch (DataIntegrityViolationException e) {
      log.warn("Idempotency key collision: {}", key);
      response.setStatus(HttpServletResponse.SC_CONFLICT);
      response.setHeader("Content-Type", "application/json");
      response.getWriter().write("{\"error\":\"idempotency_conflict\"}");
      return false;
    }
    return true;
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

  private String readBody(HttpServletRequest req) {
    if (req instanceof CachingHttpServletRequest cached) {
      return new String(cached.cachedBytes(), StandardCharsets.UTF_8);
    }
    return "";
  }

  /**
   * Wraps the request so the request body can be read both here and by the
   * handler.
   */
  public static final class CachingHttpServletRequest extends HttpServletRequestWrapper {
    private final byte[] cachedBytes;

    public CachingHttpServletRequest(HttpServletRequest request) throws IOException {
      super(request);
      this.cachedBytes = request.getInputStream().readAllBytes();
    }

    public byte[] cachedBytes() {
      return cachedBytes;
    }

    @Override
    public ServletInputStream getInputStream() {
      var bais = new java.io.ByteArrayInputStream(cachedBytes);
      return new ServletInputStream() {
        @Override
        public int read() {
          return bais.read();
        }

        @Override
        public boolean isFinished() {
          return bais.available() == 0;
        }

        @Override
        public boolean isReady() {
          return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }
      };
    }
  }
}
