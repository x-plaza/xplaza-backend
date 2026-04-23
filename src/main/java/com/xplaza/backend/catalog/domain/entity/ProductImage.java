/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.domain.entity;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "product_images")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productImagesId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  private Product product;

  private String productImageName;

  private String productImagePath;

  /** Thumb (~150px) URL produced by the image pipeline. */
  @Column(name = "thumbnail_url", length = 500)
  private String thumbnailUrl;

  /** Medium (~500px) URL produced by the image pipeline. */
  @Column(name = "medium_url", length = 500)
  private String mediumUrl;

  /** Large (~1500px) URL produced by the image pipeline. */
  @Column(name = "large_url", length = 500)
  private String largeUrl;

  /** Alt text for accessibility / SEO. */
  @Column(name = "alt_text", length = 255)
  private String altText;

  /** Display order within the product gallery. */
  @Column(name = "sort_order")
  private Integer sortOrder;

  private Integer createdBy;

  private Date createdAt;

  private Integer lastUpdatedBy;

  private Date lastUpdatedAt;
}
