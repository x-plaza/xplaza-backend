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
@Table(name = "login")
public class AdminLogin {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "admin_user_id")
  private Long id;

  private Boolean authentication;

  @Embedded
  private AuthData authData;

  @OneToMany(mappedBy = "adminLogin")
  private List<LoginUserShopList> shopList;

  @OneToMany(mappedBy = "adminLogin")
  private List<Permission> permissions;
}
