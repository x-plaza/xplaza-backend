/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_payment_type_id")
  PaymentTypeDao paymentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_status_id")
  StatusCatalogueDao status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  CurrencyDao currency;

  Double totalAmount;

  Date createdAt;

  Date updatedAt;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  List<OrderItemDao> orderItems;
}