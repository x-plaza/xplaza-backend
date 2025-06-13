/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Immutable;

@Table(name = "permissions")
@Immutable
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDao {
  @Id
  Long id;

  @OneToOne
  @JoinColumn(name = "admin_user_id")
  AdminUserDao adminUser;

  String menuName;

  boolean allOfThem;

  boolean view;

  boolean add;

  boolean update;

  boolean delete;
}