/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cart.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

/**
 * Entity representing an item in a shopping cart.
 */
@Entity
@Table(name = "cart_items", indexes = {
    @Index(name = "idx_cart_item_cart_id", columnList = "cart_id"),
    @Index(name = "idx_cart_item_product_id", columnList = "product_id"),
    @Index(name = "idx_cart_item_variant_id", columnList = "variant_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "cart")
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "cart_item_id")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id", nullable = false)
  @com.fasterxml.jackson.annotation.JsonIgnore
  private Cart cart;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "variant_id")
  private UUID variantId;

  @Column(name = "shop_id", nullable = false)
  private Long shopId;

  @Column(nullable = false)
  @Builder.Default
  private Integer quantity = 1;

  @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
  private BigDecimal unitPrice;

  @Column(name = "original_price", precision = 10, scale = 2)
  private BigDecimal originalPrice;

  @Column(name = "discount_amount", precision = 10, scale = 2)
  @Builder.Default
  private BigDecimal discountAmount = BigDecimal.ZERO;

  @Column(name = "discount_percentage", precision = 5, scale = 2)
  private BigDecimal discountPercentage;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "variant_name")
  private String variantName;

  @Column(name = "sku", length = 50)
  private String sku;

  @Column(name = "image_url")
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private ItemStatus status = ItemStatus.ACTIVE;

  @Column(name = "added_at", nullable = false, updatable = false)
  @Builder.Default
  private Instant addedAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  private Instant updatedAt = Instant.now();

  @Column(name = "custom_attributes", columnDefinition = "TEXT")
  private String customAttributes;

  // ==================== Status enum ====================

  public enum ItemStatus {
    ACTIVE,
    SAVED_FOR_LATER,
    REMOVED,
    OUT_OF_STOCK
  }

  // ==================== Lifecycle ====================

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  // ==================== Business methods ====================

  public BigDecimal getLineTotal() {
    BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(quantity));
    if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
      total = total.subtract(discountAmount);
    }
    return total.max(BigDecimal.ZERO);
  }

  public BigDecimal getLineTotalBeforeDiscount() {
    BigDecimal price = originalPrice != null ? originalPrice : unitPrice;
    return price.multiply(BigDecimal.valueOf(quantity));
  }

  public BigDecimal getTotalSavings() {
    return getLineTotalBeforeDiscount().subtract(getLineTotal());
  }

  public boolean hasDiscount() {
    return (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) ||
        (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0);
  }

  public void saveForLater() {
    this.status = ItemStatus.SAVED_FOR_LATER;
  }

  public void moveToCart() {
    this.status = ItemStatus.ACTIVE;
  }

  public void markRemoved() {
    this.status = ItemStatus.REMOVED;
  }

  public void markOutOfStock() {
    this.status = ItemStatus.OUT_OF_STOCK;
  }

  public void updatePrice(BigDecimal newUnitPrice, BigDecimal newOriginalPrice) {
    this.unitPrice = newUnitPrice;
    this.originalPrice = newOriginalPrice;
  }

  public void applyDiscount(BigDecimal amount, BigDecimal percentage) {
    this.discountAmount = amount;
    this.discountPercentage = percentage;
  }

  public void removeDiscount() {
    this.discountAmount = BigDecimal.ZERO;
    this.discountPercentage = null;
  }

  public void incrementQuantity(int amount) {
    this.quantity = this.quantity + amount;
  }

  public boolean decrementQuantity(int amount) {
    if (this.quantity - amount < 1) {
      return false;
    }
    this.quantity = this.quantity - amount;
    return true;
  }

  public boolean isActive() {
    return this.status == ItemStatus.ACTIVE;
  }

  public boolean isSavedForLater() {
    return this.status == ItemStatus.SAVED_FOR_LATER;
  }
}
