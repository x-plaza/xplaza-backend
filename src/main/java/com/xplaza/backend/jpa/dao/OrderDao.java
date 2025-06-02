/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_payment_type_id")
  private PaymentTypeDao paymentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_status_id")
  private StatusCatalogueDao status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  private CurrencyDao currency;

  private Double totalAmount;
  private Date createdAt;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<OrderItemDao> orderItems = new ArrayList<>();
}