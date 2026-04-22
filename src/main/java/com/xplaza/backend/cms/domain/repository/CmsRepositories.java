/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.cms.domain.entity.CmsBanner;
import com.xplaza.backend.cms.domain.entity.CmsFaq;
import com.xplaza.backend.cms.domain.entity.CmsPage;

public final class CmsRepositories {
  private CmsRepositories() {}

  @Repository
  public interface CmsPageRepository extends JpaRepository<CmsPage, Long> {
    Optional<CmsPage> findBySlugAndPublishedTrue(String slug);
    List<CmsPage> findByPublishedTrue();
  }

  @Repository
  public interface CmsBannerRepository extends JpaRepository<CmsBanner, Long> {
    List<CmsBanner> findByActiveTrue();
    Optional<CmsBanner> findByCode(String code);
  }

  @Repository
  public interface CmsFaqRepository extends JpaRepository<CmsFaq, Long> {
    List<CmsFaq> findByActiveTrueOrderByDisplayOrderAsc();
  }
}
