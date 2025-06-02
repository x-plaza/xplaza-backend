/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long locationId;

  private String locationName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_city_id")
  private CityDao city;

  @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
  private List<ShopDao> shops = new ArrayList<>();
}