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

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "subscription_id", nullable = false)
  private Subscription subscription;

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
