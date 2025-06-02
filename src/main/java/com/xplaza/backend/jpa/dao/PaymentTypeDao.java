/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "payment_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTypeDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentTypeId;

  private String paymentTypeName;
  private String paymentTypeDescription;

  @OneToMany(mappedBy = "paymentType", fetch = FetchType.LAZY)
  private List<OrderDao> orders = new ArrayList<>();
}