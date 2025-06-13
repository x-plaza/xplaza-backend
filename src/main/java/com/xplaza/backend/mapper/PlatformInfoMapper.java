/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.http.dto.request.PlatformInfoRequest;
import com.xplaza.backend.http.dto.response.PlatformInfoResponse;
import com.xplaza.backend.jpa.dao.PlatformInfoDao;
import com.xplaza.backend.service.entity.PlatformInfo;

@Mapper(componentModel = "spring")
public interface PlatformInfoMapper {
  PlatformInfo toEntity(PlatformInfoRequest request);

  PlatformInfoResponse toResponse(PlatformInfo entity);

  PlatformInfoDao toDao(PlatformInfo entity);

  PlatformInfo toEntityFromDao(PlatformInfoDao dao);
}
