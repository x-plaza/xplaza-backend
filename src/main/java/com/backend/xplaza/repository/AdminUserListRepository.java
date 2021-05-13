package com.backend.xplaza.repository;

import com.backend.xplaza.model.AdminUserList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminUserListRepository extends JpaRepository<AdminUserList, Long> {
    @Query(value = "select a.*, r.role_name, s.shop_name from admin_users a " +
            "left join roles r on a.fk_role_id = r.role_id" +
            "left join shop  s on a.fk_shop_id = s.shop_id" +
            "where admin_user_id = ?1", nativeQuery = true)
    AdminUserList findItemById(Long id);

    @Query(value = "select a.*, r.role_name, s.shop_name from admin_users a " +
            "left join roles r on a.fk_role_id = r.role_id" +
            "left join shops s on a.fk_shop_id = s.shop_id", nativeQuery = true)
    List<AdminUserList> findAllItem();
}
