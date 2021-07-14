package com.backend.xplaza.repository;

import com.backend.xplaza.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    @Query(value = "select user_name from admin_users where admin_user_id = ?1", nativeQuery = true)
    String getName(long id);

    @Query(value = "select * from admin_users where user_name = ?1", nativeQuery = true)
    AdminUser findUserByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "update admin_users set password=?1, salt=?2, fk_role_id=?3, full_name=?4 where admin_user_id=?5", nativeQuery = true)
    void update(String password, String salt, long role_id, String full_name, long admin_user_id);
}
