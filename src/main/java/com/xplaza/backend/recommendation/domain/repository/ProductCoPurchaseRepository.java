/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.recommendation.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.recommendation.domain.entity.ProductCoPurchase;

@Repository
public interface ProductCoPurchaseRepository extends JpaRepository<ProductCoPurchase, ProductCoPurchase.PK> {

  @Query("SELECT cp FROM ProductCoPurchase cp WHERE cp.productId = :productId ORDER BY cp.coPurchaseCount DESC")
  List<ProductCoPurchase> findTopByProductId(@Param("productId") Long productId, Pageable page);

  @Modifying
  @Query(value = "INSERT INTO product_co_purchases(product_id, co_product_id, co_purchase_count) "
      + "VALUES (:productId, :coProductId, 1) "
      + "ON CONFLICT (product_id, co_product_id) "
      + "DO UPDATE SET co_purchase_count = product_co_purchases.co_purchase_count + 1", nativeQuery = true)
  void incrementPair(@Param("productId") Long productId, @Param("coProductId") Long coProductId);
}
