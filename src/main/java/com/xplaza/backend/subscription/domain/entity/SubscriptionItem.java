/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.subscription.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "subscription_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Bidirectional ManyToOne is the owning side of the FK so the INSERT statement
  // already carries the subscription_id — an earlier unidirectional @JoinColumn
  // on the parent would INSERT NULL and then UPDATE, which fails the NOT NULL
  // check on `subscription_id`. Lazy + optional=false lets Hibernate avoid
  // dereferencing the parent when rendering the FK.
  // @JsonIgnore prevents the serializer from walking back up into the parent
  // Subscription and blowing the max depth limit: Subscription.items →
  // SubscriptionItem.subscription → Subscription.items → ... endlessly.
  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "subscription_id", nullable = false)
  private Subscription subscription;

  // Read-only scalar FK kept so SubscriptionItemRepository.findBySubscriptionId
  // and other query-time callers don't have to navigate the association. The
  // ManyToOne above owns the write.
  @Column(name = "subscription_id", insertable = false, updatable = false)
  private Long subscriptionId;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "quantity", nullable = false)
  @Builder.Default
  private Integer quantity = 1;

  @Column(name = "unit_price", nullable = false, precision = 14, scale = 2)
  private BigDecimal unitPrice;
}
