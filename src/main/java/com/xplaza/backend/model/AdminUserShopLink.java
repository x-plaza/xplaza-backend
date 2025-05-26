/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_user_shop_link")
@IdClass(AdminUserShopLinkId.class)
public class AdminUserShopLink {
  @Id
  @Column(name = "admin_user_id")
  private Long id;

  @Id
  @Column(name = "shop_id")
  private Long shop_id;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "admin_user_id", insertable = false, updatable = false)
  private AdminUser adminUser;
}
