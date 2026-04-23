/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.b2b.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "price_list_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "price_list_id", "product_id", "min_quantity" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceListItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "price_list_id", nullable = false)
  private Long priceListId;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "min_quantity", nullable = false)
  @Builder.Default
  private Integer minQuantity = 1;

  @Column(name = "unit_price", nullable = false, precision = 14, scale = 2)
  private BigDecimal unitPrice;

  @Column(name = "notes", length = 500)
  private String notes;
}
