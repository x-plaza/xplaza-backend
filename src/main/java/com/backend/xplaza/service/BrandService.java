package com.backend.xplaza.service;

import com.backend.xplaza.model.Brand;
import com.backend.xplaza.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepo;

    public void addBrand(Brand brand) {
        brandRepo.save(brand);
    }

    public void updateBrand(Brand brand) {
        brandRepo.save(brand);
    }

    public String getBrandNameByID(Long id) {
        return brandRepo.getName(id);
    }

    public void deleteBrand(Long id) {
        brandRepo.deleteById(id);
    }

    public List<Brand> listBrands() {
        return brandRepo.findAll();
    }
}
