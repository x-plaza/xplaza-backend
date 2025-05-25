/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza;

import java.util.TimeZone;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableAutoConfiguration
public class XplazaApplication {
  @PostConstruct
  public void init() {
    // Setting Spring Boot TimeZone
    TimeZone.setDefault(TimeZone.getTimeZone("Africa/Johannesburg"));
  }

  public static void main(String[] args) {
    SpringApplication.run(XplazaApplication.class, args);
  }

}
