/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long roleId;

  private String roleName;
  private String roleDescription;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_module_id")
  private Module module;

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
  private List<AdminUserDao> adminUsers = new ArrayList<>();
}