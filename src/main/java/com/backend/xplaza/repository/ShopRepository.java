package com.backend.xplaza.repository;

import com.backend.xplaza.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long>  {
    @Query(value = "select shop_name from shops where shop_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from shops s left join location l on s.fk_location_id = l.location_id where s.shop_id = ?1", nativeQuery = true)
    Shop findItemById(Long id);

    @Query(value = "select * from shops s left join location l on s.fk_location_id = l.location_id", nativeQuery = true)
    List<Shop> findAllItem();
}
