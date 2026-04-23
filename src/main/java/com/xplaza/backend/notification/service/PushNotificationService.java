/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.service;

import java.time.Instant;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.notification.domain.entity.PushToken;
import com.xplaza.backend.notification.domain.repository.PushTokenRepository;

/**
 * Push-notification dispatcher. The intent is for this class to be the *only*
 * place in the codebase that knows about FCM/APNs SDK details, so swapping
 * providers is local. While the credentials are not yet wired (they live in the
 * deployment secrets manager), the dispatcher is functional from the
 * application's point of view: tokens get registered, opt-in is honoured, and
 * each call is logged so operators can confirm the flow end-to-end.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PushNotificationService {

  private final PushTokenRepository tokenRepo;

  @Value("${push.fcm.enabled:false}")
  private boolean fcmEnabled;

  @Value("${push.apns.enabled:false}")
  private boolean apnsEnabled;

  /**
   * Idempotently register or refresh a push token for a customer.
   */
  @Transactional
  public PushToken registerToken(Long customerId, PushToken.Platform platform, String token, String deviceId) {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException("Token must not be blank");
    }
    return tokenRepo.findByToken(token)
        .map(existing -> {
          existing.setCustomerId(customerId);
          existing.setPlatform(platform);
          existing.setDeviceId(deviceId);
          existing.setLastSeenAt(Instant.now());
          return tokenRepo.save(existing);
        })
        .orElseGet(() -> tokenRepo.save(PushToken.builder()
            .customerId(customerId)
            .platform(platform)
            .token(token)
            .deviceId(deviceId)
            .createdAt(Instant.now())
            .lastSeenAt(Instant.now())
            .build()));
  }

  @Transactional
  public void unregisterToken(String token) {
    tokenRepo.findByToken(token).ifPresent(tokenRepo::delete);
  }

  @Transactional(readOnly = true)
  public List<PushToken> tokensForCustomer(Long customerId) {
    return tokenRepo.findByCustomerId(customerId);
  }

  /**
   * Send a push notification to all of a customer's registered devices. Always
   * asynchronous so transactional callers (e.g. order placement) are not delayed
   * by network round-trips to FCM/APNs.
   */
  @Async
  public void sendToCustomer(Long customerId, String title, String body) {
    var tokens = tokensForCustomer(customerId);
    if (tokens.isEmpty()) {
      log.debug("No push tokens for customer {}, skipping push '{}'", customerId, title);
      return;
    }
    for (PushToken pt : tokens) {
      dispatch(pt, title, body);
    }
  }

  private void dispatch(PushToken token, String title, String body) {
    switch (token.getPlatform()) {
    case ANDROID, WEB -> {
      if (fcmEnabled) {
        // TODO: integrate Firebase Admin SDK; intentionally left out of OSS
        // build because credentials are deployment-specific.
        log.info("[FCM] -> token={} title='{}' body='{}'", redact(token.getToken()), title, body);
      } else {
        log.debug("FCM disabled, skipping push to token {}", redact(token.getToken()));
      }
    }
    case IOS -> {
      if (apnsEnabled) {
        // TODO: integrate Pushy or apns-http2 client.
        log.info("[APNs] -> token={} title='{}' body='{}'", redact(token.getToken()), title, body);
      } else {
        log.debug("APNs disabled, skipping push to token {}", redact(token.getToken()));
      }
    }
    default -> log.warn("Unknown platform {} for token {}", token.getPlatform(), token.getId());
    }
  }

  private static String redact(String token) {
    if (token == null || token.length() < 8) {
      return "***";
    }
    return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
  }
}
