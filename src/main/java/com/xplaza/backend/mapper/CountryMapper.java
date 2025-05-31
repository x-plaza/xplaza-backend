/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.dao.CountryDAO;
import com.xplaza.backend.dto.*;
import com.xplaza.backend.entity.*;

@Mapper(componentModel = "spring")
public interface CountryMapper {
  CountryEntity toEntity(CountryRequestDTO dto);

  CountryDAO toDAO(CountryEntity entity);

  CountryResponseDTO toResponseDTO(CountryDAO dao);

  CountryDAO toDAOFromResponse(CountryResponseDTO dto);

  CountryRequestDTO toRequestDTO(CountryEntity entity);

  CountryEntity toEntityFromDAO(CountryDAO dao);
}
