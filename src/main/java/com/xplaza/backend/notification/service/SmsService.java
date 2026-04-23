/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.telesign.MessagingClient;

/**
 * Thin Telesign-backed SMS sender. The Telesign SDK is optional at runtime — if
 * credentials are missing the service logs and returns gracefully so a missing
 * third-party config never blocks order placement.
 */
@Service
@Slf4j
public class SmsService {

  @Value("${telesign.customer-id:}")
  private String customerId;

  @Value("${telesign.api-key:}")
  private String apiKey;

  @Value("${telesign.message-type:ARN}")
  private String messageType;

  public boolean send(String phoneNumberE164, String message) {
    if (customerId == null || customerId.isBlank() || apiKey == null || apiKey.isBlank()) {
      log.warn("Telesign credentials not configured; SMS to {} skipped.", phoneNumberE164);
      return false;
    }
    try {
      var client = new MessagingClient(customerId, apiKey);
      var response = client.message(phoneNumberE164, message, messageType, null);
      log.info("Telesign SMS sent (status_code={})", response.statusCode);
      return true;
    } catch (Exception e) {
      log.error("Telesign SMS failed for {}", phoneNumberE164, e);
      return false;
    }
  }
}
