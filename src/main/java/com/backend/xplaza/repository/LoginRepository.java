package com.backend.xplaza.repository;

import com.backend.xplaza.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginRepository extends JpaRepository<Login, Long> {
    @Query(value = "select l.* from login l where l.user_name = ?1", nativeQuery = true)
    Login findUserByUsername(String username);
}
