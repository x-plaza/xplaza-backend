/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "shops")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long shopId;

  String shopName;

  String shopDescription;

  String shopAddress;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_location_id")
  LocationDao location;

  String shopOwner;

  @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
  List<ProductDao> products;
}