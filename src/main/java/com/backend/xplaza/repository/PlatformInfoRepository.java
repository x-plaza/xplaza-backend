package com.backend.xplaza.repository;

import com.backend.xplaza.model.PlatformInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformInfoRepository extends JpaRepository<PlatformInfo, Long> {
}
