/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.DayName;
import com.xplaza.backend.jpa.repository.DayDAORepository;
import com.xplaza.backend.mapper.DayMapper;
import com.xplaza.backend.service.entity.DayEntity;

@Service
public class DayNameService {
  @Autowired
  private DayDAORepository dayDAORepo;
  @Autowired
  private DayMapper dayMapper;

  public void addDay(DayEntity entity) {
    DayName dao = dayMapper.toDAO(entity);
    dayDAORepo.save(dao);
  }

  public void updateDay(DayEntity entity) {
    DayName dao = dayMapper.toDAO(entity);
    dayDAORepo.save(dao);
  }

  public String getDayNameByID(Long id) {
    return dayDAORepo.findById(id).map(DayName::getName).orElse(null);
  }

  public void deleteDay(Long id) {
    dayDAORepo.deleteById(id);
  }

  public List<DayEntity> listDays() {
    return dayDAORepo.findAll().stream().map(dayMapper::toEntityFromDAO).collect(Collectors.toList());
  }
}
