/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.subscription.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.common.events.DomainEventPublisher;
import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.subscription.domain.entity.Subscription;
import com.xplaza.backend.subscription.domain.entity.SubscriptionItem;
import com.xplaza.backend.subscription.domain.repository.SubscriptionRepository;

/**
 * Subscription lifecycle service. Pause/resume/cancel are immediate; renewal is
 * driven by {@link SubscriptionRenewalScheduler}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final DomainEventPublisher eventPublisher;

  public Subscription create(Long customerId, Subscription.IntervalUnit unit, int count, String currency,
      List<SubscriptionItem> items) {
    var sub = Subscription.builder()
        .customerId(customerId)
        .intervalUnit(unit)
        .intervalCount(count)
        .currency(currency)
        .status(Subscription.SubscriptionStatus.ACTIVE)
        .nextRenewalAt(Instant.now())
        .build();
    BigDecimal total = items.stream()
        .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    sub.setTotalAmount(total);
    items.forEach(sub::addItem);
    var saved = subscriptionRepository.save(sub);
    eventPublisher.publish(new DomainEvents.SubscriptionCreated(
        UUID.randomUUID(), Instant.now(), saved.getId(), customerId));
    return saved;
  }

  @Transactional(readOnly = true)
  public List<Subscription> listForCustomer(Long customerId) {
    return subscriptionRepository.findByCustomerId(customerId);
  }

  @Transactional(readOnly = true)
  public Subscription get(Long id) {
    return subscriptionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Subscription not found: " + id));
  }

  public Subscription pause(Long id) {
    var s = get(id);
    if (s.getStatus() != Subscription.SubscriptionStatus.ACTIVE) {
      throw new IllegalStateException("Only active subscriptions can be paused");
    }
    s.setStatus(Subscription.SubscriptionStatus.PAUSED);
    return subscriptionRepository.save(s);
  }

  public Subscription resume(Long id) {
    var s = get(id);
    if (s.getStatus() != Subscription.SubscriptionStatus.PAUSED) {
      throw new IllegalStateException("Only paused subscriptions can be resumed");
    }
    s.setStatus(Subscription.SubscriptionStatus.ACTIVE);
    s.setNextRenewalAt(advance(Instant.now(), s));
    return subscriptionRepository.save(s);
  }

  public Subscription cancel(Long id) {
    var s = get(id);
    s.setStatus(Subscription.SubscriptionStatus.CANCELLED);
    s.setCancelledAt(Instant.now());
    return subscriptionRepository.save(s);
  }

  public Instant advance(Instant from, Subscription subscription) {
    int n = subscription.getIntervalCount() == null ? 1 : subscription.getIntervalCount();
    return switch (subscription.getIntervalUnit()) {
    case DAY -> from.plus(n, ChronoUnit.DAYS);
    case WEEK -> from.plus(7L * n, ChronoUnit.DAYS);
    case MONTH -> from.plus(30L * n, ChronoUnit.DAYS);
    case YEAR -> from.plus(365L * n, ChronoUnit.DAYS);
    };
  }
}
