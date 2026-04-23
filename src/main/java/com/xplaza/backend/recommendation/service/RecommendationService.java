/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.recommendation.service;

import java.time.Instant;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.catalog.domain.entity.Product;
import com.xplaza.backend.catalog.domain.repository.ProductRepository;
import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.order.domain.repository.CustomerOrderRepository;
import com.xplaza.backend.recommendation.domain.entity.ProductCoPurchase;
import com.xplaza.backend.recommendation.domain.entity.RecentlyViewedProduct;
import com.xplaza.backend.recommendation.domain.repository.ProductCoPurchaseRepository;
import com.xplaza.backend.recommendation.domain.repository.RecentlyViewedProductRepository;

/**
 * Lightweight in-DB recommendation engine. For Amazon-grade ranking this should
 * later be replaced with an offline collaborative-filtering pipeline, but the
 * API contract here is forward-compatible with that swap.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

  private final RecentlyViewedProductRepository recentlyViewedRepo;
  private final ProductCoPurchaseRepository coPurchaseRepo;
  private final ProductRepository productRepo;
  private final CustomerOrderRepository orderRepo;

  @Transactional
  public void recordView(Long customerId, Long productId) {
    if (customerId == null) {
      return;
    }
    var rec = RecentlyViewedProduct.builder()
        .customerId(customerId)
        .productId(productId)
        .viewedAt(Instant.now())
        .build();
    recentlyViewedRepo.save(rec);
  }

  @Transactional(readOnly = true)
  public List<Product> recentlyViewed(Long customerId, int limit) {
    return recentlyViewedRepo
        .findByCustomerIdOrderByViewedAtDesc(customerId, PageRequest.of(0, limit))
        .stream()
        .flatMap(rv -> productRepo.findById(rv.getProductId()).stream())
        .toList();
  }

  @Cacheable(value = "products", key = "'related-' + #productId + '-' + #limit")
  @Transactional(readOnly = true)
  public List<Product> related(Long productId, int limit) {
    return productRepo.findById(productId).map(p -> {
      // Same category, exclude self.
      if (p.getCategory() == null) {
        return List.<Product>of();
      }
      return productRepo.findAll().stream()
          .filter(o -> o.getCategory() != null
              && o.getCategory().getCategoryId().equals(p.getCategory().getCategoryId()))
          .filter(o -> !o.getProductId().equals(productId))
          .limit(limit)
          .toList();
    }).orElse(List.of());
  }

  @Cacheable(value = "products", key = "'fbt-' + #productId + '-' + #limit")
  @Transactional(readOnly = true)
  public List<Product> frequentlyBoughtTogether(Long productId, int limit) {
    return coPurchaseRepo.findTopByProductId(productId, PageRequest.of(0, limit)).stream()
        .map(ProductCoPurchase::getCoProductId)
        .flatMap(id -> productRepo.findById(id).stream())
        .toList();
  }

  @Async
  @EventListener
  @Transactional
  public void onOrderPlaced(DomainEvents.OrderPlaced event) {
    try {
      var order = orderRepo.findByIdWithItems(event.orderId()).orElse(null);
      if (order == null || order.getItems() == null || order.getItems().size() < 2) {
        return;
      }
      var productIds = order.getItems().stream()
          .map(i -> i.getProductId())
          .filter(java.util.Objects::nonNull)
          .distinct()
          .sorted()
          .toList();
      for (int i = 0; i < productIds.size(); i++) {
        for (int j = i + 1; j < productIds.size(); j++) {
          Long a = productIds.get(i);
          Long b = productIds.get(j);
          coPurchaseRepo.incrementPair(a, b);
          coPurchaseRepo.incrementPair(b, a);
        }
      }
      log.debug("Recorded {} co-purchase pairs for order {}",
          productIds.size() * (productIds.size() - 1), event.orderId());
    } catch (Exception e) {
      log.warn("Failed to record co-purchases for order {}: {}", event.orderId(), e.toString());
    }
  }
}
