package com.backend.xplaza.service;

import com.backend.xplaza.model.Day;
import com.backend.xplaza.repository.DayNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayNameService {
    @Autowired
    private DayNameRepository dayRepo;

    public void addDay(Day day) {
        dayRepo.save(day);
    }

    public void updateDay(Day day) {
        dayRepo.save(day);
    }

    public String getDayNameByID(Long id) {
        return dayRepo.getName(id);
    }

    public void deleteDay(Long id) {
        dayRepo.deleteById(id);
    }

    public List<Day> listDays() {
        return dayRepo.findAll();
    }
}
