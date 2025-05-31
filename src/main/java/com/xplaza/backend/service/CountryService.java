/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.dao.CountryDAO;
import com.xplaza.backend.dto.CountryRequestDTO;
import com.xplaza.backend.dto.CountryResponseDTO;
import com.xplaza.backend.entity.CountryEntity;
import com.xplaza.backend.mapper.CountryMapper;
import com.xplaza.backend.repository.CountryEntityRepository;

@Service
public class CountryService {
  @Autowired
  private CountryEntityRepository countryEntityRepository;

  @Autowired
  private CountryMapper countryMapper;

  public void addCountry(CountryRequestDTO dto) {
    CountryEntity entity = countryMapper.toEntity(dto);
    countryEntityRepository.save(entity);
  }

  public void updateCountry(CountryRequestDTO dto) {
    CountryEntity entity = countryMapper.toEntity(dto);
    countryEntityRepository.save(entity);
  }

  public void deleteCountry(Long id) {
    countryEntityRepository.deleteById(id);
  }

  public CountryResponseDTO listCountry(Long id) {
    CountryEntity entity = countryEntityRepository.findById(id).orElse(null);
    CountryDAO dao = countryMapper.toDAO(entity);
    return countryMapper.toResponseDTO(dao);
  }

  public List<CountryResponseDTO> listCountries() {
    return countryEntityRepository.findAll().stream()
        .map(countryMapper::toDAO)
        .map(countryMapper::toResponseDTO)
        .collect(Collectors.toList());
  }
}
