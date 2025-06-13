/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "roles")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long roleId;

  String roleName;

  String roleDescription;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_module_id")
  ModuleDao module;
}