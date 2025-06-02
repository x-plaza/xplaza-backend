/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "states")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long stateId;

  private String stateName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_country_id")
  private CountryDao country;

  @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
  private List<CityDao> cities = new ArrayList<>();
}