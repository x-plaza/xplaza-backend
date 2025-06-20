/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.LoginDao;

public interface LoginRepository extends JpaRepository<LoginDao, Long> {
  @Query(value = "select l.* from logins l where l.user_name = ?1", nativeQuery = true)
  LoginDao findUserByUsername(String username);
}
