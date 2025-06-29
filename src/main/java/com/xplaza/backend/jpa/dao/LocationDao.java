/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "locations")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long locationId;

  String locationName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_city_id")
  CityDao city;
}