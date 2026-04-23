/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.subscription.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

/**
 * Recurring subscription. Tracks the renewal schedule and lifecycle so the
 * {@code SubscriptionRenewalScheduler} can mint a new order on each cycle.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "customer_id", nullable = false)
  private Long customerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  @Builder.Default
  private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

  @Enumerated(EnumType.STRING)
  @Column(name = "interval_unit", nullable = false, length = 20)
  private IntervalUnit intervalUnit;

  @Column(name = "interval_count", nullable = false)
  @Builder.Default
  private Integer intervalCount = 1;

  @Column(name = "currency", nullable = false, length = 3)
  @Builder.Default
  private String currency = "USD";

  @Column(name = "total_amount", precision = 14, scale = 2)
  private BigDecimal totalAmount;

  @Column(name = "next_renewal_at")
  private Instant nextRenewalAt;

  @Column(name = "cancelled_at")
  private Instant cancelledAt;

  @Column(name = "gateway_customer_id", length = 100)
  private String gatewayCustomerId;

  @Column(name = "retry_count", nullable = false)
  @Builder.Default
  private Integer retryCount = 0;

  @Column(name = "last_error", length = 500)
  private String lastError;

  @Column(name = "created_at", nullable = false)
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  private Instant updatedAt = Instant.now();

  // Bidirectional association: SubscriptionItem.subscription is the owning
  // side. A unidirectional @JoinColumn here would make Hibernate INSERT child
  // rows with a NULL FK and then UPDATE, which fails the NOT NULL constraint
  // on `subscription_id`. The `addItem` helper keeps both sides in sync so
  // callers can just mutate `items` and let CascadeType.ALL persist them.
  @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  private List<SubscriptionItem> items = new ArrayList<>();

  /**
   * Attach a child item to this subscription while keeping the bidirectional
   * back-reference consistent (required for the owning-side INSERT to carry a
   * non-null {@code subscription_id}).
   */
  public void addItem(SubscriptionItem item) {
    item.setSubscription(this);
    items.add(item);
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public enum SubscriptionStatus {
    ACTIVE,
    PAUSED,
    CANCELLED,
    FAILED
  }

  public enum IntervalUnit {
    DAY,
    WEEK,
    MONTH,
    YEAR
  }
}
