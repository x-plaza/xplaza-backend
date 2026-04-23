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

import com.xplaza.backend.shop.domain.repository.ShopRepository;

/**
 * Splits a sale between the marketplace and the seller. Resolution order:
 * <ol>
 * <li>Explicit {@code overrideRate} argument (used by integration tests).</li>
 * <li>Per-shop {@code commission_rate} column (negotiated rate).</li>
 * <li>Marketplace default {@code marketplace.default-commission-rate}.</li>
 * </ol>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommissionService {

  @Value("${marketplace.default-commission-rate:0.10}")
  private BigDecimal defaultRate;

  private final ShopRepository shopRepository;

  public CommissionSplit calculate(BigDecimal grossAmount, BigDecimal overrideRate) {
    var rate = overrideRate != null ? overrideRate : defaultRate;
    var commission = grossAmount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    var sellerNet = grossAmount.subtract(commission);
    return new CommissionSplit(grossAmount, rate, commission, sellerNet);
  }

  /**
   * Calculate using the shop's negotiated commission rate (or the marketplace
   * default when the shop has none). This is the variant payouts and order-split
   * logic should call.
   */
  public CommissionSplit calculateForShop(Long shopId, BigDecimal grossAmount) {
    BigDecimal rate = shopRepository.findById(shopId)
        .map(s -> s.getCommissionRate() == null ? defaultRate : s.getCommissionRate())
        .orElse(defaultRate);
    return calculate(grossAmount, rate);
  }

  public record CommissionSplit(
      BigDecimal gross,
      BigDecimal rate,
      BigDecimal commission,
      BigDecimal sellerNet
  ) {
  }
}
