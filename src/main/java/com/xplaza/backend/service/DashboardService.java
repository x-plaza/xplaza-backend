/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.Dashboard;
import com.xplaza.backend.repository.DashboardRepository;

@Service
public class DashboardService {
  @Autowired
  private DashboardRepository dashboardRepo;

  public Dashboard getDashboardDetails(Long shop_id) {
    return dashboardRepo.findAllDetailsByShopId(shop_id);
  }

  public Double getMonthlyProfit(Long shop_id, int month) {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.get(Calendar.YEAR), month - 1, 1);
    SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
    return dashboardRepo.findProfitByMonthByShopId(shop_id, formatter.format(cal.getTime()));
  }

  public Double getMonthlySales(Long shop_id, int month) {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.get(Calendar.YEAR), month - 1, 1);
    SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
    return dashboardRepo.findSalesByMonthByShopId(shop_id, formatter.format(cal.getTime()));
  }
}
