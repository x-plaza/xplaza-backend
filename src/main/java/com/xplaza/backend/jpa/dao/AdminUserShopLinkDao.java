/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "admin_user_shop_link")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserShopLinkDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_user_id")
  private AdminUserDao adminUser;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id")
  private ShopDao shop;
}