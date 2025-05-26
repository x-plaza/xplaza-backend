/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.AdminUserList;

public interface AdminUserListRepository extends JpaRepository<AdminUserList, Long> {
  @Query(value = "select a.*, r.role_name " +
      "from admin_users a " +
      "left join roles r on a.fk_role_id = r.role_id " +
      "where a.admin_user_id = ?1", nativeQuery = true)
  AdminUserList findUserById(Long id);

  @Query(value = "select a.*, r.role_name " +
      "from admin_users a " +
      "left join roles r on a.fk_role_id = r.role_id", nativeQuery = true)
  List<AdminUserList> findAllUsers();

  @Query(value = "select a.*, r.role_name " +
      "from admin_users a " +
      "left join roles r on a.fk_role_id = r.role_id where r.role_name = ?1", nativeQuery = true)
  List<AdminUserList> findAllAdminUsersByRoleName(String role_name);
}
