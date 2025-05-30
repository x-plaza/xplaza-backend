/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.Day;
import com.xplaza.backend.repository.DayNameRepository;

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
