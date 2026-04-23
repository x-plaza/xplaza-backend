/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.idempotency;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.xplaza.backend.common.util.LogSanitizer;

/**
 * Looks for an {@code Idempotency-Key} header on POST/DELETE/PATCH/PUT requests
 * targeting payment, refund, checkout or order routes.
 *
 * <p>
 * Semantics:
 * <ul>
 * <li>If the key is new, the request body's hash + endpoint are reserved and
 * the request proceeds. After the handler completes, the captured response
 * status and body are persisted against the same key so a later replay returns
 * exactly the original response.</li>
 * <li>If the key already exists and the current endpoint + request hash match
 * the stored ones, the stored response is replayed and the handler is
 * skipped.</li>
 * <li>If the key exists but the current endpoint or body hash does not match,
 * the request is rejected with HTTP 409 — preventing a client from re-using a
 * key across different operations and getting back an unrelated cached
 * response.</li>
 * <li>If the response has not yet been persisted (concurrent in-flight request
 * with the same key), the replay returns 409 instead of leaking an empty
 * body.</li>
 * </ul>
 */
@Component
public class IdempotencyInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(IdempotencyInterceptor.class);

  /** Request attribute name for the idempotency key once it has been reserved. */
  static final String ATTR_KEY = IdempotencyInterceptor.class.getName() + ".key";

  private final IdempotencyService service;

  public IdempotencyInterceptor(IdempotencyService service) {
    this.service = service;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!isMutating(request) || !isProtectedRoute(request)) {
      return true;
    }

    String key = request.getHeader("Idempotency-Key");
    if (key == null || key.isBlank()) {
      return true;
    }

    String endpoint = request.getRequestURI();
    String body = readBody(request);
    String requestHash = service.hash(body);

    var existing = service.find(key);
    if (existing.isPresent()) {
      var rec = existing.get();
      // Endpoint + request body must match the original. If they do not, the
      // client is re-using a key for a different operation — reject with 409.
      boolean endpointMatches = endpoint.equals(rec.getEndpoint());
      boolean hashMatches = requestHash != null && requestHash.equals(rec.getRequestHash());
      if (!endpointMatches || !hashMatches) {
        log.warn("Idempotency key reuse rejected: key={}, endpointMatches={}, hashMatches={}",
            LogSanitizer.forLog(key), endpointMatches, hashMatches);
        writeJson(response, HttpServletResponse.SC_CONFLICT,
            "{\"error\":\"idempotency_key_reuse\","
                + "\"message\":\"Idempotency-Key was previously used for a different request\"}");
        return false;
      }
      // No persisted response yet means an earlier request with this key is
      // still in flight. Returning the original 0-byte body would silently
      // mislead the caller, so we surface it as a conflict.
      if (rec.getResponseStatus() == null) {
        writeJson(response, HttpServletResponse.SC_CONFLICT,
            "{\"error\":\"idempotency_in_flight\","
                + "\"message\":\"A request with this Idempotency-Key is still being processed\"}");
        return false;
      }
      response.setStatus(rec.getResponseStatus());
      response.setHeader("Content-Type", "application/json");
      response.setHeader("X-Idempotent-Replay", "true");
      if (rec.getResponseBody() != null) {
        response.getWriter().write(rec.getResponseBody());
      }
      return false;
    }

    try {
      service.reserve(key, endpoint, body);
      request.setAttribute(ATTR_KEY, key);
    } catch (DataIntegrityViolationException e) {
      // Lost the race with another request that just inserted the same key.
      log.warn("Idempotency key collision on insert: {}", LogSanitizer.forLog(key));
      writeJson(response, HttpServletResponse.SC_CONFLICT, "{\"error\":\"idempotency_conflict\"}");
      return false;
    }
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    Object keyAttr = request.getAttribute(ATTR_KEY);
    if (keyAttr == null) {
      return;
    }
    String key = keyAttr.toString();
    String body = (response instanceof CapturingHttpServletResponse cap) ? cap.capturedBody() : null;
    try {
      service.persistResponse(key, response.getStatus(), body);
    } catch (Exception persistEx) {
      log.warn("Failed to persist idempotent response for key {}: {}", key, persistEx.toString());
    }
  }

  private static void writeJson(HttpServletResponse response, int status, String json) throws IOException {
    response.setStatus(status);
    response.setHeader("Content-Type", "application/json");
    response.getWriter().write(json);
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
   * downstream handler.
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

  /**
   * Wraps the response so the body written by the handler can be both sent to the
   * client and captured for persistence in {@code idempotency_keys}.
   */
  public static final class CapturingHttpServletResponse extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private PrintWriter writer;
    private ServletOutputStream outputStream;

    public CapturingHttpServletResponse(HttpServletResponse response) {
      super(response);
    }

    public String capturedBody() {
      try {
        if (writer != null) {
          writer.flush();
        }
        if (outputStream != null) {
          outputStream.flush();
        }
      } catch (IOException ignored) {
        // best-effort; body capture failure must not break the response
      }
      return buffer.toString(StandardCharsets.UTF_8);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
      if (outputStream == null) {
        ServletOutputStream delegate = super.getOutputStream();
        outputStream = new ServletOutputStream() {
          @Override
          public boolean isReady() {
            return delegate.isReady();
          }

          @Override
          public void setWriteListener(WriteListener listener) {
            delegate.setWriteListener(listener);
          }

          @Override
          public void write(int b) throws IOException {
            delegate.write(b);
            buffer.write(b);
          }

          @Override
          public void write(byte[] b, int off, int len) throws IOException {
            delegate.write(b, off, len);
            buffer.write(b, off, len);
          }
        };
      }
      return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
      if (writer == null) {
        writer = new PrintWriter(new java.io.OutputStreamWriter(getOutputStream(), StandardCharsets.UTF_8), false);
      }
      return writer;
    }
  }
}
