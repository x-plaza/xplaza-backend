/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.dao.CategoryDAO;
import com.xplaza.backend.dto.CategoryRequestDTO;
import com.xplaza.backend.dto.CategoryResponseDTO;
import com.xplaza.backend.model.Category;
import com.xplaza.backend.model.CategoryList;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  Category toEntity(CategoryRequestDTO dto);

  CategoryResponseDTO toResponseDTO(CategoryList list);

  CategoryDAO toDAO(Category category);

  Category toEntityFromDAO(CategoryDAO dao);

  CategoryRequestDTO toRequestDTO(Category category);

  Category toEntityFromRequest(CategoryRequestDTO dto);

  CategoryResponseDTO toResponseDTOFromDAO(CategoryDAO dao);

  CategoryDAO toDAOFromResponse(CategoryResponseDTO dto);
}
