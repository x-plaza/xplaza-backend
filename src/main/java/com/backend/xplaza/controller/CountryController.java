package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Country;
import com.backend.xplaza.service.CountryService;
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
    }

    @GetMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCountries() throws JsonProcessingException {
        start = new Date();
        List<Country> dtos = countryService.listCountries();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Country List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getCountry(@PathVariable @Valid Long id) throws JsonProcessingException {
        start = new Date();
        Country dtos = countryService.listCountry(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        ObjectMapper mapper = new ObjectMapper();
        String response= "{\n" +
                "  \"responseTime\": "+ responseTime + ",\n" +
                "  \"responseType\": \"Country List\",\n" +
                "  \"status\": 200,\n" +
                "  \"response\": \"Success\",\n" +
                "  \"msg\": \"\",\n" +
                "  \"data\":"+mapper.writeValueAsString(dtos)+"\n}";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCountry (@RequestBody @Valid Country country) {
        start = new Date();
        countryService.addCountry(country);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Country", HttpStatus.CREATED.value(),"Success", "Country has been created.",null), HttpStatus.CREATED);
        //return new ResponseEntity<>(new ApiResponse(true, "Country has been created."), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCountry (@RequestBody @Valid Country country) {
        start = new Date();
        countryService.updateCountry(country);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Update Country", HttpStatus.OK.value(),"Success", "Country has been updated.",null), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, "Country has been updated."), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCountry (@PathVariable @Valid Long id) {
        String country_name = countryService.getCountryNameByID(id);
        start = new Date();
        countryService.deleteCountry(id);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Country", HttpStatus.OK.value(),"Success", country_name + " has been deleted.",null), HttpStatus.OK);
        //return new ResponseEntity<>(new ApiResponse(true, country_name + " has been deleted."), HttpStatus.OK);
    }
}