/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.idempotency;

import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service exposing safe at-most-once semantics for mutating endpoints. Callers
 * pass an idempotency key (from the {@code Idempotency-Key} header). If the
 * same key is reused for the same endpoint and request body, the prior stored
 * response is returned instead of repeating the side-effect.
 */
@Service
public class IdempotencyService {

  private static final Duration DEFAULT_TTL = Duration.ofHours(24);

  private final IdempotencyKeyRepository repo;

  public IdempotencyService(IdempotencyKeyRepository repo) {
    this.repo = repo;
  }

  /**
   * Returns an existing record if present, or {@link Optional#empty()} when the
   * key is fresh and the caller may proceed with the write.
   */
  @Transactional(readOnly = true)
  public Optional<IdempotencyKey> find(String key) {
    if (key == null || key.isBlank())
      return Optional.empty();
    return repo.findById(key);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public IdempotencyKey reserve(String key, String endpoint, String requestBody) {
    var record = IdempotencyKey.builder()
        .key(key)
        .endpoint(endpoint)
        .requestHash(hash(requestBody))
        .createdAt(Instant.now())
        .expiresAt(Instant.now().plus(DEFAULT_TTL))
        .build();
    return repo.save(record);
  }

  @Transactional
  public void persistResponse(String key, int status, String body) {
    repo.findById(key).ifPresent(rec -> {
      rec.setResponseStatus(status);
      rec.setResponseBody(body);
      repo.save(rec);
    });
  }

  public String hash(String s) {
    if (s == null)
      return null;
    try {
      var md = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(md.digest(s.getBytes()));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  /** Periodically purge expired keys to avoid unbounded growth. */
  @Scheduled(cron = "0 0 * * * *")
  @Transactional
  public void purgeExpired() {
    repo.deleteExpired(Instant.now());
  }
}
