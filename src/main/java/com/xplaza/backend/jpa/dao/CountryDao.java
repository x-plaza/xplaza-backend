/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "countries")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long countryId;

  String iso;

  String countryName;

  String nicename;

  String iso3;

  Short numcode;

  Integer phonecode;

  @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
  List<StateDao> states;
}