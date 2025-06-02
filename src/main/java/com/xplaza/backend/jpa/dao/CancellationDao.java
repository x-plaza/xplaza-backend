/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "cancellations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cancellationId;

  private String cancellationReason;
  private Date cancellationDate;
}