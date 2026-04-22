/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.ratelimit;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * In-memory token-bucket rate limiter. Buckets are keyed by client IP (or the
 * authenticated principal name when present) and the route bucket category
 * (auth, payment, default). For multi-instance deployments this should be
 * replaced with a Redis-backed bucket store; the bucket key contract is
 * unchanged so the swap is local to this filter.
 */
@Component
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitFilter extends OncePerRequestFilter {

  private final RateLimitProperties props;
  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  public RateLimitFilter(RateLimitProperties props) {
    this.props = props;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    if (!props.enabled()) return true;
    var path = request.getRequestURI();
    return path.startsWith("/actuator")
        || path.startsWith("/v3/api-docs")
        || path.startsWith("/swagger-ui")
        || path.equals("/h2-console")
        || path.startsWith("/h2-console/");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    var category = categorize(request);
    var key = clientKey(request) + "|" + category;
    var bucket = buckets.computeIfAbsent(key, k -> newBucket(category));
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
    if (probe.isConsumed()) {
      response.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));
      chain.doFilter(request, response);
    } else {
      long waitSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000L;
      response.setStatus(429);
      response.setHeader("Retry-After", String.valueOf(Math.max(1, waitSeconds)));
      response.setContentType("application/json");
      response.getWriter().write("{\"error\":\"rate_limit_exceeded\",\"category\":\"" + category + "\"}");
    }
  }

  private Bucket newBucket(String category) {
    int perMinute = switch (category) {
      case "auth" -> props.authRequestsPerMinute();
      case "payment" -> props.paymentRequestsPerMinute();
      default -> props.defaultRequestsPerMinute();
    };
    return Bucket.builder()
        .addLimit(Bandwidth.classic(perMinute, Refill.intervally(perMinute, Duration.ofMinutes(1))))
        .build();
  }

  private String categorize(HttpServletRequest req) {
    var path = req.getRequestURI();
    if (path.contains("/auth/")) return "auth";
    if (path.contains("/payments") || path.contains("/checkout")) return "payment";
    return "default";
  }

  private String clientKey(HttpServletRequest req) {
    var fwd = req.getHeader("X-Forwarded-For");
    var ip = (fwd != null && !fwd.isBlank()) ? fwd.split(",")[0].trim() : req.getRemoteAddr();
    var principal = req.getUserPrincipal();
    return principal != null ? principal.getName() : ip;
  }
}
