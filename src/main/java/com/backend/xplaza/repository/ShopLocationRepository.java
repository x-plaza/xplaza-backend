package com.backend.xplaza.repository;

import com.backend.xplaza.model.ShopLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopLocationRepository extends JpaRepository<ShopLocation, Long> {
    @Query(value = "select shop_id,shop_name,shop_description,shop_address,fk_location_id,location_name from shops s left join locations l on s.fk_location_id = l.location_id", nativeQuery = true)
    List<ShopLocation> findAllItem();

    @Query(value = "select shop_id,shop_name,shop_description,shop_address,fk_location_id,location_name from shops s left join locations l on s.fk_location_id = l.location_id where s.shop_id = ?1", nativeQuery = true)
    ShopLocation findItemById(Long id);
}
