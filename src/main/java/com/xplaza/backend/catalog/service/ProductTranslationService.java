/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.catalog.domain.entity.CategoryTranslation;
import com.xplaza.backend.catalog.domain.entity.ProductTranslation;
import com.xplaza.backend.catalog.domain.repository.CategoryTranslationRepository;
import com.xplaza.backend.catalog.domain.repository.ProductTranslationRepository;
import com.xplaza.backend.catalog.dto.response.ProductResponse;

/**
 * Looks up per-locale translations and applies them to {@link ProductResponse}
 * payloads. Defaults gracefully back to the canonical English values when a
 * locale is missing so the UI always renders something. Translations live
 * behind a Caffeine cache keyed on (productId, locale) for O(1) lookups during
 * catalog browsing.
 */
@Service
@RequiredArgsConstructor
public class ProductTranslationService {

  private final ProductTranslationRepository productTranslationRepo;
  private final CategoryTranslationRepository categoryTranslationRepo;

  @Cacheable(value = "products", key = "'pt:' + #productId + ':' + #locale")
  @Transactional(readOnly = true)
  public Optional<ProductTranslation> getProductTranslation(Long productId, String locale) {
    if (productId == null || locale == null || locale.isBlank()) {
      return Optional.empty();
    }
    return productTranslationRepo.findByProductIdAndLocale(productId, locale);
  }

  @Cacheable(value = "products", key = "'ct:' + #categoryId + ':' + #locale")
  @Transactional(readOnly = true)
  public Optional<CategoryTranslation> getCategoryTranslation(Long categoryId, String locale) {
    if (categoryId == null || locale == null || locale.isBlank()) {
      return Optional.empty();
    }
    return categoryTranslationRepo.findByCategoryIdAndLocale(categoryId, locale);
  }

  /**
   * Mutate the response in-place with the requested locale's translation if one
   * exists. Returns the same instance for chaining.
   */
  public ProductResponse applyLocale(ProductResponse response, String locale) {
    if (response == null || response.getProductId() == null || locale == null || locale.isBlank()) {
      return response;
    }
    getProductTranslation(response.getProductId(), locale).ifPresent(t -> {
      if (t.getName() != null) {
        response.setProductName(t.getName());
      }
      if (t.getDescription() != null) {
        response.setProductDescription(t.getDescription());
      }
    });
    if (response.getCategoryId() != null) {
      getCategoryTranslation(response.getCategoryId(), locale)
          .ifPresent(ct -> {
            if (ct.getName() != null) {
              response.setCategoryName(ct.getName());
            }
          });
    }
    return response;
  }

  public List<ProductResponse> applyLocale(List<ProductResponse> responses, String locale) {
    if (responses == null) {
      return List.of();
    }
    responses.forEach(r -> applyLocale(r, locale));
    return responses;
  }

  // ---------- Admin write APIs ----------
  @CacheEvict(value = "products", allEntries = true)
  @Transactional
  public ProductTranslation upsertProduct(ProductTranslation t) {
    return productTranslationRepo.save(t);
  }

  @CacheEvict(value = "products", allEntries = true)
  @Transactional
  public CategoryTranslation upsertCategory(CategoryTranslation t) {
    return categoryTranslationRepo.save(t);
  }
}
