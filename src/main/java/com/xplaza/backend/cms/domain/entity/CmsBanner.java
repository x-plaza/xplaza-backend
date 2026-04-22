/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.domain.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "cms_banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmsBanner {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "banner_id")
  private Long bannerId;

  @Column(name = "code", unique = true, nullable = false)
  private String code;
  @Column(name = "title")
  private String title;
  @Column(name = "image_url")
  private String imageUrl;
  @Column(name = "link_url")
  private String linkUrl;
  @Column(name = "position")
  private String position;
  @Column(name = "starts_at")
  private Instant startsAt;
  @Column(name = "ends_at")
  private Instant endsAt;
  @Column(name = "active")
  @Builder.Default
  private Boolean active = true;
}
