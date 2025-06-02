/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
  /*
   * @Query(value =
   * "select product_image_name from product_images where product_image_id = ?1",
   * nativeQuery = true) String getName(Long id);
   */

  @Query(value = "select * from product_images where fk_product_id = ?1", nativeQuery = true)
  List<ProductImage> findImageByProductId(Long id);

  @Modifying
  @Transactional
  @Query(value = "delete from product_images where fk_product_id = ?1", nativeQuery = true)
  void deleteImagesByProductId(Long id);
}