/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.Module;
import com.backend.xplaza.repository.ModuleRepository;

@Service
public class ModuleService {
  @Autowired
  private ModuleRepository moduleRepo;

  public void addModule(Module module) {
    moduleRepo.save(module);
  }

  public void updateModule(Module module) {
    moduleRepo.save(module);
  }

  public List<Module> listModules() {
    return moduleRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }

  public String getModuleNameByID(Long id) {
    return moduleRepo.getName(id);
  }

  public void deleteModule(Long id) {
    moduleRepo.deleteById(id);
  }

  public Module listModule(Long id) {
    return moduleRepo.findModuleById(id);
  }
}
