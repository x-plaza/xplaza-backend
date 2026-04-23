/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

/**
 * ProductVariant represents a specific purchasable version of a product.
 * 
 * A Product can have multiple variants based on attributes like Color and Size.
 * For example: - Product: "Nike Air Max 90" - Variants: - SKU-001: Black, Size
 * 10, $120 - SKU-002: White, Size 10, $120 - SKU-003: Black, Size 11, $125
 * 
 * Each variant has its own: - SKU (Stock Keeping Unit) - Price - Inventory
 * levels - Barcode
 */
@Entity
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

  @Id
  @Column(name = "variant_id")
  @Builder.Default
  private UUID variantId = UUID.randomUUID();

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "sku", nullable = false, unique = true, length = 100)
  private String sku;

  @Column(name = "name", length = 255)
  private String name;

  @Column(name = "price", nullable = false, precision = 15, scale = 2)
  private BigDecimal price;

  @Column(name = "compare_at_price", precision = 15, scale = 2)
  private BigDecimal compareAtPrice;

  @Column(name = "cost_price", precision = 15, scale = 2)
  private BigDecimal costPrice;

  @Column(name = "currency_id")
  private Long currencyId;

  @Column(name = "barcode", length = 50)
  private String barcode;

  // Physical dimensions for shipping calculations
  @Column(name = "weight_grams", precision = 10, scale = 2)
  private BigDecimal weightGrams;

  @Column(name = "length_cm", precision = 10, scale = 2)
  private BigDecimal lengthCm;

  @Column(name = "width_cm", precision = 10, scale = 2)
  private BigDecimal widthCm;

  @Column(name = "height_cm", precision = 10, scale = 2)
  private BigDecimal heightCm;

  @Column(name = "is_default")
  @Builder.Default
  private Boolean isDefault = false;

  @Column(name = "position")
  @Builder.Default
  private Integer position = 0;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20)
  @Builder.Default
  private VariantStatus status = VariantStatus.ACTIVE;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  @Builder.Default
  private Instant updatedAt = Instant.now();

  @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @Builder.Default
  private List<VariantAttribute> attributes = new ArrayList<>();

  @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  private List<VariantImage> images = new ArrayList<>();

  public enum VariantStatus {
    ACTIVE,
    INACTIVE,
    DISCONTINUED
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public BigDecimal getProfitMargin() {
    if (costPrice == null || costPrice.compareTo(BigDecimal.ZERO) == 0) {
      return null;
    }
    return price.subtract(costPrice)
        .divide(costPrice, 4, java.math.RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }

  public BigDecimal getDiscountPercentage() {
    if (compareAtPrice == null || compareAtPrice.compareTo(price) <= 0) {
      return BigDecimal.ZERO;
    }
    return compareAtPrice.subtract(price)
        .divide(compareAtPrice, 4, java.math.RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));
  }

  public String getDisplayName() {
    if (name != null && !name.isBlank()) {
      return name;
    }
    if (attributes == null || attributes.isEmpty()) {
      return sku;
    }
    return attributes.stream()
        .map(attr -> attr.getAttributeValue().getDisplayValue())
        .reduce((a, b) -> a + " / " + b)
        .orElse(sku);
  }

  public void addAttribute(VariantAttribute attribute) {
    attributes.add(attribute);
    attribute.setVariant(this);
  }

  public void addImage(VariantImage image) {
    images.add(image);
    image.setVariant(this);
  }
}
