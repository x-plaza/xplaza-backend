package com.backend.xplaza.service;

import com.backend.xplaza.model.DeliverySchedule;
import com.backend.xplaza.model.DeliveryScheduleList;
import com.backend.xplaza.repository.DeliveryScheduleListRepository;
import com.backend.xplaza.repository.DeliveryScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryScheduleService {

    @Autowired
    private DeliveryScheduleRepository deliveryScheduleRepo;

    @Autowired
    private DeliveryScheduleListRepository deliveryScheduleListRepo;

    public void addSchedule(DeliverySchedule deliverySchedule) {
        deliveryScheduleRepo.save(deliverySchedule);
    }

    public void updateSchedule(DeliverySchedule deliverySchedule) { deliveryScheduleRepo.save(deliverySchedule); }

    public List<DeliveryScheduleList> listDeliverySchedules() {
        return deliveryScheduleListRepo.findAllItem();
    }

    public DeliveryScheduleList listDeliverySchedule(Long id) { return deliveryScheduleListRepo.findDeliveryScheduleListByDayId(id); }

    public void deleteSchedule(Long id) {
        deliveryScheduleRepo.deleteById(id);
    }
}
