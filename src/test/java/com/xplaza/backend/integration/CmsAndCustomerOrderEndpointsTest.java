/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.xplaza.backend.cms.domain.entity.CmsBlock;

/**
 * MockMvc coverage for v1.1.0 endpoints introduced in the release: CMS block
 * CRUD/read and the per-vendor child-order listing on
 * {@link com.xplaza.backend.order.controller.CustomerOrderController}.
 */
public class CmsAndCustomerOrderEndpointsTest extends BaseIntegrationTest {

  @Test
  public void cmsBlock_canBeCreatedAndFetchedPublicly() throws Exception {
    String adminToken = getAdminToken();

    String code = "hero-banner-" + UUID.randomUUID();
    CmsBlock block = CmsBlock.builder()
        .code(code)
        .title("Welcome")
        .body("<h1>Hello world</h1>")
        .locale("en")
        .active(Boolean.TRUE)
        .build();

    mockMvc.perform(post("/api/v1/cms/blocks")
        .header("Authorization", "Bearer " + adminToken)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(block)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(code))
        .andExpect(jsonPath("$.title").value("Welcome"));

    // Public GET (no auth header) should resolve the block.
    mockMvc.perform(get("/api/v1/cms/blocks/" + code))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.body").value("<h1>Hello world</h1>"))
        .andExpect(jsonPath("$.locale").value("en"));

    mockMvc.perform(get("/api/v1/cms/blocks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  public void cmsBlock_unknownCodeReturns404() throws Exception {
    mockMvc.perform(get("/api/v1/cms/blocks/" + UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  public void customerOrders_childrenEndpointReturnsEmptyForUnknownParent() throws Exception {
    // Unknown parent id should still be a well-formed (empty) list, not 5xx.
    mockMvc.perform(get("/api/v1/customer-orders/" + UUID.randomUUID() + "/children"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }
}
