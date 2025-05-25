/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "modules")
public class Module {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "module_id")
  private Long id;

  @Column(name = "module_name")
  private String name;

  @Column(name = "module_description")
  private String description;
}
