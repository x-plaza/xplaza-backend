/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.recommendation.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.recommendation.domain.entity.RecentlyViewedProduct;

@Repository
public interface RecentlyViewedProductRepository
    extends JpaRepository<RecentlyViewedProduct, RecentlyViewedProduct.PK> {

  List<RecentlyViewedProduct> findByCustomerIdOrderByViewedAtDesc(Long customerId, Pageable page);
}
