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
@Table(name = "dashboard")
public class DashboardDao {
  @Id
  @Column(name = "shop_id")
  private Long shopId;

  @Column(name = "total_expense")
  private Double totalExpense;

  @Column(name = "total_income")
  private Double totalIncome;

  @Column(name = "total_revenue")
  private Double totalRevenue;

  public Long getShopId() {
    return shopId;
  }

  public void setShopId(Long shopId) {
    this.shopId = shopId;
  }

  public Double getTotalExpense() {
    return totalExpense;
  }

  public void setTotalExpense(Double totalExpense) {
    this.totalExpense = totalExpense;
  }

  public Double getTotalIncome() {
    return totalIncome;
  }

  public void setTotalIncome(Double totalIncome) {
    this.totalIncome = totalIncome;
  }

  public Double getTotalRevenue() {
    return totalRevenue;
  }

  public void setTotalRevenue(Double totalRevenue) {
    this.totalRevenue = totalRevenue;
  }
}
