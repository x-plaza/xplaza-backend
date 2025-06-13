/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public Category addCategory(Category category) {
    CategoryDao categoryDao = categoryMapper.toDao(category);
    CategoryDao savedCategoryDao = categoryRepo.save(categoryDao);
    return categoryMapper.toEntityFromDao(savedCategoryDao);
  }

  @Transactional
  public Category updateCategory(Long id, Category category) {
    CategoryDao existingCategoryDao = categoryRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

    category.setCategoryId(id);
    CategoryDao categoryDao = categoryMapper.toDao(category);
    CategoryDao updatedCategoryDao = categoryRepo.save(categoryDao);
    return categoryMapper.toEntityFromDao(updatedCategoryDao);
  }

  @Transactional
  public void deleteCategory(Long id) {
    categoryRepo.deleteById(id);
  }

  public List<Category> listCategories() {
    List<CategoryDao> categoryDaos = categoryRepo.findAll();
    return categoryDaos.stream()
        .map(categoryMapper::toEntityFromDao)
        .collect(Collectors.toList());
  }

  public Category listCategory(Long id) {
    CategoryDao categoryDao = categoryRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    return categoryMapper.toEntityFromDao(categoryDao);
  }

  public String getCategoryNameByID(Long id) {
    return categoryRepo.getName(id);
  }
}
