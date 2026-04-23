/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.auth.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AccountLockoutPolicyTest {

  @ParameterizedTest(name = "{0} failed attempts → no lockout")
  @CsvSource({ "0", "1", "2", "3", "4" })
  void belowThreshold_returnsNull(int attempts) {
    assertNull(AccountLockoutPolicy.computeLockedUntil(attempts));
  }

  /**
   * Regression test for the "5 → 2 minute" drop bug. Lockout duration must be
   * monotonically non-decreasing as failures accumulate, so an attacker can never
   * reduce their effective lockout by typing one more wrong password.
   */
  @ParameterizedTest(name = "{0} attempts ⇒ {1} minutes (monotonic, ≥5)")
  @CsvSource({
      "5,  5",
      "6,  5",
      "7,  5",
      "8,  8",
      "9,  16",
      "10, 32",
      "11, 60",
      "12, 60",
      "100, 60"
  })
  void monotonicProgression(int attempts, long expectedMinutes) {
    Instant before = Instant.now();
    Instant lockedUntil = AccountLockoutPolicy.computeLockedUntil(attempts);
    assertNotNull(lockedUntil);
    // The policy returns `Instant.now() + duration`, captured on its own
    // clock read between our `before` and a later `Instant.now()`. So the
    // delta from `before` is in the half-open interval [duration, duration+ε)
    // for some small ε. We assert the seconds-level truncation matches the
    // expected minute boundary exactly.
    long actualSeconds = Duration.between(before, lockedUntil).getSeconds();
    long expectedSeconds = expectedMinutes * 60L;
    assertTrue(actualSeconds >= expectedSeconds,
        () -> attempts + " attempts produced lockout shorter than expected: "
            + actualSeconds + "s < " + expectedSeconds + "s");
    assertTrue(actualSeconds < expectedSeconds + 5L,
        () -> attempts + " attempts produced lockout suspiciously longer than expected: "
            + actualSeconds + "s vs " + expectedSeconds + "s");
  }

  @Test
  void lockoutNeverShrinksAsAttemptsGrow() {
    long previousSeconds = -1;
    for (int attempts = 5; attempts <= 30; attempts++) {
      Instant before = Instant.now();
      Instant lockedUntil = AccountLockoutPolicy.computeLockedUntil(attempts);
      assertNotNull(lockedUntil);
      long seconds = Duration.between(before, lockedUntil).getSeconds();
      // Each subsequent attempt must lock the account for at least as long
      // as the previous attempt — never less. This is the property the
      // original `Math.pow(2, n - 7)` violated at n=8 (5min → 2min drop).
      final long capturedPrev = previousSeconds;
      final long capturedCur = seconds;
      final int capturedAttempts = attempts;
      assertTrue(seconds >= previousSeconds,
          () -> "lockout shrank from " + capturedPrev + "s to " + capturedCur
              + "s at attempts=" + capturedAttempts);
      previousSeconds = seconds;
    }
  }
}
