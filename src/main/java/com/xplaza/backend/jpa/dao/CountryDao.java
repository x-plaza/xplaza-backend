/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long countryId;

  private String iso;
  private String countryName;
  private String nicename;
  private String iso3;
  private Short numcode;
  private Integer phonecode;

  @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
  private List<StateDao> states = new ArrayList<>();
}