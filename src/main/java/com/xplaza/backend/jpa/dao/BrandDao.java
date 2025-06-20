/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "brands")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long brandId;

  String brandName;

  String brandDescription;
}