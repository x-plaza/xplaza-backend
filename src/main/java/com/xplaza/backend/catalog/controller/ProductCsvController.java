/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.xplaza.backend.catalog.service.ProductCsvService;
import com.xplaza.backend.common.util.ApiResponse;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ProductCsvController {

  private final ProductCsvService csvService;

  @GetMapping(value = "/export", produces = "text/csv")
  public ResponseEntity<byte[]> export() {
    var bytes = csvService.exportAll();
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.csv")
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(bytes);
  }

  @PostMapping(value = "/import", consumes = "multipart/form-data")
  public ResponseEntity<ApiResponse<ProductCsvService.ImportSummary>> importCsv(
      @RequestParam("file") MultipartFile file) throws Exception {
    return ResponseEntity.ok(ApiResponse.ok(csvService.importCsv(file.getInputStream())));
  }
}
