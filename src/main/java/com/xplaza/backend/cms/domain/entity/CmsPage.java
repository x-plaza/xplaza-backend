/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.domain.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "cms_pages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmsPage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "page_id")
  private Long pageId;

  @Column(name = "slug", nullable = false, unique = true)
  private String slug;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "body", columnDefinition = "TEXT")
  private String body;

  @Column(name = "seo_title")
  private String seoTitle;

  @Column(name = "seo_description")
  private String seoDescription;

  @Column(name = "locale", length = 10)
  @Builder.Default
  private String locale = "en";

  @Column(name = "published")
  @Builder.Default
  private Boolean published = true;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  @Builder.Default
  private Instant updatedAt = Instant.now();
}
