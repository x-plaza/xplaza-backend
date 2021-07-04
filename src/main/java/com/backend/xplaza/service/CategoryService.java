package com.backend.xplaza.service;

import com.backend.xplaza.model.Category;
import com.backend.xplaza.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;

    public void addCategory(Category category) {
        categoryRepo.save(category);
    }

    public void updateCategory(Category category) {
        categoryRepo.save(category);
    }

    public String getCategoryNameByID(long id) {
        return categoryRepo.getName(id);
    }

    public void deleteCategory(long id) {
        categoryRepo.deleteById(id);
    }

    public List<Category> listCategories() {
        return categoryRepo.findAll();
    }

    public Category listCategory(long id) {
        return categoryRepo.findCategoryById(id);
    }
}
