package com.backend.xplaza.repository;

import com.backend.xplaza.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query(value = "select brand_name from brands where brand_id = ?1", nativeQuery = true)
    String getName(long id);

    @Query(value = "select * from brands where brand_id = ?1", nativeQuery = true)
    Brand findBrandById(long id);
}
