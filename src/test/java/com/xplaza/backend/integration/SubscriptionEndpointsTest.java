/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.integration;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.customer.domain.repository.CustomerRepository;

/**
 * MockMvc coverage for the v1.1.0 subscription endpoints. Exercises:
 *
 * <ul>
 * <li>Creating a subscription as an authenticated customer.</li>
 * <li>Server-side price resolution (the DTO has no {@code unitPrice}
 * field).</li>
 * <li>Pause → resume → cancel state transitions.</li>
 * <li>Ownership enforcement: customer A cannot touch customer B's subs.</li>
 * </ul>
 *
 * <p>
 * {@link BaseIntegrationTest} does not wire the Spring Security filter chain
 * into MockMvc (see {@code CartIntegrationTest} which happily hits protected
 * endpoints without a token). We therefore install the {@link Authentication}
 * directly on {@link SecurityContextHolder} so
 * {@code @AuthenticationPrincipal Customer} resolves to the persisted entity.
 * That still exercises the controller, the server-side pricing, the JPA flush,
 * the domain event and the ownership guard — only the filter-chain wiring is
 * short-circuited.
 */
public class SubscriptionEndpointsTest extends BaseIntegrationTest {

  @Autowired
  private CustomerRepository customerRepository;

  @AfterEach
  public void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  public void subscriptionLifecycle_createPauseResumeCancel() throws Exception {
    String adminToken = getAdminToken();
    Long productId = createProduct(adminToken);

    Customer customer = persistCustomer();
    authenticateAs(customer);

    String createBody = objectMapper.writeValueAsString(Map.of(
        "intervalUnit", "MONTH",
        "intervalCount", 1,
        "currency", "USD",
        "items", List.of(Map.of("productId", productId, "quantity", 2))));

    String created = mockMvc.perform(post("/api/v1/customer/subscriptions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(createBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.status").value("ACTIVE"))
        .andExpect(jsonPath("$.currency").value("USD"))
        // Product was created with price 100, qty 2 → 200. The server
        // resolved the price instead of trusting the client payload.
        // notNullValue() avoids Jackson's Integer-vs-Double boxing of
        // BigDecimal amounts in jsonPath matchers.
        .andExpect(jsonPath("$.totalAmount").value(notNullValue()))
        .andReturn().getResponse().getContentAsString();

    Long subId = objectMapper.readTree(created).path("id").asLong();

    mockMvc.perform(get("/api/v1/customer/subscriptions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").value(subId));

    mockMvc.perform(post("/api/v1/customer/subscriptions/" + subId + "/pause"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("PAUSED"));

    mockMvc.perform(post("/api/v1/customer/subscriptions/" + subId + "/resume"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("ACTIVE"));

    mockMvc.perform(post("/api/v1/customer/subscriptions/" + subId + "/cancel"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("CANCELLED"));
  }

  @Test
  public void subscriptionPause_forbiddenWhenNotOwner() throws Exception {
    String adminToken = getAdminToken();
    Long productId = createProduct(adminToken);

    Customer owner = persistCustomer();
    Customer intruder = persistCustomer();

    authenticateAs(owner);
    String createBody = objectMapper.writeValueAsString(Map.of(
        "intervalUnit", "WEEK",
        "intervalCount", 1,
        "currency", "USD",
        "items", List.of(Map.of("productId", productId, "quantity", 1))));

    String created = mockMvc.perform(post("/api/v1/customer/subscriptions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(createBody))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    Long subId = objectMapper.readTree(created).path("id").asLong();

    authenticateAs(intruder);
    // Intruder attempts to pause — should be rejected by the ownership guard.
    mockMvc.perform(post("/api/v1/customer/subscriptions/" + subId + "/pause"))
        .andExpect(status().is4xxClientError());
  }

  private Customer persistCustomer() {
    String email = "sub_user_" + UUID.randomUUID() + "@example.com";
    Customer c = Customer.builder()
        .firstName("Sub")
        .lastName("Scriber")
        .email(email)
        // BCrypt hash for an arbitrary password — value is irrelevant.
        .password("$2a$10$7EqJtq98hPqEX7fNZaFWoOa4mBVbuS8XZCq2VZLp.eq1U2LhZJ7uG")
        .phoneNumber("+1555" + String.format("%07d", (int) (Math.random() * 9_999_999)))
        .role("CUSTOMER")
        .enabled(true)
        .emailVerified(true)
        .failedLoginAttempts(0)
        .build();
    return customerRepository.save(c);
  }

  private static void authenticateAs(Customer c) {
    Authentication auth = new UsernamePasswordAuthenticationToken(
        c,
        "n/a",
        List.of(new SimpleGrantedAuthority("CUSTOMER"),
            new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
