/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "admin_user_shop_link")
@IdClass(AdminUserShopLinkIdDao.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserShopLinkDao {
  @Id
  @ManyToOne
  @JoinColumn(name = "admin_user_id")
  AdminUserDao adminUser;

  @Id
  @ManyToOne
  @JoinColumn(name = "shop_id")
  ShopDao shop;
}