/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.tax.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.tax.domain.entity.TaxRule;
import com.xplaza.backend.tax.domain.repository.TaxRuleRepository;
import com.xplaza.backend.tax.domain.repository.TaxZoneRepository;

/**
 * Region-aware tax computation. Lookups are cached because zone/rule data
 * changes infrequently (usually quarterly) but is read on every checkout.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaxService {

  private final TaxZoneRepository zoneRepo;
  private final TaxRuleRepository ruleRepo;

  public record TaxBreakdown(
      BigDecimal taxableAmount,
      BigDecimal totalTax,
      List<AppliedTax> appliedTaxes
  ) {
  }

  public record AppliedTax(
      String name,
      BigDecimal rate,
      BigDecimal amount
  ) {
  }

  /**
   * Compute tax for the given pre-tax amount and shipping destination. If no zone
   * matches, returns zero-tax breakdown.
   */
  @Cacheable(value = "tax", key = "#countryCode + '-' + #region + '-' + #amount")
  @Transactional(readOnly = true)
  public TaxBreakdown computeTax(BigDecimal amount, String countryCode, String region) {
    if (amount == null || amount.signum() <= 0 || countryCode == null) {
      return new TaxBreakdown(amount == null ? BigDecimal.ZERO : amount, BigDecimal.ZERO, List.of());
    }
    var zone = zoneRepo.findByCountryCodeAndRegion(countryCode, region == null ? "" : region)
        .orElseGet(() -> zoneRepo.findByCountryCode(countryCode).stream().findFirst().orElse(null));
    if (zone == null) {
      log.debug("No tax zone matched for {}/{}", countryCode, region);
      return new TaxBreakdown(amount, BigDecimal.ZERO, List.of());
    }
    var rules = ruleRepo.findByTaxZoneIdAndActiveTrueOrderByPriorityAsc(zone.getTaxZoneId());
    BigDecimal taxableBase = amount;
    BigDecimal totalTax = BigDecimal.ZERO;
    var applied = new java.util.ArrayList<AppliedTax>();
    for (TaxRule r : rules) {
      var tax = taxableBase.multiply(r.getRate()).setScale(2, RoundingMode.HALF_UP);
      totalTax = totalTax.add(tax);
      applied.add(new AppliedTax(r.getName(), r.getRate(), tax));
      if (Boolean.TRUE.equals(r.getCompound())) {
        taxableBase = taxableBase.add(tax);
      }
    }
    return new TaxBreakdown(amount, totalTax, applied);
  }
}
