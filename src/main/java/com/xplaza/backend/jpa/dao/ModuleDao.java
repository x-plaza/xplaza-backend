/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "modules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long moduleId;

  private String moduleName;
  private String moduleDescription;

  @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
  private List<RoleDao> roles = new ArrayList<>();
}