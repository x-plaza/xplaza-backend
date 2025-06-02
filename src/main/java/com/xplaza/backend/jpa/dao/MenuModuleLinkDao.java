/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "menu_module_link")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuModuleLinkDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id")
  private MenuDao menu;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "module_id")
  private ModuleDao module;
}