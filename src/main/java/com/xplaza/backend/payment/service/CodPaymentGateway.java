/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.payment.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Cash-on-delivery "gateway" — there is no external system to call, but we
 * still create a tracked transaction so the order/refund state machine works
 * the same way as for card payments.
 */
@Service
public class CodPaymentGateway {

  public CodAuthorizationResult createCodAuthorization(BigDecimal amount, String currency, UUID orderId) {
    var ref = "cod-" + UUID.randomUUID();
    return new CodAuthorizationResult(ref, amount, currency, orderId);
  }

  public record CodAuthorizationResult(String reference, BigDecimal amount, String currency, UUID orderId) {}
}
