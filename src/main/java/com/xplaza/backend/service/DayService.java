/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.DayDao;
import com.xplaza.backend.jpa.repository.DayRepository;
import com.xplaza.backend.mapper.DayMapper;
import com.xplaza.backend.service.entity.Day;

@Service
public class DayService {
  @Autowired
  private DayRepository dayRepo;
  @Autowired
  private DayMapper dayMapper;

  public void addDay(Day entity) {
    DayDao dao = dayMapper.toDao(entity);
    dayRepo.save(dao);
  }

  public void updateDay(Day entity) {
    DayDao dao = dayMapper.toDao(entity);
    dayRepo.save(dao);
  }

  public String getDayNameByID(Long id) {
    return dayRepo.findById(id)
        .map(dayMapper::toEntityFromDao)
        .map(Day::getDayName)
        .orElse(null);
  }

  public void deleteDay(Long id) {
    dayRepo.deleteById(id);
  }

  public List<Day> listDays() {
    return dayRepo.findAll().stream().map(dayMapper::toEntityFromDao).collect(Collectors.toList());
  }
}
