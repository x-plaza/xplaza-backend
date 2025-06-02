/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.StateEntityRepository;
import com.xplaza.backend.mapper.StateMapper;
import com.xplaza.backend.service.entity.StateEntity;

@Service
public class StateService {
  @Autowired
  private StateEntityRepository stateEntityRepository;

  @Autowired
  private StateMapper stateMapper;

  public void addState(StateEntity entity) {
    stateEntityRepository.save(entity);
  }

  public void updateState(StateEntity entity) {
    stateEntityRepository.save(entity);
  }

  public void deleteState(Long id) {
    stateEntityRepository.deleteById(id);
  }

  public StateEntity listState(Long id) {
    return stateEntityRepository.findById(id).orElse(null);
  }

  public List<StateEntity> listStates() {
    return stateEntityRepository.findAll();
  }
}
