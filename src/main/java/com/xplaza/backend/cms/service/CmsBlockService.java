/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cms.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.cms.domain.entity.CmsBlock;
import com.xplaza.backend.cms.domain.repository.CmsBlockRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CmsBlockService {

  private final CmsBlockRepository repo;

  @Cacheable(value = "cms-blocks", key = "#code + ':' + #locale")
  @Transactional(readOnly = true)
  public Optional<CmsBlock> getActive(String code, String locale) {
    return repo.findByCodeAndLocaleAndActiveTrue(code, locale != null ? locale : "en");
  }

  @Transactional(readOnly = true)
  public List<CmsBlock> listActive() {
    return repo.findByActiveTrue();
  }

  @CacheEvict(value = "cms-blocks", allEntries = true)
  public CmsBlock save(CmsBlock block) {
    return repo.save(block);
  }

  @CacheEvict(value = "cms-blocks", allEntries = true)
  public void delete(Long id) {
    repo.deleteById(id);
  }
}
