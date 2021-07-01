package com.backend.xplaza.repository;

import com.backend.xplaza.model.ShopList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopListRepository extends JpaRepository<ShopList, Long> {
    @Query(value = "select shop_id, shop_name, shop_owner, shop_address, fk_location_id, location_name from shops s " +
            "left join locations l on s.fk_location_id = l.location_id", nativeQuery = true)
    List<ShopList> findAllShopList();

    @Query(value = "select shop_id, shop_name, shop_owner, shop_address, fk_location_id, location_name from shops s " +
            "left join locations l on s.fk_location_id = l.location_id where s.shop_id = ?1", nativeQuery = true)
    ShopList findShopListById(Long id);
}
