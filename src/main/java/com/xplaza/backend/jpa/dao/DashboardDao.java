/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.Immutable;

@Table(name = "dashboard")
@Immutable
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDao {
  @Id
  private Long dashboardId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id")
  ShopDao shop;
}
