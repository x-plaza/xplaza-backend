/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.domain.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {
  @Id @Column(name = "customer_id") private Long customerId;

  @Column(name = "email_enabled") @Builder.Default private Boolean emailEnabled = true;
  @Column(name = "sms_enabled") @Builder.Default private Boolean smsEnabled = true;
  @Column(name = "push_enabled") @Builder.Default private Boolean pushEnabled = true;
  @Column(name = "marketing_emails") @Builder.Default private Boolean marketingEmails = false;
  @Column(name = "order_updates") @Builder.Default private Boolean orderUpdates = true;
  @Column(name = "promotional_offers") @Builder.Default private Boolean promotionalOffers = false;
  @Column(name = "newsletter") @Builder.Default private Boolean newsletter = false;
}
