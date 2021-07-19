package com.backend.xplaza.repository;

import com.backend.xplaza.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    @Query(value = "select user_name from admin_users where admin_user_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from admin_users where user_name = ?1", nativeQuery = true)
    AdminUser findUserByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "update admin_users set fk_role_id=?1, full_name=?2 where admin_user_id=?3", nativeQuery = true)
    void update(Long role_id, String full_name, Long admin_user_id);

    @Modifying
    @Transactional
    @Query(value = "update admin_users set password=?1, salt=?2 where user_name=?3", nativeQuery = true)
    void changePassword(String new_password, String salt, String user_name);

    @Modifying
    @Transactional
    @Query(value = "update admin_users set is_confirmed=?2 where admin_user_id=?1", nativeQuery = true)
    void updateConfirmStatus(Long user_id, Boolean is_confirmed);
}
