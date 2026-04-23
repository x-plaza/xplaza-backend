/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.ratelimit;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;

import jakarta.servlet.FilterChain;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerMapping;

import com.github.benmanes.caffeine.cache.Cache;

/**
 * Regression test for the unbounded-bucket-map memory leak. Each unique
 * (clientKey, route category) pair used to create a permanent
 * {@code ConcurrentHashMap} entry; the bounded Caffeine cache must enforce a
 * size cap so the bucket store cannot grow without limit.
 */
class RateLimitFilterMemoryTest {

  @Test
  void bucketCacheIsBoundedBySize() throws Exception {
    var props = new RateLimitProperties(true, 10, 30, 120, /* maxBuckets */ 64L, /* idleMin */ 10L);
    var filter = new RateLimitFilter(props);
    FilterChain chain = (req, res) -> {
    };

    for (int i = 0; i < 10_000; i++) {
      MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/v1/products");
      req.setRemoteAddr("10.0." + (i / 256) + "." + (i % 256));
      req.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "/api/v1/products");
      MockHttpServletResponse res = new MockHttpServletResponse();
      filter.doFilter(req, res, chain);
    }

    Cache<?, ?> buckets = bucketsCache(filter);
    buckets.cleanUp();
    assertThat(buckets.estimatedSize())
        .as("bucket cache must be bounded by maxBuckets, not grow with unique clients")
        .isLessThanOrEqualTo(props.maxBuckets());
  }

  @SuppressWarnings("unchecked")
  private static Cache<String, ?> bucketsCache(RateLimitFilter filter) throws Exception {
    Field f = RateLimitFilter.class.getDeclaredField("buckets");
    f.setAccessible(true);
    return (Cache<String, ?>) f.get(filter);
  }
}
