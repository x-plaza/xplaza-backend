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
  @Column(name = "admin_user_id")
  private Long adminUserId;

  @Id
  @Column(name = "shop_id")
  private Long shopId;

  @ManyToOne
  @JoinColumn(name = "admin_user_id", insertable = false, updatable = false)
  AdminUserDao adminUser;

  @ManyToOne
  @JoinColumn(name = "shop_id", insertable = false, updatable = false)
  ShopDao shop;

  // Add transient getters for @IdClass compatibility
  @Transient
  public Long getAdminUserId() {
    return adminUser != null ? adminUser.getAdminUserId() : null;
  }

  @Transient
  public Long getShopId() {
    return shop != null ? shop.getShopId() : null;
  }
}