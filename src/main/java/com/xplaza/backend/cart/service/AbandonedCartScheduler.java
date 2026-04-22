/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cart.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.cart.domain.repository.CartRepository;
import com.xplaza.backend.common.events.DomainEventPublisher;
import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.customer.domain.repository.CustomerRepository;

/**
 * Periodically marks inactive carts as abandoned and emits an event so the
 * notifications module can send a recovery email. Runs hourly by default.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AbandonedCartScheduler {

  private final CartRepository cartRepository;
  private final CustomerRepository customerRepository;
  private final DomainEventPublisher eventPublisher;

  @Value("${cart.abandoned-after-hours:4}")
  private int abandonedAfterHours;

  @Scheduled(cron = "${cart.abandoned-cron:0 15 * * * *}")
  @Transactional
  public void detectAbandoned() {
    var threshold = Instant.now().minus(Duration.ofHours(abandonedAfterHours));
    var inactive = cartRepository.findInactiveCarts(threshold);
    if (inactive.isEmpty()) return;
    log.info("Detected {} abandoned carts (threshold: {}h)", inactive.size(), abandonedAfterHours);
    for (var cart : inactive) {
      cart.setStatus(com.xplaza.backend.cart.domain.entity.Cart.CartStatus.ABANDONED);
      cartRepository.save(cart);
      String email = null;
      if (cart.getCustomerId() != null) {
        email = customerRepository.findById(cart.getCustomerId()).map(c -> c.getEmail()).orElse(null);
      }
      eventPublisher.publish(new DomainEvents.CartAbandoned(
          UUID.randomUUID(), Instant.now(), cart.getId().getMostSignificantBits(),
          cart.getCustomerId(), email));
    }
  }
}
