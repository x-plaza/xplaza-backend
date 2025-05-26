/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_images")
public class ProductImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_image_id")
  private Long id;

  @Column(name = "product_image_name")
  private String name;

  @Column(name = "product_image_path")
  private String path;

  @Column(name = "fk_product_id")
  private Long product_id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JsonBackReference
  @JoinColumn(name = "fk_product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
  private Product product;
}
