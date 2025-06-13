/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "menu_module_link")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuModuleLinkDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id")
  MenuDao menu;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "module_id")
  ModuleDao module;
}