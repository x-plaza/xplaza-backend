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
@Table(name = "states")
public class State {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "state_id")
  private Long id;

  @Column(name = "state_name")
  private String name;

  @Column(name = "fk_country_id")
  private Long country_id;
}
