package com.backend.xplaza.repository;

import com.backend.xplaza.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginRepository extends JpaRepository<Login, Long> {
    @Query(value = "select a.admin_user_id, a.user_name, r.role_id, r.role_name, s.shop_id, s.shop_name, false as authentication from admin_users a " +
            "left join roles r on a.fk_role_id = r.role_id " +
            "left join shops s on a.fk_shop_id = s.shop_id " +
            "where a.user_name = ?1", nativeQuery = true)
    Login findUserByUsername(String username);
}
