package com.backend.xplaza.repository;

import com.backend.xplaza.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    @Query(value = "select user_name from admin_users where admin_user_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from admin_users where admin_user_id = ?1", nativeQuery = true)
    AdminUser findItemById(Long id);
}
