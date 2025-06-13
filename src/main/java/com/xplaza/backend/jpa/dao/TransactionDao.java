/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "transactions")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long transactionId;

  Double transactionAmount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  CurrencyDao currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_order_id")
  OrderDao order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_payment_type_id")
  PaymentTypeDao paymentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_status_id")
  StatusCatalogueDao status;
}