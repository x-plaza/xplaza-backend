/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.payment.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class StripePaymentGateway implements PaymentGateway {

  @Override
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
    // Stripe-side idempotency: prefer caller-supplied key, fall back to a stable hash.
    String idempotencyKey = meta.getOrDefault("idempotencyKey",
        "create-pi-" + (description == null ? "" : description.hashCode()) + "-" + amount + currency);
    var options = RequestOptions.builder().setIdempotencyKey(idempotencyKey).build();
    return PaymentIntent.create(params, options);
  }

  @Override
  public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws StripeException {
    PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
    Map<String, Object> params = new HashMap<>();
    var options = RequestOptions.builder().setIdempotencyKey("confirm-" + paymentIntentId).build();
    return paymentIntent.confirm(params, options);
  }
}
