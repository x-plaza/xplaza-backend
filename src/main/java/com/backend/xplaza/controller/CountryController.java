package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Country;
import com.backend.xplaza.service.CountryService;
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
@RequestMapping("/api/country")
public class CountryController {
    @Autowired
    private CountryService countryService;
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
    public ResponseEntity<List<Country>> getCountries() {
        start = new Date();
        List<Country> dtos = countryService.listCountries();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("responseTime", String.valueOf(responseTime));
        responseHeader.set("responseType", "Country List");
        responseHeader.set("status", String.valueOf(HttpStatus.OK.value()));
        responseHeader.set("response", "Success");
        return new ResponseEntity<List<Country>>(dtos, responseHeader, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCountry (@RequestBody @Valid Country country) {
        countryService.addCountry(country);
        return new ResponseEntity<>(new ApiResponse(true, "Country has been created."), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCountry (@PathVariable @Valid Long id) {
        String country_name = countryService.getCountryNameByID(id);
        countryService.deleteCountry(id);
        return new ResponseEntity<>(new ApiResponse(true, country_name + " has been deleted."), HttpStatus.OK);
    }
}