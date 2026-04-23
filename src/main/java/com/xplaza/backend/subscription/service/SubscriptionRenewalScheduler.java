/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.subscription.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.catalog.domain.repository.ProductRepository;
import com.xplaza.backend.common.events.DomainEventPublisher;
import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.order.service.CustomerOrderService;
import com.xplaza.backend.order.service.CustomerOrderService.SubscriptionOrderLine;
import com.xplaza.backend.subscription.domain.entity.Subscription;
import com.xplaza.backend.subscription.domain.entity.SubscriptionItem;
import com.xplaza.backend.subscription.domain.repository.SubscriptionItemRepository;
import com.xplaza.backend.subscription.domain.repository.SubscriptionRepository;

/**
 * Hourly worker that walks every {@link Subscription} due for renewal and mints
 * a new {@link com.xplaza.backend.order.domain.entity.CustomerOrder} for each.
 * On success the scheduler advances {@code nextRenewalAt} and publishes
 * {@link DomainEvents.SubscriptionRenewed}; on failure the attempt is retried
 * up to {@link #MAX_RETRIES} times before the subscription is moved to
 * {@code FAILED} and a {@link DomainEvents.SubscriptionRenewalFailed} event is
 * emitted so ops/CS tooling can surface the incident.
 *
 * <p>
 * Each subscription is processed in its own transaction (via a self-injected
 * {@code @Lazy} reference) so a failure on one row never rolls back the
 * progress made on the others in the same tick.
 */
@Component
@Slf4j
public class SubscriptionRenewalScheduler {

  private static final int MAX_RETRIES = 3;

  private final SubscriptionRepository subscriptionRepository;
  private final SubscriptionItemRepository subscriptionItemRepository;
  private final SubscriptionService subscriptionService;
  private final CustomerOrderService customerOrderService;
  private final ProductRepository productRepository;
  private final DomainEventPublisher eventPublisher;
  private final SubscriptionRenewalScheduler self;

  public SubscriptionRenewalScheduler(SubscriptionRepository subscriptionRepository,
      SubscriptionItemRepository subscriptionItemRepository,
      SubscriptionService subscriptionService,
      CustomerOrderService customerOrderService,
      ProductRepository productRepository,
      DomainEventPublisher eventPublisher,
      @org.springframework.context.annotation.Lazy SubscriptionRenewalScheduler self) {
    this.subscriptionRepository = subscriptionRepository;
    this.subscriptionItemRepository = subscriptionItemRepository;
    this.subscriptionService = subscriptionService;
    this.customerOrderService = customerOrderService;
    this.productRepository = productRepository;
    this.eventPublisher = eventPublisher;
    this.self = self;
  }

  @Scheduled(cron = "${subscription.renewal-cron:0 5 * * * *}")
  public void renewDueSubscriptions() {
    var due = subscriptionRepository.findDueForRenewal(Instant.now());
    if (due.isEmpty()) {
      return;
    }
    log.info("Subscription renewal: {} due", due.size());
    for (Subscription sub : due) {
      try {
        // Each subscription gets its own transaction so one bad row does not
        // roll the entire tick back.
        self.renewSingleInTransaction(sub.getId());
      } catch (Exception e) {
        log.error("Subscription {} renewal tick failed: {}", sub.getId(), e.toString(), e);
      }
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void renewSingleInTransaction(Long subscriptionId) {
    var sub = subscriptionRepository.findById(subscriptionId).orElse(null);
    if (sub == null || sub.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
      return;
    }
    try {
      renewOne(sub);
    } catch (Exception e) {
      handleFailure(sub, e);
    }
  }

  private void renewOne(Subscription sub) {
    Instant now = Instant.now();
    var items = subscriptionItemRepository.findBySubscriptionId(sub.getId());
    if (items.isEmpty()) {
      log.warn("Subscription {} has no items; skipping renewal and cancelling", sub.getId());
      sub.setStatus(Subscription.SubscriptionStatus.CANCELLED);
      sub.setCancelledAt(now);
      subscriptionRepository.save(sub);
      return;
    }

    List<SubscriptionOrderLine> lines = new ArrayList<>(items.size());
    for (SubscriptionItem item : items) {
      var product = productRepository.findById(item.getProductId()).orElse(null);
      if (product == null) {
        // Skip items whose product has been removed — we log so operators
        // can investigate, but do not fail the whole renewal.
        log.warn("Subscription {} references deleted product {}; skipping line",
            sub.getId(), item.getProductId());
        continue;
      }
      Long shopId = product.getShop() != null ? product.getShop().getShopId() : null;
      lines.add(new SubscriptionOrderLine(
          item.getProductId(),
          shopId,
          product.getProductName(),
          item.getQuantity(),
          item.getUnitPrice()));
    }
    if (lines.isEmpty()) {
      throw new IllegalStateException(
          "Subscription " + sub.getId() + " renewed with zero valid line items");
    }

    var order = customerOrderService.createSubscriptionOrder(
        sub.getCustomerId(), sub.getCurrency(), lines, sub.getId());

    log.info("Subscription {} renewed: order {} ({} items, total {})",
        sub.getId(), order.getOrderId(), lines.size(), order.getGrandTotal());

    sub.setNextRenewalAt(subscriptionService.advance(now, sub));
    sub.setRetryCount(0);
    sub.setLastError(null);
    subscriptionRepository.save(sub);

    eventPublisher.publish(new DomainEvents.SubscriptionRenewed(
        UUID.randomUUID(), Instant.now(),
        sub.getId(), sub.getCustomerId(), order.getOrderId(),
        order.getGrandTotal(), order.getCurrency()));
  }

  private void handleFailure(Subscription sub, Exception e) {
    int retries = (sub.getRetryCount() == null ? 0 : sub.getRetryCount()) + 1;
    sub.setRetryCount(retries);
    sub.setLastError(truncate(e.getMessage()));
    if (retries >= MAX_RETRIES) {
      sub.setStatus(Subscription.SubscriptionStatus.FAILED);
      log.error("Subscription {} moved to FAILED after {} retries", sub.getId(), retries, e);
    } else {
      log.warn("Subscription {} renewal attempt {} failed: {}", sub.getId(), retries, e.toString());
    }
    subscriptionRepository.save(sub);
    try {
      eventPublisher.publish(new DomainEvents.SubscriptionRenewalFailed(
          UUID.randomUUID(), Instant.now(),
          sub.getId(), sub.getCustomerId(), retries, truncate(e.getMessage())));
    } catch (Exception publishFailure) {
      log.error("Failed to publish SubscriptionRenewalFailed for {}: {}",
          sub.getId(), publishFailure.toString());
    }
  }

  private static String truncate(String message) {
    if (message == null) {
      return "";
    }
    return message.length() > 500 ? message.substring(0, 500) : message;
  }
}
