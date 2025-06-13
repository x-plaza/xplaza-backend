/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "currencies")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long currencyId;

  String currencyName;

  String currencySign;
}