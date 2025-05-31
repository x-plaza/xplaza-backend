/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.dao.BrandDAO;
import com.xplaza.backend.dto.BrandRequestDTO;
import com.xplaza.backend.dto.BrandResponseDTO;
import com.xplaza.backend.model.Brand;

@Mapper(componentModel = "spring")
public interface BrandMapper {
  Brand toEntity(BrandRequestDTO dto);

  BrandResponseDTO toResponseDTO(Brand brand);

  BrandDAO toDAO(Brand brand);

  Brand toEntityFromDAO(BrandDAO dao);

  BrandRequestDTO toRequestDTO(Brand brand);

  Brand toEntityFromRequest(BrandRequestDTO dto);

  BrandResponseDTO toResponseDTOFromDAO(BrandDAO dao);

  BrandDAO toDAOFromResponse(BrandResponseDTO dto);
}
