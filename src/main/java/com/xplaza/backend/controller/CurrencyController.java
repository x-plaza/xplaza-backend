/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.model.Currency;
import com.xplaza.backend.service.CurrencyService;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController extends BaseController {
  @Autowired
  private CurrencyService currencyService;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getCurrencies() throws JsonProcessingException, JSONException {
    start = new Date();
    List<Currency> dtos = currencyService.listCurrencies();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Currency List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getCurrency(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    Currency dtos = currencyService.listCurrency(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Currency By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addCurrency(@RequestBody @Valid Currency brand) {
    start = new Date();
    currencyService.addCurrency(brand);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Currency", HttpStatus.CREATED.value(),
        "Success", "Currency has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateCurrency(@RequestBody @Valid Currency brand) {
    start = new Date();
    currencyService.updateCurrency(brand);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Currency", HttpStatus.OK.value(),
        "Success", "Currency has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteCurrency(@PathVariable @Valid Long id) {
    String currency_name = currencyService.getCurrencyNameByID(id);
    start = new Date();
    currencyService.deleteCurrency(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Currency", HttpStatus.OK.value(),
        "Success", currency_name + " has been deleted.", null), HttpStatus.OK);
  }
}
