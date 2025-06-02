/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  @Query(value = "select role_name from roles where role_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from roles where role_id = ?1", nativeQuery = true)
  Role findRoleById(Long id);

  @Query(value = "select r.role_name from roles r left join admin_users au on au.fk_role_id = r.role_id where au.admin_user_id = ?1", nativeQuery = true)
  String getRoleNameByUserID(Long id);
}