package com.backend.xplaza.service;

import com.backend.xplaza.model.Product;
import com.backend.xplaza.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;

    public void addProduct(Product product) {
        productRepo.save(product);
    }

    public void updateProduct(Product product) {
        productRepo.save(product);
    }

    public List<Product> listProducts() {
        return productRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public String getProductNameByID(Long id) {
        return productRepo.getName(id);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
