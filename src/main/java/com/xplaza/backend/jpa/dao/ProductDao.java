/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  private String productName;
  private String productDescription;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_brand_id")
  private BrandDao brand;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_category_id")
  private CategoryDao category;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_product_var_type_id")
  private ProductVariationTypeDao productVariationType;

  private Integer productVarTypeValue;
  private Double productBuyingPrice;
  private Double productSellingPrice;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_currency_id")
  private CurrencyDao currency;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "fk_shop_id")
  private ShopDao shop;

  private Integer quantity;
  private Integer createdBy;
  private Date createdAt;
  private Integer lastUpdatedBy;
  private Date lastUpdatedAt;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ProductImageDao> images = new ArrayList<>();
}