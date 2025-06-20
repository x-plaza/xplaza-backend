/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.ModuleDao;

public interface ModuleRepository extends JpaRepository<ModuleDao, Long> {
  @Query(value = "select module_name from modules where module_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from modules where module_id = ?1", nativeQuery = true)
  ModuleDao findModuleById(Long id);
}
