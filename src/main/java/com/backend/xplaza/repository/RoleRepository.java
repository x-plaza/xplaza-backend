package com.backend.xplaza.repository;

import com.backend.xplaza.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "select role_name from roles where role_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from roles where role_id = ?1", nativeQuery = true)
    Role findItemById(Long id);
}