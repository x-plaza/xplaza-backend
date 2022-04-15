package com.backend.xplaza.service;

import com.backend.xplaza.model.Dashboard;
import com.backend.xplaza.model.MonthlySales;
import com.backend.xplaza.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class DashboardService {
    @Autowired
    private DashboardRepository dashboardRepo;

    public Dashboard getDashboardDetails(Long shop_id) {
        return dashboardRepo.findAllDetailsByShopId(shop_id);
    }

    public Double getMonthlyProfit(Long shop_id, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), month-1, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
        return dashboardRepo.findProfitByMonthByShopId(shop_id, formatter.format(cal.getTime()));
    }

    public Double getMonthlySales(Long shop_id, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), month-1, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
        return dashboardRepo.findSalesByMonthByShopId(shop_id, formatter.format(cal.getTime()));
    }
}
