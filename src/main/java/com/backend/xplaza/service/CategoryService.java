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

    public String getCategoryNameByID(Long id) {
        return categoryRepo.getName(id);
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    public List<Category> listCategories() {
        return categoryRepo.findAll();
    }
}
