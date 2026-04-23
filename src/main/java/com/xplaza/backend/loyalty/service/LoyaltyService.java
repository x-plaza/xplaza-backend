/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.loyalty.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.common.events.DomainEventPublisher;
import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.customer.domain.repository.CustomerRepository;

/**
 * Loyalty engine — accrual on completed orders and redemption at checkout. Tier
 * thresholds and earn/redemption ratios come from configuration so they can be
 * tuned without redeploying.
 */
@Service
@Slf4j
public class LoyaltyService {

  private final CustomerRepository customerRepository;
  private final DomainEventPublisher eventPublisher;
  private final LoyaltyService self;

  public LoyaltyService(CustomerRepository customerRepository,
      DomainEventPublisher eventPublisher,
      @Lazy LoyaltyService self) {
    this.customerRepository = customerRepository;
    this.eventPublisher = eventPublisher;
    this.self = self;
  }

  @Value("${loyalty.points.earn-rate:1}")
  private long earnRate; // points per currency unit spent

  @Value("${loyalty.points.redemption-rate:0.01}")
  private BigDecimal redemptionRate; // currency value per point

  @Transactional
  public long accrue(Long customerId, BigDecimal amountSpent, UUID orderId) {
    var customer = customerRepository.findById(customerId).orElseThrow();
    long points = amountSpent.multiply(BigDecimal.valueOf(earnRate))
        .setScale(0, RoundingMode.DOWN).longValue();
    customer.setLoyaltyPoints((customer.getLoyaltyPoints() == null ? 0L : customer.getLoyaltyPoints()) + points);
    customer.setLoyaltyTier(determineTier(customer.getLoyaltyPoints()));
    customerRepository.save(customer);
    eventPublisher.publish(new DomainEvents.LoyaltyPointsEarned(
        UUID.randomUUID(), Instant.now(), customerId, points, "order:" + orderId));
    return points;
  }

  @Transactional
  public long grantPoints(Long customerId, long points, String reason) {
    if (points <= 0) {
      return 0L;
    }
    var customer = customerRepository.findById(customerId).orElseThrow();
    long newBalance = (customer.getLoyaltyPoints() == null ? 0L : customer.getLoyaltyPoints()) + points;
    customer.setLoyaltyPoints(newBalance);
    customer.setLoyaltyTier(determineTier(newBalance));
    customerRepository.save(customer);
    eventPublisher.publish(new DomainEvents.LoyaltyPointsEarned(
        UUID.randomUUID(), Instant.now(), customerId, points, reason));
    return points;
  }

  @Transactional
  public BigDecimal redeem(Long customerId, long points, UUID orderId) {
    var customer = customerRepository.findById(customerId).orElseThrow();
    long balance = customer.getLoyaltyPoints() == null ? 0L : customer.getLoyaltyPoints();
    if (points > balance) {
      throw new IllegalArgumentException("insufficient_loyalty_balance");
    }
    customer.setLoyaltyPoints(balance - points);
    customerRepository.save(customer);
    var value = BigDecimal.valueOf(points).multiply(redemptionRate);
    eventPublisher.publish(new DomainEvents.LoyaltyPointsRedeemed(
        UUID.randomUUID(), Instant.now(), customerId, points, orderId));
    return value;
  }

  String determineTier(long points) {
    if (points >= 10000) {
      return "PLATINUM";
    }
    if (points >= 5000) {
      return "GOLD";
    }
    if (points >= 1000) {
      return "SILVER";
    }
    return "BRONZE";
  }

  @Async
  @EventListener
  public void on(DomainEvents.OrderPlaced event) {
    if (event.customerId() == null) {
      return;
    }
    try {
      self.accrue(event.customerId(), event.total(), event.orderId());
    } catch (Exception e) {
      log.error("Loyalty accrual failed for order {} (customer {}): {}",
          event.orderId(), event.customerId(), e.getMessage(), e);
    }
  }
}
