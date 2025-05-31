/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.dao.CityDAO;
import com.xplaza.backend.dto.CityRequestDTO;
import com.xplaza.backend.dto.CityResponseDTO;
import com.xplaza.backend.entity.CityEntity;
import com.xplaza.backend.mapper.CityMapper;
import com.xplaza.backend.repository.CityEntityRepository;

@Service
public class CityService {
  @Autowired
  private CityEntityRepository cityEntityRepository;

  @Autowired
  private CityMapper cityMapper;

  public void addCity(CityRequestDTO dto) {
    CityEntity entity = cityMapper.toEntity(dto);
    cityEntityRepository.save(entity);
  }

  public void updateCity(CityRequestDTO dto) {
    CityEntity entity = cityMapper.toEntity(dto);
    cityEntityRepository.save(entity);
  }

  public void deleteCity(Long id) {
    cityEntityRepository.deleteById(id);
  }

  public CityResponseDTO listCity(Long id) {
    CityEntity entity = cityEntityRepository.findById(id).orElse(null);
    CityDAO dao = cityMapper.toDAO(entity);
    return cityMapper.toResponseDTO(dao);
  }

  public List<CityResponseDTO> listCities() {
    return cityEntityRepository.findAll().stream()
        .map(cityMapper::toDAO)
        .map(cityMapper::toResponseDTO)
        .collect(Collectors.toList());
  }
}
