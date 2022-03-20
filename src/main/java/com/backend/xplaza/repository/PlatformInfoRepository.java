package com.backend.xplaza.repository;

import com.backend.xplaza.model.PlatformInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PlatformInfoRepository extends JpaRepository<PlatformInfo, Long> {
    @Modifying
    @Transactional
    @Query(value = "update platform_info set additional_info=?1, cell_no=?2, banner_image=?3, banner_image_path=?4", nativeQuery = true)
    void update(String additional_info, String cell_no, String banner_image, String path);
}
