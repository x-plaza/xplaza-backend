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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public List<ProductList> listProducts() throws ParseException {
        List<ProductList> listOfProducts = productListRepo.findAllProductList();
        listOfProducts = updateDiscountedPrice(listOfProducts,"admin");
        return listOfProducts;
    }

    public List<ProductList> listProductsByUserID(Long user_id) throws ParseException {
        List<ProductList> listOfProducts =  productListRepo.findAllProductListByUserID(user_id);
        listOfProducts = updateDiscountedPrice(listOfProducts,"admin");
        return listOfProducts;
    }

    public ProductList listProduct(Long id) throws ParseException {
        ProductList p = productListRepo.findProductListById(id);
        p = updateDiscountedPrice(p,"admin");
        return p;
    }

    public List<ProductList> listProductsByShopIDByAdmin(Long shop_id) throws ParseException {
        List<ProductList> listOfProducts = productListRepo.findAllProductListByShopID(shop_id);
        listOfProducts = updateDiscountedPrice(listOfProducts,"admin");
        listOfProducts.forEach((i) -> i.setName(i.getName()+" ("+i.getProduct_var_type_value()+i.getProduct_var_type_name()+")")); // Lambda function
        return listOfProducts;
    }

    public List<ProductList> listProductsByShopID(Long shop_id) throws ParseException {
        List<ProductList> listOfProducts = productListRepo.findAllProductListByShopID(shop_id);
        listOfProducts = updateDiscountedPrice(listOfProducts,"customer");
        return listOfProducts;
    }

    public List<ProductList> listProductsByCategory(Long shop_id, Long category_id) throws ParseException {
        List<ProductList> listOfProducts =  productListRepo.findAllProductListByCategory(shop_id,category_id);
        //listOfProducts.forEach((i) -> i.setBuying_price(null));
        listOfProducts = updateDiscountedPrice(listOfProducts,"customer");
        return listOfProducts;
    }

    public List<ProductSearch> listProductsByName(Long shop_id, String product_name) {
        return productSearchRepo.findProductListByName(shop_id,product_name);
    }
    // for all products
    public List<ProductList> updateDiscountedPrice(List<ProductList> listOfProducts, String type) throws ParseException {
        for (ProductList p: listOfProducts) {
            if (type.equals("customer")) p.setBuying_price(null);
            if(p.getDiscount_amount() != null && p.getDiscount_type_name() != null) {
                Date current_date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                if (current_date.compareTo(formatter.parse(p.getDiscount_start_date())) >= 0
                        && current_date.compareTo(formatter.parse(p.getDiscount_end_date())) <= 0) {
                    Double discounted_price = 0.0;
                    if (p.getDiscount_type_name().equals("Fixed Amount"))
                        discounted_price = p.getSelling_price() - p.getDiscount_amount();
                    else
                        discounted_price = p.getSelling_price() - (p.getSelling_price() * p.getDiscount_amount()) / 100; // for percentage type
                    p.setDiscounted_price(Math.round(discounted_price * 100.0) / 100.0); // set discounted price
                }
            }
        }
        return listOfProducts;
    }
    // for individual product
    public ProductList updateDiscountedPrice(ProductList p, String type) throws ParseException {
        if (type.equals("customer")) p.setBuying_price(null);
        if(p.getDiscount_amount() != null && p.getDiscount_type_name() != null) {
            Date current_date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            if (current_date.compareTo(formatter.parse(p.getDiscount_start_date())) >= 0
                    && current_date.compareTo(formatter.parse(p.getDiscount_end_date())) <= 0) {
                Double discounted_price = 0.0;
                if (p.getDiscount_type_name().equals("Fixed Amount"))
                    discounted_price = p.getSelling_price() - p.getDiscount_amount();
                else
                    discounted_price = p.getSelling_price() - (p.getSelling_price() * p.getDiscount_amount()) / 100; // for percentage type
                p.setDiscounted_price(Math.round(discounted_price * 100.0) / 100.0); // set discounted price
            }
        }
        return p;
    }
}
