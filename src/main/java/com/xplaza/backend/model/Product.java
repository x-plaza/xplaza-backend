/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Long id;

  @Column(name = "product_name")
  private String name;

  @Column(name = "product_description")
  private String description;

  @Column(name = "fk_brand_id")
  private Long brand_id;

  @Column(name = "fk_category_id")
  private Long category_id;

  @Column(name = "fk_product_var_type_id")
  private Long product_var_type_id;

  private Double product_var_type_value;

  @Column(name = "product_buying_price")
  private Double buying_price;

  @Column(name = "product_selling_price")
  private Double selling_price;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "fk_shop_id")
  private Long shop_id;

  @Column(name = "quantity")
  private Long quantity;

  @OneToMany(mappedBy = "product")
  private List<ProductImage> productImage;
}
