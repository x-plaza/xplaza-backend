/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.AdminLogin;

public interface LoginRepository extends JpaRepository<AdminLogin, Long> {
  @Query(value = "select l.* from login l where l.user_name = ?1", nativeQuery = true)
  AdminLogin findUserByUsername(String username);
}
