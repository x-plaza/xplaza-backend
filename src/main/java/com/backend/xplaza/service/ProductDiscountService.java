package com.backend.xplaza.service;

import com.backend.xplaza.model.ProductDiscount;
import com.backend.xplaza.model.ProductDiscountList;
import com.backend.xplaza.repository.ProductDiscountListRepository;
import com.backend.xplaza.repository.ProductDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDiscountService {
    @Autowired
    private ProductDiscountRepository productDiscountRepo;
    @Autowired
    private ProductDiscountListRepository productDiscountListRepo;

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
        return productDiscountListRepo.findAllProductDiscounts();
    }

    public ProductDiscountList listProductDiscount(Long id) { return productDiscountListRepo.findProductDiscountById(id); }

    public List<ProductDiscountList> listProductDiscountsByUserID(Long user_id) { return productDiscountListRepo.findAllProductDiscountByUserID(user_id); }
}
