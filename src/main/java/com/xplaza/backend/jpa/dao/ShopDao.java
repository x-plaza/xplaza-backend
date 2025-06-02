/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "shops")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long shopId;

  private String shopName;
  private String shopDescription;
  private String shopAddress;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_location_id")
  private LocationDao location;

  private String shopOwner;

  @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
  private List<ProductDao> products = new ArrayList<>();
}