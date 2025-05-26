/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.PlatformInfo;
import com.xplaza.backend.repository.PlatformInfoRepository;

@Service
public class PlatformInfoService {
  @Autowired
  private PlatformInfoRepository platformInfoRepo;

  public PlatformInfo listPlatform() {
    return platformInfoRepo.findAll().get(0); // for single row
  }

  public void updatePlatformInfo(PlatformInfo platformInfo) {
    platformInfoRepo.update(platformInfo.getAdditional_info(), platformInfo.getCell_no(),
        platformInfo.getBanner_image(), platformInfo.getBanner_image_path());
  }
}
