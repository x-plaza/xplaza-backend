package com.backend.xplaza.repository;

import com.backend.xplaza.model.AdminUserShopLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AdminUserShopLinkRepository extends JpaRepository<AdminUserShopLink, Long> {
    @Modifying
    @Transactional
    @Query(value = "insert into admin_user_shop_link values(?1, ?2)", nativeQuery = true)
    void insert(long admin_user_id, long shop_id);

    @Modifying
    @Transactional
    @Query(value = "delete from admin_user_shop_link where admin_user_id = ?1", nativeQuery = true)
    void deleteByAdminUserID(long id);
}
