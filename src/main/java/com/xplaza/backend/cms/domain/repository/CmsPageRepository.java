/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.cms.domain.entity.CmsPage;

@Repository
public interface CmsPageRepository extends JpaRepository<CmsPage, Long> {
  Optional<CmsPage> findBySlugAndPublishedTrue(String slug);

  List<CmsPage> findByPublishedTrue();
}
