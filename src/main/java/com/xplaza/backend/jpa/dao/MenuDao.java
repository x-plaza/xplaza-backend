/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "menus")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long menuId;

  String menuName;

  String menuDescription;

  @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
  List<MenuModuleLinkDao> menuModuleLinks;
}