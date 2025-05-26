/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "day_names")
public class Day {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "day_id")
  private Long id;

  @Column(name = "day_name")
  private String name;
}
