package com.backend.xplaza.repository;

import com.backend.xplaza.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Query(value = "select product_image_name from product_images where product_image_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from product_images where product_image_id = ?1", nativeQuery = true)
    ProductImage findItemById(long id);
}
