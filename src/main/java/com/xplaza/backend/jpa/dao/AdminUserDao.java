/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "admin_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long adminUserId;

  private String userName;
  private String password;
  private String salt;
  private String fullName;
  private String confirmationCode;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_role_id")
  private RoleDao role;

  @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<AdminUserShopLinkDao> shopLinks = new ArrayList<>();
}
