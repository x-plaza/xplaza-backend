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
    int defaultRequestsPerMinute
) {
  public RateLimitProperties {
    if (authRequestsPerMinute <= 0)
      authRequestsPerMinute = 10;
    if (paymentRequestsPerMinute <= 0)
      paymentRequestsPerMinute = 30;
    if (defaultRequestsPerMinute <= 0)
      defaultRequestsPerMinute = 120;
  }
}
