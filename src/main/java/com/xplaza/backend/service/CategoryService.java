/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.Category;
import com.xplaza.backend.model.CategoryList;
import com.xplaza.backend.repository.CategoryListRepository;
import com.xplaza.backend.repository.CategoryRepository;

@Service
public class CategoryService {
  @Autowired
  private CategoryRepository categoryRepo;
  @Autowired
  private CategoryListRepository categoryListRepo;

  public void addCategory(Category category) {
    categoryRepo.save(category);
  }

  public void updateCategory(Category category) {
    categoryRepo.save(category);
  }

  public String getCategoryNameByID(Long id) {
    return categoryRepo.getName(id);
  }

  public void deleteCategory(Long id) {
    categoryRepo.deleteById(id);
  }

  public List<CategoryList> listCategories() {
    return categoryListRepo.findAllCategories();
  }

  public CategoryList listCategory(Long id) {
    return categoryListRepo.findCategoryById(id);
  }

  public boolean isExist(Category category) {
    return categoryRepo.existsByName(category.getName());
  }

  public boolean hasChildCategory(Long id) {
    return categoryRepo.hasChildCategory(id);
  }
}
