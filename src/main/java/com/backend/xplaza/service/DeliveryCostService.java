package com.backend.xplaza.service;

import com.backend.xplaza.model.DeliveryCost;
import com.backend.xplaza.model.DeliveryCostList;
import com.backend.xplaza.repository.DeliveryCostListRepository;
import com.backend.xplaza.repository.DeliveryCostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryCostService {
    @Autowired
    private DeliveryCostRepository deliveryCostRepo;
    @Autowired
    private DeliveryCostListRepository deliveryCostListRepo;

    public void addDeliveryCost(DeliveryCost deliveryCost) {
        deliveryCostRepo.save(deliveryCost);
    }

    public void updateDeliveryCost(DeliveryCost deliveryCost) {
        deliveryCostRepo.save(deliveryCost);
    }

    public String getDeliverySlabRangeNameByID(Long id) {
        return deliveryCostRepo.getName(id);
    }

    public void deleteDeliveryCost(Long id) {
        deliveryCostRepo.deleteById(id);
    }

    public List<DeliveryCostList> listDeliveryCosts() {
        return deliveryCostListRepo.findAllDeliveryCost();
    }

    public DeliveryCostList listDeliveryCost(Long id) {
        return deliveryCostListRepo.findDeliveryCostById(id);
    }
}
