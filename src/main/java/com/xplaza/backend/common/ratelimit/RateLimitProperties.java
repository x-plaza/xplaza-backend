/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.ratelimit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "xplaza.rate-limit")
public record RateLimitProperties(
    boolean enabled,
    int authRequestsPerMinute,
    int paymentRequestsPerMinute,
    int defaultRequestsPerMinute,
    long maxBuckets,
    long bucketIdleMinutes
) {
  public RateLimitProperties {
    if (authRequestsPerMinute <= 0)
      authRequestsPerMinute = 10;
    if (paymentRequestsPerMinute <= 0)
      paymentRequestsPerMinute = 30;
    if (defaultRequestsPerMinute <= 0)
      defaultRequestsPerMinute = 120;
    // Cap on the number of cached buckets; protects against unbounded memory
    // growth driven by the unique-client cardinality (one bucket per client
    // key x route category). 100k entries ~ a few MB of heap.
    if (maxBuckets <= 0)
      maxBuckets = 100_000L;
    // Buckets refill on a 1-minute window, so after several minutes of idle
    // time a new bucket would already be fully refilled; dropping it is safe.
    if (bucketIdleMinutes <= 0)
      bucketIdleMinutes = 10L;
  }
}
