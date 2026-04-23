/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.auth.service;

import java.time.Duration;
import java.time.Instant;

/**
 * Pure-function policy controlling progressive lockout. Behaviour:
 * <ul>
 * <li>{@code 0..4} failed attempts → no lockout</li>
 * <li>{@code 5..7} failed attempts → fixed 5-minute lockout</li>
 * <li>{@code 8+} failed attempts → escalating geometric back-off, doubling each
 * attempt and capped at 1 hour</li>
 * </ul>
 *
 * <p>
 * The geometric tier intentionally starts at {@code 2^(failedAttempts - 5)} so
 * the first escalation (8 attempts) yields 8 minutes — strictly greater than
 * the previous 5-minute tier — keeping the lockout monotonically non-decreasing
 * as failures pile up. A {@link Math#max} floor of 5 minutes is also applied as
 * a defence-in-depth guard against future tweaks that could otherwise regress
 * the floor.
 */
public final class AccountLockoutPolicy {
  private static final long FIXED_TIER_MINUTES = 5L;
  private static final long MAX_LOCKOUT_MINUTES = 60L;

  private AccountLockoutPolicy() {
  }

  public static Instant computeLockedUntil(int failedAttempts) {
    if (failedAttempts < 5) {
      return null;
    }
    if (failedAttempts < 8) {
      return Instant.now().plus(Duration.ofMinutes(FIXED_TIER_MINUTES));
    }
    long geometric = (long) Math.pow(2, failedAttempts - 5);
    long minutes = Math.max(FIXED_TIER_MINUTES, Math.min(MAX_LOCKOUT_MINUTES, geometric));
    return Instant.now().plus(Duration.ofMinutes(minutes));
  }
}
