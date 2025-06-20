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

@Table(name = "logins")
@Immutable
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDao {
  @Id
  @Column(name = "admin_user_id")
  private Long adminUserId;

  @OneToOne
  @JoinColumn(name = "admin_user_id", insertable = false, updatable = false)
  private AdminUserDao adminUser;

  String userName;

  String fullName;

  String roleId;

  String roleName;

  boolean authentication;
}
