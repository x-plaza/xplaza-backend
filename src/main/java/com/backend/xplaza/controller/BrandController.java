package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Brand;
import com.backend.xplaza.service.BrandService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
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
@RequestMapping("/api/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    private Date start, end;
    private long responseTime;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Set-Cookie", "type=ninja");
    }

    @GetMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBrands() throws JsonProcessingException, JSONException {
        start = new Date();
        List<Brand> dtos = brandService.listBrands();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Brand List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBrand(@PathVariable @Valid long id) throws JsonProcessingException {
        start = new Date();
        Brand dtos = brandService.listBrand(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Brand List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value= "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addBrand (@RequestBody @Valid Brand brand) {
        start = new Date();
        brandService.addBrand(brand);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Brand", HttpStatus.CREATED.value(),"Success", "Brand has been created.",null), HttpStatus.CREATED);
    }

    @PutMapping(value= "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> updateBrand (@RequestBody @Valid Brand brand) {
        start = new Date();
        brandService.updateBrand(brand);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Brand", HttpStatus.OK.value(),"Success", "Brand has been updated.",null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteBrand (@PathVariable @Valid long id) {
        String brand_name = brandService.getBrandNameByID(id);
        start = new Date();
        brandService.deleteBrand(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Brand", HttpStatus.OK.value(),"Success", brand_name + " has been deleted.",null), HttpStatus.OK);
    }
}
