/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.domain.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

/**
 * Storefront CMS block. Used for static content slots like the homepage hero
 * banner, footer copy, return policy etc. Lookup is by the composite
 * {@code (code, locale)} key so the same {@code code} can ship a localised
 * variant per supported locale.
 */
@Entity
@Table(name = "cms_blocks", uniqueConstraints = @UniqueConstraint(name = "uq_cms_blocks_code_locale", columnNames = {
    "code", "locale" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmsBlock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String code;

  @Column(length = 255)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String body;

  @Column(length = 10)
  @Builder.Default
  private String locale = "en";

  @Column(name = "is_active")
  @Builder.Default
  private Boolean active = Boolean.TRUE;

  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  private Instant updatedAt = Instant.now();

  @PreUpdate
  void touch() {
    this.updatedAt = Instant.now();
  }
}
