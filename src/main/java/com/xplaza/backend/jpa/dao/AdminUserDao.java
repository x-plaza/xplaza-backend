/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "admin_users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long adminUserId;

  String userName;

  String password;

  String salt;

  String fullName;

  String confirmationCode;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_role_id")
  RoleDao role;

  @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<AdminUserShopLinkDao> shopLinks;
}
