package com.backend.xplaza.service;

import com.backend.xplaza.model.Product;
import com.backend.xplaza.model.ProductDiscount;
import com.backend.xplaza.model.ProductDiscountList;
import com.backend.xplaza.repository.ProductDiscountListRepository;
import com.backend.xplaza.repository.ProductDiscountRepository;
import com.backend.xplaza.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDiscountService {
    @Autowired
    private ProductDiscountRepository productDiscountRepo;
    @Autowired
    private ProductDiscountListRepository productDiscountListRepo;
    @Autowired
    private ProductRepository productRepo;

    public boolean checkDiscountValidity(ProductDiscount productDiscount) {
        Product product = productRepo.findProductById(productDiscount.getProduct_id());
        Double original_selling_price = product.getSelling_price();
        if(productDiscount.getDiscount_amount() > original_selling_price)
            return false;

        return true;
    }

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
