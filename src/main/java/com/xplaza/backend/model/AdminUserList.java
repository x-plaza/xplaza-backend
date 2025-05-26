/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_users")
public class AdminUserList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "admin_user_id")
  private Long id;

  @Column(name = "full_name")
  private String full_name;

  @Column(name = "user_name")
  private String user_name;

  @Column(name = "password")
  private String password;

  @Column(name = "salt")
  private String salt;

  @Column(name = "fk_role_id")
  private Long role_id;

  @Column(name = "role_name")
  private String role_name;

  @OneToMany(mappedBy = "adminUserList")
  private List<AdminUserShopList> shopList;
}
