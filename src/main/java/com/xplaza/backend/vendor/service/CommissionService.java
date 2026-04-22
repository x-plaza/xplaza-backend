/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.vendor.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Splits a sale between the marketplace and the seller. The default rate is
 * configurable but per-shop overrides should be added later via the shop entity
 * once the {@code commission_rate} column is in use.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommissionService {

  @Value("${marketplace.default-commission-rate:0.10}")
  private BigDecimal defaultRate;

  public CommissionSplit calculate(BigDecimal grossAmount, BigDecimal overrideRate) {
    var rate = overrideRate != null ? overrideRate : defaultRate;
    var commission = grossAmount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    var sellerNet = grossAmount.subtract(commission);
    return new CommissionSplit(grossAmount, rate, commission, sellerNet);
  }

  public record CommissionSplit(
      BigDecimal gross,
      BigDecimal rate,
      BigDecimal commission,
      BigDecimal sellerNet
  ) {
  }
}
