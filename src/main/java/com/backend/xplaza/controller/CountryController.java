package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Country;
import com.backend.xplaza.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/country")
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping(value = { "", "/" })
    public ResponseEntity<List<Country>> getCountries() {
        List<Country> dtos = countryService.listCountries();
        return new ResponseEntity<List<Country>>(dtos, HttpStatus.OK);
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
        return new ResponseEntity<>(new ApiResponse(true, country_name + " has been deleted."), HttpStatus.CREATED);
    }
}