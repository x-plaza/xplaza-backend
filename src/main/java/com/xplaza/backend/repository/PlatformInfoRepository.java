/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.model.PlatformInfo;

public interface PlatformInfoRepository extends JpaRepository<PlatformInfo, Long> {
  @Modifying
  @Transactional
  @Query(value = "update platform_info set additional_info=?1, cell_no=?2, banner_image=?3, banner_image_path=?4", nativeQuery = true)
  void update(String additional_info, String cell_no, String banner_image, String path);
}
