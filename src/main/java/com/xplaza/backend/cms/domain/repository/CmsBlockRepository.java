/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.cms.domain.entity.CmsBlock;

@Repository
public interface CmsBlockRepository extends JpaRepository<CmsBlock, Long> {
  Optional<CmsBlock> findByCodeAndLocaleAndActiveTrue(String code, String locale);

  Optional<CmsBlock> findByCode(String code);

  List<CmsBlock> findByActiveTrue();
}
