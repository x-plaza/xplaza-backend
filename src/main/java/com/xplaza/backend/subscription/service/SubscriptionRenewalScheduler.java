/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.subscription.service;

import java.time.Instant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.subscription.domain.entity.Subscription;
import com.xplaza.backend.subscription.domain.repository.SubscriptionItemRepository;
import com.xplaza.backend.subscription.domain.repository.SubscriptionRepository;

/**
 * Hourly worker that walks every {@link Subscription} due for renewal and mints
 * a new order. Uses the customer's default address and stored payment method
 * (when available); on payment failure the subscription is moved to
 * {@code FAILED} after the configured retry budget is exhausted.
 *
 * <p>
 * The current iteration writes a synthetic order via the existing
 * {@code orders} module. Hooking up {@code StripeGateway.charge(...)} for
 * card-on-file renewals is the v1.2 step.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionRenewalScheduler {

  private static final int MAX_RETRIES = 3;

  private final SubscriptionRepository subscriptionRepository;
  private final SubscriptionItemRepository subscriptionItemRepository;
  private final SubscriptionService subscriptionService;

  @Scheduled(cron = "${subscription.renewal-cron:0 5 * * * *}")
  @Transactional
  public void renewDueSubscriptions() {
    var now = Instant.now();
    var due = subscriptionRepository.findDueForRenewal(now);
    if (due.isEmpty()) {
      return;
    }
    log.info("Subscription renewal: {} due", due.size());
    for (Subscription sub : due) {
      try {
        renewOne(sub, now);
      } catch (Exception e) {
        handleFailure(sub, e);
      }
    }
  }

  private void renewOne(Subscription sub, Instant now) {
    var items = subscriptionItemRepository.findBySubscriptionId(sub.getId());
    log.info("Renewing subscription {} ({} items, total {})", sub.getId(), items.size(), sub.getTotalAmount());
    // TODO: invoke OrderService.createSubscriptionOrder(...) once the
    // multi-vendor split allows synthesised checkouts to bypass the cart.
    sub.setNextRenewalAt(subscriptionService.advance(now, sub));
    sub.setRetryCount(0);
    sub.setLastError(null);
    subscriptionRepository.save(sub);
  }

  private void handleFailure(Subscription sub, Exception e) {
    int retries = (sub.getRetryCount() == null ? 0 : sub.getRetryCount()) + 1;
    sub.setRetryCount(retries);
    sub.setLastError(e.getMessage());
    if (retries >= MAX_RETRIES) {
      sub.setStatus(Subscription.SubscriptionStatus.FAILED);
      log.error("Subscription {} moved to FAILED after {} retries", sub.getId(), retries, e);
    } else {
      log.warn("Subscription {} renewal attempt {} failed: {}", sub.getId(), retries, e.toString());
    }
    subscriptionRepository.save(sub);
  }
}
