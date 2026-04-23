/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.vendor.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job that collects pending sales per vendor and triggers a
 * disbursement (Stripe Connect or bank transfer). The actual payout adapter
 * lives in the payment module — this scheduler is the trigger.
 */
@Component
@Slf4j
public class VendorPayoutScheduler {

  @Value("${marketplace.payout-cron:0 0 2 * * MON}")
  private String cron;

  @Scheduled(cron = "${marketplace.payout-cron:0 0 2 * * MON}")
  public void runWeeklyPayouts() {
    log.info("Vendor payout job starting");
    // TODO: query pending payouts table, settle for each vendor.
    log.info("Vendor payout job completed");
  }
}
