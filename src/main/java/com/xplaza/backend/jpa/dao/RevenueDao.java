/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.Immutable;

@Table(name = "revenue")
@Immutable
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class RevenueDao {
  @Id
  @OneToOne
  @JoinColumn(name = "shop_id")
  ShopDao shop;

  Double totalExpense;

  Double totalIncome;

  Double totalRevenue;
}