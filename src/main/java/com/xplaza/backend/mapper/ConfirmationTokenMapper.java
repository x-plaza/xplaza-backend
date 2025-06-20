/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.jpa.dao.ConfirmationTokenDao;
import com.xplaza.backend.service.entity.ConfirmationToken;

@Mapper(componentModel = "spring")
public interface ConfirmationTokenMapper {
  ConfirmationTokenDao toDao(ConfirmationToken entity);

  ConfirmationToken toEntityFromDao(ConfirmationTokenDao dao);
}
