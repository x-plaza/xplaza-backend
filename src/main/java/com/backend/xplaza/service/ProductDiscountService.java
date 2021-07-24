package com.backend.xplaza.service;

import com.backend.xplaza.model.ProductDiscount;
import com.backend.xplaza.model.ProductDiscountList;
import com.backend.xplaza.repository.ProductDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDiscountService {
    @Autowired
    private ProductDiscountRepository productDiscountRepo;

    public void addProductDiscount(ProductDiscount productDiscount) {
        productDiscountRepo.save(productDiscount);
    }

    public void updateProductDiscount(ProductDiscount productDiscount) {
        productDiscountRepo.save(productDiscount);
    }

    public String getProductNameByID(Long id) {
        return productDiscountRepo.getName(id);
    }

    public void deleteProductDiscount(Long id) {
        productDiscountRepo.deleteById(id);
    }

    public List<ProductDiscountList> listProductDiscounts() {
        return productDiscountRepo.findAllProductDiscounts();
    }

    public ProductDiscountList listProductDiscount(Long id) { return productDiscountRepo.findProductDiscountById(id); }

    public List<ProductDiscountList> listProductDiscountsByUserID(Long user_id) { return productDiscountRepo.findAllProductDiscountByUserID(user_id); }
}
