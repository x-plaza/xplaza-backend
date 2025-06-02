/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.CategoryDao;
import com.xplaza.backend.jpa.repository.CategoryRepository;
import com.xplaza.backend.mapper.CategoryMapper;
import com.xplaza.backend.service.entity.Category;

@Service
public class CategoryService {
  @Autowired
  private CategoryRepository categoryRepo;

  @Autowired
  private CategoryMapper categoryMapper;

  public void addCategory(Category category) {
    CategoryDao dao = categoryMapper.toDao(category);
    categoryRepo.save(dao);
  }

  public void updateCategory(Category category) {
    CategoryDao dao = categoryMapper.toDao(category);
    categoryRepo.save(dao);
  }

  public void deleteCategory(Long id) {
    categoryRepo.deleteById(id);
  }

  public List<Category> listCategories() {
    return categoryRepo.findAll().stream().map(categoryMapper::toEntityFromDAO).collect(Collectors.toList());
  }

  public Category listCategory(Long id) {
    CategoryDao dao = categoryRepo.findById(id).orElse(null);
    return categoryMapper.toEntityFromDAO(dao);
  }
}
