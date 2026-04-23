/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.domain.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

/**
 * Product Attribute defines a characteristic that products can have. Examples:
 * Color, Size, Material, Weight, Pattern
 * 
 * Attributes can be: - Variant attributes: These create different purchasable
 * SKUs (Color, Size) - Non-variant attributes: Additional product info
 * (Material, Weight)
 */
@Entity
@Table(name = "attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attribute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "attribute_id")
  private Long attributeId;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 20)
  @Builder.Default
  private AttributeType type = AttributeType.SELECT;

  @Column(name = "is_variant_attribute")
  @Builder.Default
  private Boolean isVariantAttribute = false;

  @Column(name = "is_filterable")
  @Builder.Default
  private Boolean isFilterable = true;

  @Column(name = "is_searchable")
  @Builder.Default
  private Boolean isSearchable = true;

  @Column(name = "position")
  @Builder.Default
  private Integer position = 0;

  @Column(name = "is_active")
  @Builder.Default
  private Boolean isActive = true;

  @Column(name = "category_id")
  private Long categoryId;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  @Builder.Default
  private Instant updatedAt = Instant.now();

  @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  private List<AttributeValue> values = new ArrayList<>();

  /**
   * Supported attribute types.
   */
  public enum AttributeType {
    SELECT,
    MULTI_SELECT,
    TEXT,
    NUMBER,
    BOOLEAN
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public void addValue(AttributeValue value) {
    values.add(value);
    value.setAttribute(this);
  }

  public void removeValue(AttributeValue value) {
    values.remove(value);
    value.setAttribute(null);
  }
}
