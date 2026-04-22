/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Caffeine-backed cache. In cloud, the application can be configured to
 * use Redis via Spring Data Redis cache manager (autoconfigured when
 * {@code spring.cache.type=redis}); the Caffeine manager remains the local
 * default for low-latency reads.
 */
@Configuration
public class CacheConfig implements CachingConfigurer {

  static final List<String> CACHE_NAMES = List.of(
      "products", "productById", "categories", "categoryById",
      "shops", "currencies", "taxRules", "customerSegments",
      "homepageCampaigns", "deliveryCosts", "recommendations");

  @Override
  @Bean
  public CacheManager cacheManager() {
    var manager = new CaffeineCacheManager();
    manager.setCaffeine(Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .recordStats());
    manager.setCacheNames(CACHE_NAMES);
    return manager;
  }
}
