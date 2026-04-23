/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Locks down the Stripe idempotency-key derivation:
 *
 * <ol>
 * <li>Caller-supplied keys win.</li>
 * <li>Otherwise the key is a stable SHA-256 fingerprint of the request +
 * metadata, so legitimate retries collapse but unrelated payments never
 * collide.</li>
 * <li>The pathological "no description, no metadata" path no longer collapses
 * to {@code "create-pi-0-..."} for every payment of the same amount.</li>
 * </ol>
 */
class StripePaymentGatewayKeyTest {

  private static final Method KEY = resolveMethod();

  private static Method resolveMethod() {
    try {
      Method m = StripePaymentGateway.class.getDeclaredMethod(
          "resolveIdempotencyKey", BigDecimal.class, String.class, String.class, Map.class);
      m.setAccessible(true);
      return m;
    } catch (NoSuchMethodException e) {
      throw new AssertionError(e);
    }
  }

  private static String key(BigDecimal amount, String currency, String description, Map<String, String> meta)
      throws Exception {
    return (String) KEY.invoke(new StripePaymentGateway(), amount, currency, description, meta);
  }

  @Test
  void callerSuppliedKeyWinsOutright() throws Exception {
    Map<String, String> meta = new HashMap<>();
    meta.put("idempotencyKey", "explicit-key-123");
    meta.put("orderId", "ord-1");
    String k = key(new BigDecimal("12.34"), "usd", "noise", meta);
    assertEquals("explicit-key-123", k);
  }

  @Test
  void identicalRequestsProduceIdenticalKey() throws Exception {
    Map<String, String> meta = Map.of("orderId", "ord-42", "customerId", "cust-7");
    String k1 = key(new BigDecimal("99.99"), "eur", "Order #42", meta);
    String k2 = key(new BigDecimal("99.99"), "eur", "Order #42", new HashMap<>(meta));
    assertEquals(k1, k2, "stable across retries of the same logical operation");
    assertTrue(k1.startsWith("create-pi-"), "expected create-pi- prefix, got " + k1);
  }

  @Test
  void differentOrderIdsProduceDifferentKeys() throws Exception {
    String a = key(new BigDecimal("10.00"), "usd", "any", Map.of("orderId", "ord-1"));
    String b = key(new BigDecimal("10.00"), "usd", "any", Map.of("orderId", "ord-2"));
    assertNotEquals(a, b);
  }

  /**
   * The original bug: with {@code description == null} and an empty metadata map,
   * every payment of the same {@code amount + currency} collapsed onto the same
   * key, causing Stripe to reject the second one.
   */
  @Test
  void nullDescriptionWithoutMetadata_doesNotCollideAcrossPayments() throws Exception {
    String a = key(new BigDecimal("25.00"), "usd", null, new HashMap<>());
    String b = key(new BigDecimal("25.00"), "usd", null, new HashMap<>());
    assertNotEquals(a, b, "no-context fallback must not collide across calls");
    assertTrue(a.startsWith("create-pi-nocontext-"), "expected nocontext fallback, got " + a);
  }

  @Test
  void differentAmountsProduceDifferentKeys() throws Exception {
    String a = key(new BigDecimal("10.00"), "usd", "x", Map.of("orderId", "ord-1"));
    String b = key(new BigDecimal("11.00"), "usd", "x", Map.of("orderId", "ord-1"));
    assertNotEquals(a, b);
  }
}
