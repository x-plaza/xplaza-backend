/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.recommendation.domain.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "product_co_purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ProductCoPurchase.PK.class)
public class ProductCoPurchase {

  @Id
  @Column(name = "product_id")
  private Long productId;
  @Id
  @Column(name = "co_product_id")
  private Long coProductId;

  @Column(name = "co_purchase_count")
  private Long coPurchaseCount;

  @lombok.Data
  @lombok.NoArgsConstructor
  @lombok.AllArgsConstructor
  public static class PK implements java.io.Serializable {
    private Long productId;
    private Long coProductId;
  }
}
