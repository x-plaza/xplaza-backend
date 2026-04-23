/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.idempotency;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "idempotency_keys", indexes = {
    @Index(name = "idx_idem_expires", columnList = "expires_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyKey {

  @Id
  @Column(name = "key_value", length = 200)
  private String key;

  @Column(name = "endpoint", nullable = false, length = 200)
  private String endpoint;

  @Column(name = "request_hash", length = 128)
  private String requestHash;

  @Column(name = "response_status")
  private Integer responseStatus;

  @Column(name = "response_body", columnDefinition = "TEXT")
  private String responseBody;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;
}
