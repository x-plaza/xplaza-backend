package com.backend.xplaza.service;

import com.backend.xplaza.model.Product;
import com.backend.xplaza.model.ProductImage;
import com.backend.xplaza.model.ProductList;
import com.backend.xplaza.repository.ProductImageRepository;
import com.backend.xplaza.repository.ProductListRepository;
import com.backend.xplaza.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductListRepository productListRepo;
    @Autowired
    private ProductImageRepository productImageRepo;

    @Transactional
    public void addProduct(Product product) {
        productRepo.save(product);
        for (ProductImage productImage : product.getProductImage()) {
            productImage.setProduct_id(product.getId());
            productImageRepo.save(productImage);
        }
    }

    @Transactional
    public void updateProduct(Product product) {
        productImageRepo.deleteImagesByProductId(product.getId());
        for (ProductImage productImage : product.getProductImage()) {
            productImage.setProduct_id(product.getId());
            productImageRepo.save(productImage);
        }
        productRepo.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productImageRepo.deleteImagesByProductId(id);
        productRepo.deleteById(id);
    }

    public String getProductNameByID(Long id) { return productRepo.getName(id); }

    public List<ProductList> listProducts() {
        return productListRepo.findAllProductList();
    }

    public ProductList listProduct(Long id) {
        return productListRepo.findProductListById(id);
    }

    public List<ProductList> listProductsByUserID(Long user_id) {
        return productListRepo.findAllProductListByUserID(user_id);
    }

    public List<ProductList> listProductsByShopID(Long shop_id) {
        return productListRepo.findAllProductListByShopID(shop_id);
    }
}
