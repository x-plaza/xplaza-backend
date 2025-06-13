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

import com.xplaza.backend.jpa.dao.ProductDao;

public interface ProductRepository extends JpaRepository<ProductDao, Long> {
  @Query(value = "select product_name from products where product_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from products where product_id = ?1", nativeQuery = true)
  ProductDao findProductById(Long id);

  @Modifying
  @Transactional
  @Query(value = "update products set quantity = ?2 where product_id = ?1", nativeQuery = true)
  void updateInventory(Long id, int quantity);

  @Query(value = "SELECT p.* FROM products p " +
      "JOIN shops s ON p.shop_id = s.shop_id " +
      "JOIN shop_users su ON s.shop_id = su.shop_id " +
      "WHERE su.user_id = ?1", nativeQuery = true)
  List<ProductDao> findByUserId(Long userId);

  @Query(value = "SELECT p.* FROM products p " +
      "WHERE p.shop_id = ?1", nativeQuery = true)
  List<ProductDao> findByShopId(Long shopId);

  @Query(value = "SELECT p.* FROM products p " +
      "WHERE p.category_id = ?1", nativeQuery = true)
  List<ProductDao> findByCategoryId(Long categoryId);

  @Query(value = "SELECT p.* FROM products p " +
      "WHERE p.brand_id = ?1", nativeQuery = true)
  List<ProductDao> findByBrandId(Long brandId);

  @Query(value = "SELECT p.* FROM products p " +
      "WHERE p.shop_id = ?1 AND p.category_id = ?2", nativeQuery = true)
  List<ProductDao> findByShopIdAndCategoryId(Long shopId, Long categoryId);

  @Query(value = "SELECT p.* FROM products p " +
      "WHERE p.shop_id = ?1 AND p.is_trending = true", nativeQuery = true)
  List<ProductDao> findTrendingByShopId(Long shopId);
}
