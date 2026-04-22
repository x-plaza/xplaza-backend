/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.cms.domain.entity.CmsBanner;
import com.xplaza.backend.cms.domain.entity.CmsFaq;
import com.xplaza.backend.cms.domain.entity.CmsPage;
import com.xplaza.backend.cms.domain.repository.CmsRepositories.*;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/cms")
@RequiredArgsConstructor
public class CmsController {

  private final CmsPageRepository pages;
  private final CmsBannerRepository banners;
  private final CmsFaqRepository faqs;

  @GetMapping("/pages")
  public ResponseEntity<ApiResponse<List<CmsPage>>> listPages() {
    return ResponseEntity.ok(ApiResponse.ok(pages.findByPublishedTrue()));
  }

  @GetMapping("/pages/{slug}")
  public ResponseEntity<ApiResponse<CmsPage>> getPage(@PathVariable String slug) {
    var page = pages.findBySlugAndPublishedTrue(slug)
        .orElseThrow(() -> new ResourceNotFoundException("page not found"));
    return ResponseEntity.ok(ApiResponse.ok(page));
  }

  @PostMapping("/pages")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<CmsPage>> createPage(@RequestBody CmsPage p) {
    return ResponseEntity.ok(ApiResponse.ok(pages.save(p)));
  }

  @PutMapping("/pages/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<CmsPage>> updatePage(@PathVariable Long id, @RequestBody CmsPage p) {
    p.setPageId(id);
    return ResponseEntity.ok(ApiResponse.ok(pages.save(p)));
  }

  @DeleteMapping("/pages/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<Void>> deletePage(@PathVariable Long id) {
    pages.deleteById(id);
    return ResponseEntity.ok(ApiResponse.ok("deleted"));
  }

  @GetMapping("/banners")
  public ResponseEntity<ApiResponse<List<CmsBanner>>> listBanners() {
    return ResponseEntity.ok(ApiResponse.ok(banners.findByActiveTrue()));
  }

  @PostMapping("/banners")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<CmsBanner>> createBanner(@RequestBody CmsBanner b) {
    return ResponseEntity.ok(ApiResponse.ok(banners.save(b)));
  }

  @GetMapping("/faqs")
  public ResponseEntity<ApiResponse<List<CmsFaq>>> listFaqs() {
    return ResponseEntity.ok(ApiResponse.ok(faqs.findByActiveTrueOrderByDisplayOrderAsc()));
  }

  @PostMapping("/faqs")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<CmsFaq>> createFaq(@RequestBody CmsFaq f) {
    return ResponseEntity.ok(ApiResponse.ok(faqs.save(f)));
  }
}
