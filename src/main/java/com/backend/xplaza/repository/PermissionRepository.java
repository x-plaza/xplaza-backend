/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.xplaza.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
