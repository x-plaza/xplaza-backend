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

  @Transactional
  public PushToken registerToken(Long customerId, PushToken.Platform platform, String token, String deviceId) {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException("Token must not be blank");
    }
    if (customerId == null) {
      throw new IllegalArgumentException("customerId is required");
    }
    var existing = tokenRepo.findByToken(token).orElse(null);
    if (existing != null) {
      if (!customerId.equals(existing.getCustomerId())) {
        log.warn("Rejecting push token re-registration: token already bound to a different customer "
            + "(attemptedBy={}, boundTo={}, tokenId={})",
            customerId, existing.getCustomerId(), existing.getId());
        throw new org.springframework.security.access.AccessDeniedException(
            "Push token is already registered to a different customer");
      }
      existing.setPlatform(platform);
      existing.setDeviceId(deviceId);
      existing.setLastSeenAt(Instant.now());
      return tokenRepo.save(existing);
    }
    return tokenRepo.save(PushToken.builder()
        .customerId(customerId)
        .platform(platform)
        .token(token)
        .deviceId(deviceId)
        .createdAt(Instant.now())
        .lastSeenAt(Instant.now())
        .build());
  }

  @Transactional
  public void unregisterToken(Long customerId, String token) {
    if (customerId == null || token == null || token.isBlank()) {
      return;
    }
    tokenRepo.findByToken(token)
        .filter(t -> customerId.equals(t.getCustomerId()))
        .ifPresent(tokenRepo::delete);
  }

  @Transactional(readOnly = true)
  public List<PushToken> tokensForCustomer(Long customerId) {
    return tokenRepo.findByCustomerId(customerId);
  }

  @Async
  public void sendToCustomer(Long customerId, String title, String body) {
    var tokens = tokensForCustomer(customerId);
    if (tokens.isEmpty()) {
      log.debug("No push tokens for customer {}, skipping push (title length={})",
          customerId, length(title));
      return;
    }
    for (PushToken pt : tokens) {
      dispatch(pt, title, body);
    }
  }

  private void dispatch(PushToken token, String title, String body) {
    Long tokenId = token.getId();
    switch (token.getPlatform()) {
    case ANDROID, WEB -> {
      if (fcmEnabled) {
        // TODO: integrate Firebase Admin SDK; intentionally left out of OSS
        // build because credentials are deployment-specific.
        log.info("[FCM] -> tokenId={} title length={} body length={}",
            tokenId, length(title), length(body));
      } else {
        log.debug("FCM disabled, skipping push to tokenId={}", tokenId);
      }
    }
    case IOS -> {
      if (apnsEnabled) {
        // TODO: integrate Pushy or apns-http2 client.
        log.info("[APNs] -> tokenId={} title length={} body length={}",
            tokenId, length(title), length(body));
      } else {
        log.debug("APNs disabled, skipping push to tokenId={}", tokenId);
      }
    }
    default -> log.warn("Unknown platform {} for tokenId {}", token.getPlatform(), tokenId);
    }
  }

  private static int length(String s) {
    return s == null ? 0 : s.length();
  }
}
