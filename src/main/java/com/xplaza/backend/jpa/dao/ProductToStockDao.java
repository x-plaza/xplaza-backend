/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "product_to_stock")
public class ProductToStockDao {
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "remaining_unit")
  private Integer remainingUnit;

  @Column(name = "shop_id")
  private Long shopId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getRemainingUnit() {
    return remainingUnit;
  }

  public void setRemainingUnit(Integer remainingUnit) {
    this.remainingUnit = remainingUnit;
  }

  public Long getShopId() {
    return shopId;
  }

  public void setShopId(Long shopId) {
    this.shopId = shopId;
  }
}
