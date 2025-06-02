/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "currencies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long currencyId;

  private String currencyName;
  private String currencySign;

  @OneToMany(mappedBy = "currency", fetch = FetchType.LAZY)
  private List<ProductDao> products = new ArrayList<>();
}