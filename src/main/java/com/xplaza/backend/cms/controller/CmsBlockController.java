/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.controller;

import java.util.List;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.cms.domain.entity.CmsBlock;
import com.xplaza.backend.cms.service.CmsBlockService;

/**
 * Public read endpoints + admin write endpoints for storefront CMS blocks.
 */
@RestController
@RequestMapping("/api/v1/cms/blocks")
@RequiredArgsConstructor
@Tag(name = "CMS Blocks", description = "Static content blocks served to the storefront")
public class CmsBlockController {

  private final CmsBlockService service;

  @Operation(summary = "Get an active CMS block by code (storefront-public)")
  @GetMapping("/{code}")
  public ResponseEntity<CmsBlock> get(
      @PathVariable String code,
      @RequestParam(required = false, defaultValue = "en") String locale) {
    return service.getActive(code, locale)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "List all active CMS blocks")
  @GetMapping
  public ResponseEntity<List<CmsBlock>> list() {
    return ResponseEntity.ok(service.listActive());
  }

  @Operation(summary = "Create or update a CMS block (admin)")
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CmsBlock> save(@RequestBody @Valid CmsBlock block) {
    return ResponseEntity.ok(service.save(block));
  }

  @Operation(summary = "Delete a CMS block (admin)")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
