/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.events;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Polls the outbox and marks events as published. This loop is intentionally
 * minimal — Spring's
 * {@link org.springframework.transaction.event.TransactionalEventListener}
 * already takes care of in-process delivery; the relay's job is to flag rows as
 * delivered (so the table stays small) and to retry rows that failed earlier
 * delivery (e.g. after a process crash). Plug in an outbound bus
 * (Kafka/SNS/webhook) here when external delivery is needed.
 */
@Component
public class OutboxRelayWorker {

  private static final Logger log = LoggerFactory.getLogger(OutboxRelayWorker.class);
  private static final int BATCH = 200;

  private final OutboxEventRepository repo;

  public OutboxRelayWorker(OutboxEventRepository repo) {
    this.repo = repo;
  }

  @Scheduled(fixedDelayString = "${xplaza.outbox.relay-delay-ms:5000}")
  @Transactional
  public void relay() {
    var batch = repo.findUnpublished(PageRequest.of(0, BATCH));
    if (batch.isEmpty())
      return;
    var now = Instant.now();
    for (var ev : batch) {
      try {
        ev.setPublishedAt(now);
      } catch (Exception e) {
        ev.setRetries(ev.getRetries() + 1);
        ev.setLastError(truncate(e.getMessage()));
        log.warn("Outbox relay failed for event {}: {}", ev.getEventId(), e.getMessage());
      }
    }
    repo.saveAll(batch);
  }

  private static String truncate(String s) {
    if (s == null)
      return null;
    return s.length() > 999 ? s.substring(0, 999) : s;
  }
}
