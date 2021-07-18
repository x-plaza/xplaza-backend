package com.backend.xplaza.service;

import com.backend.xplaza.model.ProductVarType;
import com.backend.xplaza.repository.ProductVarTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVarTypeService {
    @Autowired
    private ProductVarTypeRepository prodVarTypeRepo;

    public void addProductVarType(ProductVarType productVarType) {
        prodVarTypeRepo.save(productVarType);
    }

    public void updateProductVarType(ProductVarType productVarType) {
        prodVarTypeRepo.save(productVarType);
    }

    public List<ProductVarType> listProductVarTypes() { return prodVarTypeRepo.findAll(Sort.by(Sort.Direction.ASC, "name")); }

    public String getProductVarTypeNameByID(Long id) {
        return prodVarTypeRepo.getName(id);
    }

    public void deleteProductVarType(Long id) {
        prodVarTypeRepo.deleteById(id);
    }

    public ProductVarType listProductVarType(Long id) {
        return prodVarTypeRepo.findProdVarTypeById(id);
    }
}
