/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Publishes a {@link DomainEvent} both as a transactional outbox row and as a
 * Spring application event so in-process listeners can subscribe with
 * {@code @TransactionalEventListener(phase = AFTER_COMMIT)}. The outbox row is
 * the source of truth — even if the application crashes after commit, the
 * relay worker will retry delivery.
 */
@Component
public class DomainEventPublisher {

  private static final Logger log = LoggerFactory.getLogger(DomainEventPublisher.class);

  private final OutboxEventRepository outbox;
  private final ApplicationEventPublisher springPublisher;
  private final ObjectMapper objectMapper;

  public DomainEventPublisher(OutboxEventRepository outbox,
      ApplicationEventPublisher springPublisher,
      ObjectMapper objectMapper) {
    this.outbox = outbox;
    this.springPublisher = springPublisher;
    this.objectMapper = objectMapper;
  }

  @Transactional(propagation = Propagation.MANDATORY)
  public void publish(DomainEvent event) {
    try {
      String payload = objectMapper.writeValueAsString(event);
      outbox.save(OutboxEvent.newId(event, payload));
      springPublisher.publishEvent(event);
    } catch (Exception e) {
      log.error("Failed to publish domain event {}: {}", event.type(), e.getMessage());
      throw new IllegalStateException("Failed to enqueue domain event", e);
    }
  }
}
