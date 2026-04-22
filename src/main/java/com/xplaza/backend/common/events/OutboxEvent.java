/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.events;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "domain_event_outbox", indexes = {
    @Index(name = "idx_outbox_unpublished", columnList = "published_at"),
    @Index(name = "idx_outbox_event_type", columnList = "event_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_id", nullable = false, unique = true, length = 36)
  private String eventId;

  @Column(name = "event_type", nullable = false, length = 100)
  private String eventType;

  @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
  private String payload;

  @Column(name = "occurred_at", nullable = false)
  private Instant occurredAt;

  @Column(name = "published_at")
  private Instant publishedAt;

  @Column(name = "retries", nullable = false)
  private int retries;

  @Column(name = "last_error", length = 1000)
  private String lastError;

  public static OutboxEvent of(DomainEvent event, String payload) {
    return OutboxEvent.builder()
        .eventId(event.eventId().toString())
        .eventType(event.type())
        .payload(payload)
        .occurredAt(event.occurredAt())
        .retries(0)
        .build();
  }

  public static OutboxEvent newId(DomainEvent event, String payload) {
    var e = of(event, payload);
    if (e.eventId == null)
      e.eventId = UUID.randomUUID().toString();
    return e;
  }
}
