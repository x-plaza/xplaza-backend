/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.customer.domain.repository.CustomerRepository;
import com.xplaza.backend.notification.domain.entity.Notification;
import com.xplaza.backend.notification.domain.repository.NotificationRepository;

/**
 * Service for notification operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final EmailService emailService;
  private final CustomerRepository customerRepository;

  public Notification createNotification(Long customerId, Notification.NotificationType type,
      Notification.NotificationChannel channel, String title, String message,
      Notification.ReferenceType referenceType, String referenceId) {
    Notification notification = Notification.builder()
        .customerId(customerId)
        .type(type)
        .channel(channel)
        .title(title)
        .message(message)
        .referenceType(referenceType)
        .referenceId(referenceId)
        .build();

    notification = notificationRepository.save(notification);
    log.info("Created notification for customer {}: type={}", customerId, type);

    if (channel == Notification.NotificationChannel.EMAIL) {
      customerRepository.findById(customerId).ifPresent(customer -> {
        emailService.sendEmail(customer.getEmail(), title, message);
      });
    } else if (channel == Notification.NotificationChannel.IN_APP) {
      notification.markAsSent();
      notification = notificationRepository.save(notification);
    }

    return notification;
  }

  public Notification createInAppNotification(Long customerId, Notification.NotificationType type,
      String title, String message) {
    return createNotification(customerId, type, Notification.NotificationChannel.IN_APP,
        title, message, null, null);
  }

  public Notification createOrderNotification(Long customerId, Notification.NotificationType type,
      String title, String message, String orderId) {
    return createNotification(customerId, type, Notification.NotificationChannel.IN_APP,
        title, message, Notification.ReferenceType.ORDER, orderId);
  }

  public Notification createProductNotification(Long customerId, Notification.NotificationType type,
      String title, String message, Long productId) {
    return createNotification(customerId, type, Notification.NotificationChannel.IN_APP,
        title, message, Notification.ReferenceType.PRODUCT, productId.toString());
  }

  @Transactional(readOnly = true)
  public Page<Notification> getNotifications(Long customerId, Pageable pageable) {
    return notificationRepository.findByCustomerId(customerId, pageable);
  }

  @Transactional(readOnly = true)
  public Page<Notification> getUnreadNotifications(Long customerId, Pageable pageable) {
    return notificationRepository.findUnreadByCustomerId(customerId, pageable);
  }

  @Transactional(readOnly = true)
  public long countUnread(Long customerId) {
    return notificationRepository.countUnreadByCustomerId(customerId);
  }

  public Notification markAsRead(UUID notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));
    notification.markAsRead();
    return notificationRepository.save(notification);
  }

  public int markAllAsRead(Long customerId) {
    int updated = notificationRepository.markAllAsReadByCustomerId(customerId, Instant.now());
    log.info("Marked {} notifications as read for customer {}", updated, customerId);
    return updated;
  }

  public void deleteNotification(UUID notificationId) {
    notificationRepository.deleteById(notificationId);
  }

  @Transactional(readOnly = true)
  public List<Notification> getNotificationsReadyToSend() {
    return notificationRepository.findReadyToSend(Instant.now());
  }

  public int processPendingNotifications() {
    List<Notification> pending = getNotificationsReadyToSend();
    int processed = 0;

    for (Notification notification : pending) {
      try {
        // In a real implementation, this would integrate with email/SMS/push services
        sendNotification(notification);
        notification.markAsSent();
        notificationRepository.save(notification);
        processed++;
      } catch (Exception e) {
        log.error("Failed to send notification {}: {}", notification.getNotificationId(), e.getMessage());
        notification.markAsFailed(e.getMessage());
        notificationRepository.save(notification);
      }
    }

    log.info("Processed {} pending notifications", processed);
    return processed;
  }

  private void sendNotification(Notification notification) {
    switch (notification.getChannel()) {
    case EMAIL:
      customerRepository.findById(notification.getCustomerId()).ifPresent(customer -> {
        emailService.sendEmail(customer.getEmail(), notification.getTitle(), notification.getMessage());
      });
      log.info("Sending email notification to customer {}", notification.getCustomerId());
      break;
    case SMS:
      // Placeholder for SMS integration
      log.info("Sending SMS notification to customer {} (Not implemented)", notification.getCustomerId());
      break;
    case PUSH:
      // Placeholder for push notification integration
      log.info("Sending push notification to customer {} (Not implemented)", notification.getCustomerId());
      break;
    case IN_APP:
      // Already handled
      break;
    }
  }

  public int cleanupOldNotifications(int daysOld) {
    Instant cutoff = Instant.now().minus(daysOld, ChronoUnit.DAYS);
    int deleted = notificationRepository.deleteOlderThan(cutoff);
    log.info("Deleted {} old notifications older than {} days", deleted, daysOld);
    return deleted;
  }

  @Transactional(readOnly = true)
  public List<Notification> getNotificationsByReference(Notification.ReferenceType type, String refId) {
    return notificationRepository.findByReference(type, refId);
  }
}
