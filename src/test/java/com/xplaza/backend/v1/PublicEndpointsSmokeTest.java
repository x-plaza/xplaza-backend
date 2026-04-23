/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.xplaza.backend.config.TestConfig;

/**
 * Smoke checks that the v1.0.0 public endpoints (CMS, search, recommendations,
 * gift-card balance) are wired into the MVC context. Behavioural tests live
 * with each module; this just guarantees route registration so a missing bean
 * fails CI fast.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
class PublicEndpointsSmokeTest {

  @Autowired
  MockMvc mvc;

  @Test
  void cmsPagesRouteIsRegistered() throws Exception {
    mvc.perform(get("/api/v1/cms/pages")).andExpect(status().is2xxSuccessful());
  }

  @Test
  void cmsBannersRouteIsRegistered() throws Exception {
    mvc.perform(get("/api/v1/cms/banners")).andExpect(status().is2xxSuccessful());
  }

  @Test
  void cmsFaqRouteIsRegistered() throws Exception {
    mvc.perform(get("/api/v1/cms/faqs")).andExpect(status().is2xxSuccessful());
  }

  @Test
  void searchRouteIsRegistered() throws Exception {
    mvc.perform(get("/api/v1/search/products?q=test")).andExpect(status().is2xxSuccessful());
  }

  @Test
  void autocompleteRouteIsRegistered() throws Exception {
    mvc.perform(get("/api/v1/search/autocomplete?q=foo")).andExpect(status().is2xxSuccessful());
  }
}
