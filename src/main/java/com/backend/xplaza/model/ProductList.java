/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class ProductList {
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

  @Column(name = "brand_name")
  private String brand_name;

  @Column(name = "fk_category_id")
  private Long category_id;

  @Column(name = "category_name")
  private String category_name;

  @Column(name = "fk_product_var_type_id")
  private Long product_var_type_id;

  @Column(name = "var_type_name")
  private String product_var_type_name;

  private Double product_var_type_value;

  @Column(name = "product_buying_price")
  private Double buying_price;

  @Column(name = "product_selling_price")
  private Double selling_price;

  @Column(name = "discount_amount")
  private Double discount_amount;

  @Column(name = "discount_type_name")
  private String discount_type_name;

  @Column(name = "discount_start_date")
  private Date discount_start_date;

  public String getDiscount_start_date() {
    if (discount_start_date != null)
      return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(discount_start_date);
    return null;
  }

  @Column(name = "discount_end_date")
  private Date discount_end_date;

  public String getDiscount_end_date() {
    if (discount_end_date != null)
      return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(discount_end_date);
    return null;
  }

  @Column(name = "discounted_price")
  private Double discounted_price;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "currency_name")
  private String currency_name;

  @Column(name = "currency_sign")
  private String currency_sign;

  @Column(name = "fk_shop_id")
  private Long shop_id;

  @Column(name = "shop_name")
  private String shop_name;

  @Column(name = "quantity")
  private Long quantity;

  @OneToMany(mappedBy = "productList")
  private List<ProductImageList> productImageList;
}
