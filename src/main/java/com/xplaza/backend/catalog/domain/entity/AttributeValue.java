/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.domain.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

/**
 * Represents a possible value for an attribute.
 * 
 * Examples: - For "Color" attribute: Red, Blue, Green, Black, White - For
 * "Size" attribute: XS, S, M, L, XL, XXL
 */
@Entity
@Table(name = "attribute_values", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "attribute_id", "code" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeValue {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "value_id")
  private Long valueId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attribute_id", nullable = false)
  private Attribute attribute;

  @Column(name = "display_value", nullable = false, length = 255)
  private String displayValue;

  @Column(name = "code", nullable = false, length = 100)
  private String code;

  @Column(name = "metadata", columnDefinition = "TEXT")
  private String metadata;

  @Column(name = "position")
  @Builder.Default
  private Integer position = 0;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  public Long getAttributeId() {
    return attribute != null ? attribute.getAttributeId() : null;
  }

  public String getAttributeName() {
    return attribute != null ? attribute.getName() : null;
  }
}
