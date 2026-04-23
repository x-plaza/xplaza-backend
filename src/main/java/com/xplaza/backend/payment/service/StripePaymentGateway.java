/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.payment.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;

@Service
@Slf4j
public class StripePaymentGateway implements PaymentGateway {

  private static final String[] IDENTITY_META_KEYS = {
      "orderId", "checkoutSessionId", "cartId", "customerId", "paymentIntentRef"
  };

  @Override
  @CircuitBreaker(name = "stripe")
  @Retry(name = "stripe")
  public PaymentIntent createPaymentIntent(BigDecimal amount, String currency, String description,
      Map<String, String> metadata) throws StripeException {
    Map<String, String> meta = metadata == null ? new HashMap<>() : new HashMap<>(metadata);
    PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
        .setAmount(amount.multiply(new BigDecimal(100)).longValue())
        .setCurrency(currency)
        .setDescription(description)
        .putAllMetadata(meta)
        .setAutomaticPaymentMethods(
            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                .setEnabled(true)
                .build())
        .build();
    String idempotencyKey = resolveIdempotencyKey(amount, currency, description, meta);
    var options = RequestOptions.builder().setIdempotencyKey(idempotencyKey).build();
    return PaymentIntent.create(params, options);
  }

  private String resolveIdempotencyKey(BigDecimal amount, String currency, String description,
      Map<String, String> meta) {
    String supplied = meta.get("idempotencyKey");
    if (supplied != null && !supplied.isBlank()) {
      return supplied;
    }
    boolean hasIdentity = description != null && !description.isBlank();
    if (!hasIdentity) {
      for (String key : IDENTITY_META_KEYS) {
        String v = meta.get(key);
        if (v != null && !v.isBlank()) {
          hasIdentity = true;
          break;
        }
      }
    }
    if (!hasIdentity && meta.isEmpty()) {
      String key = "create-pi-nocontext-" + UUID.randomUUID();
      log.warn("Stripe createPaymentIntent called without identifying context "
          + "(no metadata, no description); falling back to a per-call UUID idempotency key '{}'. "
          + "Network retries will create duplicate intents — supply metadata.idempotencyKey or an orderId.",
          key);
      return key;
    }
    String fingerprint = sha256Hex(
        amount + "|"
            + currency + "|"
            + Objects.toString(description, "") + "|"
            + new TreeMap<>(meta));
    return "create-pi-" + fingerprint;
  }

  private static String sha256Hex(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException e) {
      // SHA-256 is mandated by every JRE; this is unreachable in practice.
      throw new IllegalStateException("SHA-256 not available", e);
    }
  }

  @Override
  @CircuitBreaker(name = "stripe")
  @Retry(name = "stripe")
  public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws StripeException {
    PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
    Map<String, Object> params = new HashMap<>();
    var options = RequestOptions.builder().setIdempotencyKey("confirm-" + paymentIntentId).build();
    return paymentIntent.confirm(params, options);
  }
}
