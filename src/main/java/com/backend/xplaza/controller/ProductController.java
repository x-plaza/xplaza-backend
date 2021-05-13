package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Product;
import com.backend.xplaza.model.ProductList;
import com.backend.xplaza.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    private Date start, end;
    long responseTime;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Set-Cookie", "type=ninja");
    }

    @GetMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProducts() throws JsonProcessingException {
        start = new Date();
        List<ProductList> dtos = productService.listProducts();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Product List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProduct(@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        ProductList dtos = productService.listProduct(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Product List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct (@RequestBody @Valid Product product) {
        Long product_id = productService.getMaxID();
        start = new Date();
        productService.addProduct(product);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product", HttpStatus.CREATED.value(),"Success", "Product has been created.","product_id: "+product_id+1), HttpStatus.CREATED);
        //return new ResponseEntity<>(new ApiResponse(true, "Product has been created."), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateProduct (@RequestBody @Valid Product product) {
        start = new Date();
        productService.updateProduct(product);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product", HttpStatus.OK.value(),"Success", "Product has been updated.","[]"), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, "Product has been updated."), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct (@PathVariable @Valid Long id) {
        String product_name = productService.getProductNameByID(id);
        start = new Date();
        productService.deleteProduct(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Product", HttpStatus.OK.value(),"Success", product_name + " has been deleted.","[]"), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, product_name + " has been deleted."), HttpStatus.OK);
    }
}
