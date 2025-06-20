/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.Immutable;

@Table(name = "top_customer")
@Immutable
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class TopCustomerDao {
  @Id
  Long id;

  Long customerId;

  String customerName;

  Double totalOrderAmount;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "shop_id")
  ShopDao shop;
}