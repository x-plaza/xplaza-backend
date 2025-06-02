/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "discount_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountTypeDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long discountTypeId;

  private String discountTypeName;
  private String discountTypeDescription;

  @OneToMany(mappedBy = "discountType", fetch = FetchType.LAZY)
  private List<ProductDiscountDao> productDiscounts = new ArrayList<>();
}