package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.ProductVarType;
import com.backend.xplaza.service.ProductVarTypeService;
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
@RequestMapping("/api/prodvartype")
public class ProductVarTypeController {
    @Autowired
    private ProductVarTypeService prodVarTypeService;
    private Date start, end;
    long responseTime;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Set-Cookie", "type=ninja");
        response.setHeader("msg", "");
    }

    @GetMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProductVarTypes() throws JsonProcessingException {
        start = new Date();
        List<ProductVarType> dtos = prodVarTypeService.listProductVarTypes();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Product Variation Type List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProductVarType(@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        ProductVarType dtos = prodVarTypeService.listProductVarType(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Product Variation Type List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProductVarType (@RequestBody @Valid ProductVarType productVarType) {
        start = new Date();
        prodVarTypeService.addProductVarType(productVarType);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product Variation Type", HttpStatus.CREATED.value(),"Success", "Product Variation Type has been created.","[]"), HttpStatus.CREATED);
        //return new ResponseEntity<>(new ApiResponse(true, "Product Variation Type has been created."), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateProductVarType (@RequestBody @Valid ProductVarType productVarType) {
        start = new Date();
        prodVarTypeService.updateProductVarType(productVarType);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product Variation Type", HttpStatus.OK.value(),"Success", "Product Variation Type has been updated.","[]"), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, "Product Variation Type has been updated."), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProductVarType (@PathVariable @Valid Long id) {
        String prod_var_type_name = prodVarTypeService.getProductVarTypeNameByID(id);
        start = new Date();
        prodVarTypeService.deleteProductVarType(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Product Variation Type", HttpStatus.OK.value(),"Success", prod_var_type_name + " has been deleted.","[]"), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, prod_var_type_name + " has been deleted."), HttpStatus.OK);
    }
}
