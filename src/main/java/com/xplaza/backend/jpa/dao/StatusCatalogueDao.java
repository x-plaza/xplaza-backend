/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "status_catalogues")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusCatalogueDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long statusId;

  private String statusName;

  private String statusDesc;
}