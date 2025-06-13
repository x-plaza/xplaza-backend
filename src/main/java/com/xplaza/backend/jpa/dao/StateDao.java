/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "states")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StateDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long stateId;

  String stateName;

  @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
  List<CityDao> cities;
}