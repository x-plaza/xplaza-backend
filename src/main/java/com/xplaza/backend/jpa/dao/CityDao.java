/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "cities")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long cityId;

  String cityName;

  @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
  List<LocationDao> locations;
}