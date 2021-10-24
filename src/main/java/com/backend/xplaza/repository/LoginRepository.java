package com.backend.xplaza.repository;

import com.backend.xplaza.model.AdminLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginRepository extends JpaRepository<AdminLogin, Long> {
    @Query(value = "select l.* from login l where l.user_name = ?1", nativeQuery = true)
    AdminLogin findUserByUsername(String username);
}
