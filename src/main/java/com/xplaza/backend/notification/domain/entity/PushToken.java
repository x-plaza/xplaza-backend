/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.domain.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

/**
 * A device push token registered by a customer for FCM (Android / web) or APNs
 * (iOS). The notification worker fans out via {@code platform}.
 */
@Entity
@Table(name = "push_tokens", uniqueConstraints = @UniqueConstraint(columnNames = "token"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "customer_id", nullable = false)
  private Long customerId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Platform platform;

  @Column(nullable = false, length = 500)
  private String token;

  @Column(name = "device_id", length = 120)
  private String deviceId;

  @Column(name = "created_at", nullable = false)
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "last_seen_at", nullable = false)
  @Builder.Default
  private Instant lastSeenAt = Instant.now();

  public enum Platform {
    ANDROID,
    IOS,
    WEB
  }
}
