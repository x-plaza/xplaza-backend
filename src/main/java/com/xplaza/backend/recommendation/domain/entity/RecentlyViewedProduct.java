/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.recommendation.domain.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "recently_viewed_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RecentlyViewedProduct.PK.class)
public class RecentlyViewedProduct {

  @Id @Column(name = "customer_id") private Long customerId;
  @Id @Column(name = "product_id") private Long productId;

  @Column(name = "viewed_at")
  private Instant viewedAt;

  @lombok.Data
  @lombok.NoArgsConstructor
  @lombok.AllArgsConstructor
  public static class PK implements java.io.Serializable {
    private Long customerId;
    private Long productId;
  }
}
