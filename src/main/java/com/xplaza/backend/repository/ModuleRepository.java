/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.Module;

public interface ModuleRepository extends JpaRepository<Module, Long> {
  @Query(value = "select module_name from modules where module_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from modules where module_id = ?1", nativeQuery = true)
  Module findModuleById(Long id);
}
