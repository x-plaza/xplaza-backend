/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.idempotency;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IdempotencyWebMvcConfig implements WebMvcConfigurer {

  private final IdempotencyInterceptor interceptor;

  public IdempotencyWebMvcConfig(IdempotencyInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptor)
        .addPathPatterns("/api/v1/payments/**", "/api/v1/checkout/**",
            "/api/v1/orders/**", "/api/v1/customer/orders/**");
  }
}
