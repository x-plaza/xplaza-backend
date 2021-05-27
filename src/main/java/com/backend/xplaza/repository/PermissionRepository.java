package com.backend.xplaza.repository;

import com.backend.xplaza.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository <Permission, Long>{
}
