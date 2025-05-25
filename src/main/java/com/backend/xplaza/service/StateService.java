/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.State;
import com.backend.xplaza.repository.StateRepository;

@Service
public class StateService {
  @Autowired
  private StateRepository stateRepo;

  public void addState(State city) {
    stateRepo.save(city);
  }

  public void updateState(State city) {
    stateRepo.save(city);
  }

  public String getStateNameByID(Long id) {
    return stateRepo.getName(id);
  }

  public void deleteState(Long id) {
    stateRepo.deleteById(id);
  }

  public List<State> listStates() {
    return stateRepo.findAll();
  }

  public State listState(Long id) {
    return stateRepo.findStateById(id);
  }
}
