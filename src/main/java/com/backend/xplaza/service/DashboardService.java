package com.backend.xplaza.service;

import com.backend.xplaza.model.Dashboard;
import com.backend.xplaza.repository.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    private DashboardRepository dashboardRepo ;

    public Dashboard getDashboardDetails(Long shop_id) {
        return dashboardRepo.findAllDetailsByShopId(shop_id);
    }
}
