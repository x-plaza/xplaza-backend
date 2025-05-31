/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.dao.StateDAO;
import com.xplaza.backend.dto.StateRequestDTO;
import com.xplaza.backend.dto.StateResponseDTO;
import com.xplaza.backend.entity.StateEntity;
import com.xplaza.backend.mapper.StateMapper;
import com.xplaza.backend.repository.StateEntityRepository;

@Service
public class StateService {
  @Autowired
  private StateEntityRepository stateEntityRepository;

  @Autowired
  private StateMapper stateMapper;

  public void addState(StateRequestDTO dto) {
    StateEntity entity = stateMapper.toEntity(dto);
    stateEntityRepository.save(entity);
  }

  public void updateState(StateRequestDTO dto) {
    StateEntity entity = stateMapper.toEntity(dto);
    stateEntityRepository.save(entity);
  }

  public void deleteState(Long id) {
    stateEntityRepository.deleteById(id);
  }

  public StateResponseDTO listState(Long id) {
    StateEntity entity = stateEntityRepository.findById(id).orElse(null);
    StateDAO dao = stateMapper.toDAO(entity);
    return stateMapper.toResponseDTO(dao);
  }

  public List<StateResponseDTO> listStates() {
    return stateEntityRepository.findAll().stream()
        .map(stateMapper::toDAO)
        .map(stateMapper::toResponseDTO)
        .collect(Collectors.toList());
  }
}
