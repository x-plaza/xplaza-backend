/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shops")
public class ShopList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "shop_id")
  private Long id;

  @Column(name = "shop_name")
  private String name;

  @Column(name = "shop_owner")
  private String owner;

  @Column(name = "shop_address")
  private String address;

  @Column(name = "shop_description")
  private String description;

  @Column(name = "fk_location_id")
  private Long location_id;

  @Column(name = "location_name")
  private String location_name;
}
