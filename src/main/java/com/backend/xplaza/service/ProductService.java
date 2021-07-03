package com.backend.xplaza.service;

import com.backend.xplaza.model.Product;
import com.backend.xplaza.model.ProductList;
import com.backend.xplaza.repository.ProductListRepository;
import com.backend.xplaza.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductListRepository productListRepo;

    public void addProduct(Product product) {
        productRepo.save(product);
    }

    public void updateProduct(Product product) {
        productRepo.save(product);
    }

    public String getProductNameByID(Long id) { return productRepo.getName(id); }

    public Long getMaxID() {
        return productRepo.getMaxID();
    }

    public void deleteImagesByProductId(Long id) { productRepo.deleteImagesByProductId(id); }

    public void deleteProduct(Long id) { productRepo.deleteById(id); }

    public List<ProductList> listProducts() {
        return productListRepo.findAllProductList();
    }

    public ProductList listProduct(long id) {
        return productListRepo.findProductListById(id);
    }

    public List<ProductList> listProductsByUserID(long user_id) {
        return productListRepo.findAllProductListByUserID(user_id);
    }
}
