/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "payment_types")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentTypeId;

  private String paymentTypeName;
  private String paymentTypeDescription;

  @OneToMany(mappedBy = "paymentType", fetch = FetchType.LAZY)
  private List<OrderDao> orders = new ArrayList<>();
}