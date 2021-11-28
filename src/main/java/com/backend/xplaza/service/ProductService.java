package com.backend.xplaza.service;

import com.backend.xplaza.model.Product;
import com.backend.xplaza.model.ProductImage;
import com.backend.xplaza.model.ProductList;
import com.backend.xplaza.model.ProductSearch;
import com.backend.xplaza.repository.ProductImageRepository;
import com.backend.xplaza.repository.ProductListRepository;
import com.backend.xplaza.repository.ProductRepository;
import com.backend.xplaza.repository.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ProductListRepository productListRepo;
    @Autowired
    private ProductImageRepository productImageRepo;
    @Autowired
    private ProductSearchRepository productSearchRepo;

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
        List<ProductImage> updatedImageList = new ArrayList<ProductImage>();
        for (ProductImage productImage : product.getProductImage()) {
            productImage.setProduct_id(product.getId());
            ProductImage updatedImage = productImageRepo.save(productImage);
            updatedImageList.add(updatedImage);
        }
        product.setProductImage(updatedImageList);
        productRepo.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productImageRepo.deleteImagesByProductId(id);
        productRepo.deleteById(id);
    }

    public String getProductNameByID(Long id) { return productRepo.getName(id); }

    public List<ProductList> listProducts() {
        List<ProductList> listOfProducts = productListRepo.findAllProductList();
        for (ProductList p: listOfProducts) {
            if(p.getDiscount_amount() != null && p.getDiscount_type_name() != null) {
                Double discounted_price = 0.0;
                if(p.getDiscount_type_name().equals("Fixed Amount")) discounted_price = p.getSelling_price() - p.getDiscount_amount();
                else discounted_price = p.getSelling_price() - (p.getSelling_price() * p.getDiscount_amount())/100; // for percentage type
                p.setDiscounted_price(Math.round(discounted_price * 100.0) / 100.0); // set discounted price
            }
        }
        return listOfProducts;
    }

    public ProductList listProduct(Long id) {
        ProductList p = productListRepo.findProductListById(id);
        //p.setBuying_price(null);
        if(p.getDiscount_amount() != null && p.getDiscount_type_name() != null) {
            Double discounted_price = 0.0;
            if(p.getDiscount_type_name().equals("Fixed Amount")) discounted_price = p.getSelling_price() - p.getDiscount_amount();
            else discounted_price = p.getSelling_price() - (p.getSelling_price() * p.getDiscount_amount())/100; // for percentage type
            p.setDiscounted_price(Math.round(discounted_price * 100.0) / 100.0); // set discounted price
        }
        return p;
    }

    public List<ProductList> listProductsByUserID(Long user_id) {
        List<ProductList> listOfProducts =  productListRepo.findAllProductListByUserID(user_id);
        for (ProductList p: listOfProducts) {
            if(p.getDiscount_amount() != null && p.getDiscount_type_name() != null) {
                Double discounted_price = 0.0;
                if(p.getDiscount_type_name().equals("Fixed Amount")) discounted_price = p.getSelling_price() - p.getDiscount_amount();
                else discounted_price = p.getSelling_price() - (p.getSelling_price() * p.getDiscount_amount())/100; // for percentage type
                p.setDiscounted_price(Math.round(discounted_price * 100.0) / 100.0); // set discounted price
            }
        }
        return listOfProducts;
    }

    public List<ProductList> listProductsByShopID(Long shop_id) {
        List<ProductList> listOfProducts = productListRepo.findAllProductListByShopID(shop_id);
        //listOfProducts.forEach((i) -> i.setBuying_price(null)); // Lambda function
        for (ProductList p: listOfProducts) {
            p.setBuying_price(null);
            if(p.getDiscount_amount() != null && p.getDiscount_type_name() != null) {
                Double discounted_price = 0.0;
                if(p.getDiscount_type_name().equals("Fixed Amount")) discounted_price = p.getSelling_price() - p.getDiscount_amount();
                else discounted_price = p.getSelling_price() - (p.getSelling_price() * p.getDiscount_amount())/100; // for percentage type
                p.setDiscounted_price(Math.round(discounted_price * 100.0) / 100.0); // set discounted price
            }
        }
        return listOfProducts;
    }

    public List<ProductList> listProductsByCategory(Long shop_id, Long category_id) {
        List<ProductList> listOfProducts =  productListRepo.findAllProductListByCategory(shop_id,category_id);
        //listOfProducts.forEach((i) -> i.setBuying_price(null));
        for (ProductList p: listOfProducts) {
            p.setBuying_price(null);
            if(p.getDiscount_amount() != null && p.getDiscount_type_name() != null) {
                Double discounted_price = 0.0;
                if(p.getDiscount_type_name().equals("Fixed Amount")) discounted_price = p.getSelling_price() - p.getDiscount_amount();
                else discounted_price = p.getSelling_price() - (p.getSelling_price() * p.getDiscount_amount())/100; // for percentage type
                p.setDiscounted_price(Math.round(discounted_price * 100.0) / 100.0); // set discounted price
            }
        }
        return listOfProducts;
    }

    public List<ProductSearch> listProductsByName(Long shop_id, String product_name) {
        return productSearchRepo.findProductListByName(shop_id,product_name);
    }
}
