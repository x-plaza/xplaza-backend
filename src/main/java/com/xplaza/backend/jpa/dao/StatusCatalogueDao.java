/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "status_catalogues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusCatalogueDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long statusId;

  private String statusName;
  private String statusDescription;
}