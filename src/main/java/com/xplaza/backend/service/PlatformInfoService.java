/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.PlatformInfoRepository;
import com.xplaza.backend.mapper.PlatformInfoMapper;
import com.xplaza.backend.service.entity.PlatformInfo;

@Service
public class PlatformInfoService {
  @Autowired
  private PlatformInfoRepository platformInfoRepo;
  @Autowired
  private PlatformInfoMapper platformInfoMapper;

  public PlatformInfo listPlatform() {
    return platformInfoMapper.toEntityFromDao(platformInfoRepo.findAll().getFirst());
  }

  public void updatePlatformInfo(PlatformInfo platformInfo) {
    var existingPlatformInfo = platformInfoRepo.findById(platformInfo.getId())
        .orElseThrow(() -> new IllegalArgumentException("Platform info not found"));
    existingPlatformInfo.setName(platformInfo.getName());
    existingPlatformInfo.setInvoice(platformInfo.getInvoicePrefix());
    existingPlatformInfo.setAdditionalInfo(platformInfo.getAdditionalInfo());
    existingPlatformInfo.setCellNo(platformInfo.getCellNo());
    existingPlatformInfo.setBannerImage(platformInfo.getBannerImage());
    existingPlatformInfo.setBannerImagePath(platformInfo.getBannerImagePath());
    platformInfoRepo.save(existingPlatformInfo);
  }

  public void addPlatformInfo(@Valid PlatformInfo platformInfo) {
    platformInfoRepo.save(platformInfoMapper.toDao(platformInfo));
  }
}
