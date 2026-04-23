/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.cms.domain.entity.CmsFaq;

@Repository
public interface CmsFaqRepository extends JpaRepository<CmsFaq, Long> {
  List<CmsFaq> findByActiveTrueOrderByDisplayOrderAsc();
}
