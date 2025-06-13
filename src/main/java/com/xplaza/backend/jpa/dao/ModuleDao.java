/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "modules")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long moduleId;

  String moduleName;

  String moduleDescription;

  @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
  List<RoleDao> roles;
}