package com.backend.xplaza.service;

import com.backend.xplaza.model.ProductImage;
import com.backend.xplaza.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository productImgRepo;

    public void addProductImage(ProductImage product) {
        productImgRepo.save(product);
    }

    public void updateProductImage(ProductImage product) {
        productImgRepo.save(product);
    }

    public List<ProductImage> listProductImages() {
        return productImgRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public String getProductImageNameByID(Long id) {
        return productImgRepo.getName(id);
    }

    public void deleteProductImage(Long id) {
        productImgRepo.deleteById(id);
    }

    public ProductImage listProductImage(long id) {
        return productImgRepo.findItemById(id);
    }
}
