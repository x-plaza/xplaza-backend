/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.xplaza.backend.catalog.domain.entity.Product;
import com.xplaza.backend.catalog.dto.request.ProductRequest;
import com.xplaza.backend.catalog.dto.response.ProductResponse;
import com.xplaza.backend.catalog.mapper.ProductMapper;
import com.xplaza.backend.catalog.service.ProductService;
import com.xplaza.backend.catalog.service.ProductTranslationService;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.common.util.ApiResponse.PageMeta;

/**
 * Product Controller - Clean REST API design.
 * 
 * Key improvements over V1: - Uses query parameters for filtering instead of
 * separate endpoints - Proper pagination support - Let Spring handle JSON
 * serialization (no manual ObjectMapper) - Consistent response structure via
 * ApiResponse - Proper HTTP status codes - Validation annotations
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Product Management", description = "APIs for managing products with pagination and filtering")
public class ProductController {

  private final ProductService productService;
  private final ProductMapper productMapper;
  private final ProductTranslationService translationService;

  @GetMapping
  @Operation(summary = "List products", description = "Get paginated list of products with optional filters for shop, category, brand, and search")
  public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(
      @RequestParam(required = false) Long shopId,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) Long brandId,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice,
      @RequestParam(required = false) String gender,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) int size,
      @RequestParam(defaultValue = "productId") String sort,
      @RequestParam(defaultValue = "ASC") Sort.Direction direction,
      @RequestParam(required = false) String locale) {

    // Cap page size to prevent abuse
    size = Math.min(size, 100);
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

    Page<Product> productPage = productService.findProductsFiltered(
        shopId, categoryId, brandId, search, minPrice, maxPrice, gender, pageable);

    List<ProductResponse> dtos = productPage.getContent().stream()
        .map(productMapper::toResponse)
        .toList();

    if (locale != null && !locale.isBlank()) {
      translationService.applyLocale(dtos, locale);
    }

    PageMeta pageMeta = PageMeta.from(productPage);

    return ResponseEntity.ok(ApiResponse.ok(dtos, pageMeta));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
  public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
      @PathVariable @Positive Long id,
      @RequestParam(required = false) String locale) {

    Product product = productService.listProduct(id);
    ProductResponse dto = productMapper.toResponse(product);
    if (locale != null && !locale.isBlank()) {
      translationService.applyLocale(dto, locale);
    }

    return ResponseEntity.ok(ApiResponse.ok(dto));
  }

  @PostMapping
  @Operation(summary = "Create product", description = "Create a new product")
  public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
      @RequestBody @Valid ProductRequest request) {

    Product entity = productMapper.toEntity(request);
    Product saved = productService.addProduct(entity);
    ProductResponse dto = productMapper.toResponse(saved);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.created(dto));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update product", description = "Update an existing product by ID")
  public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
      @PathVariable @Positive Long id,
      @RequestBody @Valid ProductRequest request) {

    // Set the ID from path to ensure we update the right resource
    request.setProductId(id);

    Product entity = productMapper.toEntity(request);
    Product updated = productService.updateProduct(entity);
    ProductResponse dto = productMapper.toResponse(updated);

    return ResponseEntity.ok(ApiResponse.ok(dto));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete product", description = "Delete a product by ID")
  public ResponseEntity<ApiResponse<Void>> deleteProduct(
      @PathVariable @Positive Long id) {

    String productName = productService.getProductNameByID(id);
    productService.deleteProduct(id);

    return ResponseEntity.ok(ApiResponse.ok(productName + " has been deleted"));
  }

  @PatchMapping("/{id}/inventory")
  @Operation(summary = "Update product inventory", description = "Update product inventory quantity")
  public ResponseEntity<ApiResponse<Void>> updateInventory(
      @PathVariable @Positive Long id,
      @RequestParam @Min(0) int quantity) {

    productService.updateProductInventory(id, quantity);

    return ResponseEntity.ok(ApiResponse.ok("Inventory updated"));
  }

  @PostMapping(value = "/{id}/images", consumes = "multipart/form-data")
  @Operation(summary = "Upload product images", description = "Upload images for a product or variant (max 10 total)")
  public ResponseEntity<ApiResponse<List<String>>> uploadProductImages(
      @PathVariable Long id,
      @RequestParam(required = false) java.util.UUID variantId,
      @RequestParam("files") List<MultipartFile> files) {
    List<String> imageUrls = productService.uploadProductImages(id, variantId, files);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(imageUrls));
  }
}
