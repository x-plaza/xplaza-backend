/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "revenue")
public class Dashboard {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "shop_id")
  private Long shop_id;

  @Embedded
  private Revenue revenue;

  @OneToMany(mappedBy = "dashboard")
  private List<TopCustomer> topCustomers;

  @OneToMany(mappedBy = "dashboard")
  private List<TopProduct> topProducts;

  @OneToMany(mappedBy = "dashboard")
  private List<ProductToStock> productToStocks;
}
