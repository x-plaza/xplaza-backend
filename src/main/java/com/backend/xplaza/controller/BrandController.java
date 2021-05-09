package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Brand;
import com.backend.xplaza.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = { "", "/" })
    public ResponseEntity<List<Brand>> getBrands() {
        start = new Date();
        List<Brand> dtos = brandService.listBrands();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("responseTime", String.valueOf(responseTime));
        responseHeader.set("responseType", "Brand List");
        responseHeader.set("status", String.valueOf(HttpStatus.OK.value()));
        responseHeader.set("response", "Success");
        return new ResponseEntity<List<Brand>>(dtos, responseHeader, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBrand (@RequestBody @Valid Brand brand) {
        brandService.addBrand(brand);
        return new ResponseEntity<>(new ApiResponse(true, "Brand has been created."), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateBrand (@RequestBody @Valid Brand brand) {
        brandService.updateBrand(brand);
        return new ResponseEntity<>(new ApiResponse(true, "Brand has been updated."), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBrand (@PathVariable @Valid Long id) {
        String brand_name = brandService.getBrandNameByID(id);
        brandService.deleteBrand(id);
        return new ResponseEntity<>(new ApiResponse(true, brand_name + " has been deleted."), HttpStatus.OK);
    }
}
