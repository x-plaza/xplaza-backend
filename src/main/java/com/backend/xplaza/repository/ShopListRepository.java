package com.backend.xplaza.repository;

import com.backend.xplaza.model.ShopList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.Valid;
import java.util.List;

public interface ShopListRepository extends JpaRepository<ShopList, Long> {
    @Query(value = "select s.*, l.location_name from shops s " +
            "left join locations l on s.fk_location_id = l.location_id", nativeQuery = true)
    List<ShopList> findAllShopList();

    @Query(value = "select s.*, l.location_name from shops s " +
            "left join locations l on s.fk_location_id = l.location_id where s.shop_id = ?1", nativeQuery = true)
    ShopList findShopListById(Long id);

    @Query(value = "select s.*, l.location_name from shops s " +
            "left join locations l on s.fk_location_id = l.location_id " +
            "left join admin_user_shop_link ausl on ausl.shop_id = s.shop_id " +
            "where ausl.admin_user_id = ?1", nativeQuery = true)
    List<ShopList> findAllShopListByUserID(@Valid Long user_id);
}
