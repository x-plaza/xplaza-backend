/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cityId;

  private String cityName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_state_id")
  private StateDao state;

  @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
  private List<LocationDao> locations = new ArrayList<>();
}