/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.recommendation.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.recommendation.domain.entity.ProductCoPurchase;

@Repository
public interface ProductCoPurchaseRepository extends JpaRepository<ProductCoPurchase, ProductCoPurchase.PK> {

  @Query("SELECT cp FROM ProductCoPurchase cp WHERE cp.productId = :productId ORDER BY cp.coPurchaseCount DESC")
  List<ProductCoPurchase> findTopByProductId(@Param("productId") Long productId, Pageable page);
}
