/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "discount_types")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountTypeDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long discountTypeId;

  String discountTypeName;

  String discountTypeDescription;

  @OneToMany(mappedBy = "discountType", fetch = FetchType.LAZY)
  List<ProductDiscountDao> productDiscounts;
}