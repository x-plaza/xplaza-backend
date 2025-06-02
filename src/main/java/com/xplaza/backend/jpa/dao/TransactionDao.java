/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long transactionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  private Currency currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_payment_type_id")
  private PaymentTypeDao paymentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_order_id")
  private OrderDao order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_status_id")
  private StatusCatalogueDao status;

  private Double amount;
  private Date transactionDate;
}