/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.elasticsearch.autoconfigure.DataElasticsearchAutoConfiguration;
import org.springframework.boot.data.elasticsearch.autoconfigure.DataElasticsearchRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
    DataElasticsearchAutoConfiguration.class,
    DataElasticsearchRepositoriesAutoConfiguration.class
})
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class XplazaApplication {
  public static void main(String[] args) {
    SpringApplication.run(XplazaApplication.class, args);
  }
}
