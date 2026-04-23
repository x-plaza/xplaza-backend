/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.catalog.domain.entity.ProductTranslation;

@Repository
public interface ProductTranslationRepository extends JpaRepository<ProductTranslation, Long> {
  Optional<ProductTranslation> findByProductIdAndLocale(Long productId, String locale);

  List<ProductTranslation> findByProductId(Long productId);
}
