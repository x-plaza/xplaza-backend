/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "products")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long productId;

  String productName;

  String productDescription;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_brand_id")
  BrandDao brand;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_category_id")
  CategoryDao category;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_product_var_type_id")
  ProductVariationTypeDao productVariationType;

  Integer productVarTypeValue;

  Double productBuyingPrice;

  Double productSellingPrice;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_currency_id")
  CurrencyDao currency;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_shop_id")
  ShopDao shop;

  Integer quantity;

  Integer createdBy;

  Date createdAt;

  Integer lastUpdatedBy;

  Date lastUpdatedAt;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  List<ProductImageDao> images;
}