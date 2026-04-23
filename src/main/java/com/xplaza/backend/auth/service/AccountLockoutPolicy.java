/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.auth.service;

import java.time.Duration;
import java.time.Instant;

/**
 * Pure-function policy controlling progressive lockout. Behaviour: - 0..4
 * failed attempts: no lockout - 5..7 failed attempts: 5-minute lockout - 8+
 * failed attempts: 1-hour lockout (escalating geometric back-off)
 */
public final class AccountLockoutPolicy {
  private AccountLockoutPolicy() {
  }

  public static Instant computeLockedUntil(int failedAttempts) {
    if (failedAttempts < 5) {
      return null;
    }
    if (failedAttempts < 8) {
      return Instant.now().plus(Duration.ofMinutes(5));
    }
    long minutes = (long) Math.min(60, Math.pow(2, failedAttempts - 7));
    return Instant.now().plus(Duration.ofMinutes(minutes));
  }
}
