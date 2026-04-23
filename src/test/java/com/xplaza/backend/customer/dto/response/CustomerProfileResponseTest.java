/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.customer.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.customer.domain.entity.Customer;

/**
 * Locks down that the {@code /me} and {@code /gdpr/export} contract never leaks
 * security-sensitive fields. Both the dedicated DTO <em>and</em> the underlying
 * entity are exercised so a future refactor that goes back to returning
 * {@link Customer} directly will still produce a green test.
 */
class CustomerProfileResponseTest {

  private static final ObjectMapper MAPPER = new ObjectMapper()
      .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE)
      .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY);

  @Test
  void profileResponse_doesNotExposeCredentials() throws Exception {
    Customer c = baseCustomer();
    String json = MAPPER.writeValueAsString(CustomerProfileResponse.from(c));
    assertNoSecrets(json);
    assertFalse(json.contains("hashed-secret"), "password hash leaked");
    assertFalse(json.contains("totp-shared-secret"), "MFA secret leaked");
  }

  @Test
  void rawCustomerEntity_alsoHidesCredentialsViaJsonIgnore() throws Exception {
    Customer c = baseCustomer();
    String json = MAPPER.writeValueAsString(c);
    assertNoSecrets(json);
  }

  private static Customer baseCustomer() {
    Customer c = Customer.builder()
        .firstName("Ada")
        .lastName("Lovelace")
        .email("ada@example.com")
        .password("hashed-secret")
        .mfaEnabled(true)
        .mfaSecret("totp-shared-secret")
        .oauthSubject("oauth|subject|leak")
        .failedLoginAttempts(3)
        .lockedUntil(Instant.now().plusSeconds(60))
        .build();
    c.setCustomerId(42L);
    return c;
  }

  private static void assertNoSecrets(String json) {
    assertEquals(false, json.contains("\"password\""),
        "password field must not appear in JSON: " + json);
    assertEquals(false, json.contains("\"mfaSecret\""),
        "mfaSecret field must not appear in JSON: " + json);
    assertEquals(false, json.contains("\"oauthSubject\""),
        "oauthSubject field must not appear in JSON: " + json);
    assertEquals(false, json.contains("\"failedLoginAttempts\""),
        "failedLoginAttempts field must not appear in JSON: " + json);
    assertEquals(false, json.contains("\"lockedUntil\""),
        "lockedUntil field must not appear in JSON: " + json);
  }
}
