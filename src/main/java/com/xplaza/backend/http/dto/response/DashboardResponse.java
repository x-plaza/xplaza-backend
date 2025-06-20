/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class DashboardResponse {
  private Long shopId;
  private Revenue revenue;
  private List<TopCustomer> topCustomers;
  private List<TopProduct> topProducts;
  private List<ProductToStock> productToStocks;
  // add other fields as needed
}
