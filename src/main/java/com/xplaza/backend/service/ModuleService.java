/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.ModuleDao;
import com.xplaza.backend.jpa.repository.ModuleRepository;
import com.xplaza.backend.mapper.ModuleMapper;
import com.xplaza.backend.service.entity.Module;

@Service
public class ModuleService {
  @Autowired
  private ModuleRepository moduleRepo;
  @Autowired
  private ModuleMapper moduleMapper;

  public void addModule(Module module) {
    ModuleDao dao = moduleMapper.toDao(module);
    moduleRepo.save(dao);
  }

  public void updateModule(Module module) {
    ModuleDao dao = moduleMapper.toDao(module);
    moduleRepo.save(dao);
  }

  public List<Module> listModules() {
    return moduleRepo.findAll(Sort.by(Sort.Direction.ASC, "name"))
        .stream().map(moduleMapper::toEntityFromDao).collect(Collectors.toList());
  }

  public String getModuleNameByID(Long id) {
    return moduleRepo.getName(id);
  }

  public void deleteModule(Long id) {
    moduleRepo.deleteById(id);
  }

  public Module listModule(Long id) {
    ModuleDao dao = moduleRepo.findModuleById(id);
    return moduleMapper.toEntityFromDao(dao);
  }
}
