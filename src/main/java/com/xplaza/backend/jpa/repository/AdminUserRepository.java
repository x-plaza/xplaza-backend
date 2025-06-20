/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.AdminUserDao;

public interface AdminUserRepository extends JpaRepository<AdminUserDao, Long> {
  @Query(value = "select user_name from admin_users where admin_user_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from admin_users where user_name = ?1", nativeQuery = true)
  AdminUserDao findUserByUsername(String username);

  @Modifying
  @Transactional
  @Query(value = "update admin_users set fk_role_id=?1, full_name=?2 where admin_user_id=?3", nativeQuery = true)
  void update(Long roleId, String fullName, Long adminUserId);

  @Modifying
  @Transactional
  @Query(value = "update admin_users set password=?1, salt=?2 where user_name=?3", nativeQuery = true)
  void changePassword(String newPassword, String salt, String userName);

  @Query(value = "select user_name from admin_users au " +
      "left join admin_user_shop_link ausl on au.admin_user_id = ausl.admin_user_id " +
      "where ausl.shop_id = ?1", nativeQuery = true)
  List<String> getEmailListByShopId(Long shopId);
}
