/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cart.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.*;

import lombok.*;

/**
 * Aggregate root representing a shopping cart. Supports both guest and
 * authenticated customers.
 */
@Entity
@Table(name = "carts", indexes = {
    @Index(name = "idx_cart_customer_id", columnList = "customer_id"),
    @Index(name = "idx_cart_session_id", columnList = "session_id"),
    @Index(name = "idx_cart_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "cart_id")
  private UUID id;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "session_id")
  private String sessionId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private CartStatus status = CartStatus.ACTIVE;

  @Column(name = "currency", length = 3)
  @Builder.Default
  private String currencyCode = "USD";

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  @Getter(AccessLevel.NONE)
  private List<CartItem> items = new ArrayList<>();

  public void addCartItem(CartItem item) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(item);
    item.setCart(this);
  }

  public List<CartItem> getItems() {
    return items == null ? List.of() : Collections.unmodifiableList(items);
  }

  @Column(name = "coupon_code", length = 50)
  private String couponCode;

  @Column(name = "coupon_discount", precision = 10, scale = 2)
  @Builder.Default
  private BigDecimal couponDiscount = BigDecimal.ZERO;

  @Column(columnDefinition = "TEXT")
  private String notes;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  private Instant updatedAt = Instant.now();

  @Column(name = "expires_at")
  private Instant expiresAt;

  @Column(name = "last_activity_at")
  @Builder.Default
  private Instant lastActivityAt = Instant.now();

  // ==================== Status enum ====================

  public enum CartStatus {
    ACTIVE,
    MERGED,
    CONVERTED,
    ABANDONED,
    CLEARED
  }

  // ==================== Lifecycle ====================

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
    this.lastActivityAt = Instant.now();
  }

  // ==================== Business methods ====================

  public boolean isEmpty() {
    return items == null || items.isEmpty() || getActiveItems().isEmpty();
  }

  public List<CartItem> getActiveItems() {
    if (items == null)
      return List.of();
    return items.stream()
        .filter(item -> item.getStatus() == CartItem.ItemStatus.ACTIVE)
        .collect(Collectors.toList());
  }

  public List<CartItem> getSavedItems() {
    if (items == null)
      return List.of();
    return items.stream()
        .filter(item -> item.getStatus() == CartItem.ItemStatus.SAVED_FOR_LATER)
        .collect(Collectors.toList());
  }

  public CartItem addItem(Long productId, UUID variantId, Long shopId, int quantity, BigDecimal unitPrice) {
    // Check if item already exists
    CartItem existingItem = findItem(productId, variantId);
    if (existingItem != null && existingItem.getStatus() == CartItem.ItemStatus.ACTIVE) {
      existingItem.setQuantity(existingItem.getQuantity() + quantity);
      return existingItem;
    }

    // Create new item
    CartItem newItem = CartItem.builder()
        .cart(this)
        .productId(productId)
        .variantId(variantId)
        .shopId(shopId)
        .quantity(quantity)
        .unitPrice(unitPrice)
        .build();

    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(newItem);
    return newItem;
  }

  public boolean removeItem(UUID itemId) {
    if (items == null)
      return false;
    return items.removeIf(item -> item.getId().equals(itemId));
  }

  public CartItem findItem(Long productId, UUID variantId) {
    if (items == null)
      return null;
    return items.stream()
        .filter(item -> item.getProductId().equals(productId) &&
            ((variantId == null && item.getVariantId() == null) ||
                (variantId != null && variantId.equals(item.getVariantId()))))
        .findFirst()
        .orElse(null);
  }

  public CartItem getItem(UUID itemId) {
    if (items == null)
      return null;
    return items.stream()
        .filter(item -> item.getId().equals(itemId))
        .findFirst()
        .orElse(null);
  }

  public boolean updateItemQuantity(UUID itemId, int newQuantity) {
    CartItem item = getItem(itemId);
    if (item == null)
      return false;

    if (newQuantity <= 0) {
      return removeItem(itemId);
    }

    item.setQuantity(newQuantity);
    return true;
  }

  public boolean saveItemForLater(UUID itemId) {
    CartItem item = getItem(itemId);
    if (item == null)
      return false;

    item.saveForLater();
    return true;
  }

  public boolean moveToCart(UUID itemId) {
    CartItem item = getItem(itemId);
    if (item == null)
      return false;

    item.moveToCart();
    return true;
  }

  public BigDecimal getSubtotal() {
    return getActiveItems().stream()
        .map(CartItem::getLineTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public int getTotalItemCount() {
    return getActiveItems().stream()
        .mapToInt(CartItem::getQuantity)
        .sum();
  }

  public int getUniqueItemCount() {
    return getActiveItems().size();
  }

  public BigDecimal getTotal() {
    BigDecimal subtotal = getSubtotal();
    if (couponDiscount != null && couponDiscount.compareTo(BigDecimal.ZERO) > 0) {
      subtotal = subtotal.subtract(couponDiscount);
    }
    return subtotal.max(BigDecimal.ZERO);
  }

  public void applyCoupon(String code, BigDecimal discount) {
    this.couponCode = code;
    this.couponDiscount = discount;
  }

  public void removeCoupon() {
    this.couponCode = null;
    this.couponDiscount = BigDecimal.ZERO;
  }

  public void clear() {
    if (items != null) {
      items.clear();
    }
    removeCoupon();
    this.status = CartStatus.CLEARED;
  }

  public void markConverted() {
    this.status = CartStatus.CONVERTED;
  }

  public void markMerged() {
    this.status = CartStatus.MERGED;
  }

  public boolean isOwnedBy(Long customerId) {
    return this.customerId != null && this.customerId.equals(customerId);
  }

  public boolean isGuestCart() {
    return this.customerId == null && this.sessionId != null;
  }

  public void assignToCustomer(Long customerId) {
    this.customerId = customerId;
  }

  public boolean isActive() {
    return this.status == CartStatus.ACTIVE;
  }

  public boolean isExpired() {
    return this.expiresAt != null && Instant.now().isAfter(this.expiresAt);
  }
}
