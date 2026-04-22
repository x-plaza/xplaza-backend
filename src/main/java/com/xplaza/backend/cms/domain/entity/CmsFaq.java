/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.domain.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "cms_faq")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmsFaq {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "faq_id") private Long faqId;

  @Column(name = "question", nullable = false) private String question;
  @Column(name = "answer", columnDefinition = "TEXT", nullable = false) private String answer;
  @Column(name = "category") private String category;
  @Column(name = "display_order") @Builder.Default private Integer displayOrder = 0;
  @Column(name = "active") @Builder.Default private Boolean active = true;
}
