/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.AdminUserShopLinkDao;
import com.xplaza.backend.model.AdminUserShopLinkId;

public interface AdminUserShopLinkRepository extends JpaRepository<AdminUserShopLinkDao, AdminUserShopLinkId> {
  @Modifying
  @Transactional
  @Query(value = "insert into admin_user_shop_link values(?1, ?2)", nativeQuery = true)
  void insert(Long admin_user_id, Long shop_id);

  @Modifying
  @Transactional
  @Query(value = "delete from admin_user_shop_link where admin_user_id = ?1", nativeQuery = true)
  void deleteByAdminUserID(Long id);
}
