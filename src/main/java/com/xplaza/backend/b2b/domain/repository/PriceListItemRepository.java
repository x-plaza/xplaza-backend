/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.b2b.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.b2b.domain.entity.PriceListItem;

@Repository
public interface PriceListItemRepository extends JpaRepository<PriceListItem, Long> {

  List<PriceListItem> findByPriceListIdAndProductIdOrderByMinQuantityDesc(Long priceListId, Long productId);

  List<PriceListItem> findByPriceListId(Long priceListId);
}
