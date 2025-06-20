/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.http.dto.request.CurrencyRequest;
import com.xplaza.backend.http.dto.response.CurrencyResponse;
import com.xplaza.backend.mapper.CurrencyMapper;
import com.xplaza.backend.service.CurrencyService;
import com.xplaza.backend.service.entity.Currency;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController extends BaseController {
  private final CurrencyService currencyService;
  private final CurrencyMapper currencyMapper;

  @Autowired
  public CurrencyController(CurrencyService currencyService, CurrencyMapper currencyMapper) {
    this.currencyService = currencyService;
    this.currencyMapper = currencyMapper;
  }

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<ApiResponse> getCurrencies() throws JsonProcessingException {
    start = new Date();
    List<Currency> entities = currencyService.listCurrencies();
    List<CurrencyResponse> dtos = entities.stream().map(currencyMapper::toResponse).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "Currency List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCurrency(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    Currency entity = currencyService.listCurrency(id);
    CurrencyResponse dto = currencyMapper.toResponse(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "Currency By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addCurrency(@RequestBody @Valid CurrencyRequest currencyRequest) {
    start = new Date();
    Currency currency = currencyMapper.toEntity(currencyRequest);
    currencyService.addCurrency(currency);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Currency", HttpStatus.CREATED.value(),
        "Success", "Currency has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse> updateCurrency(@PathVariable Long id,
      @RequestBody @Valid CurrencyRequest currencyRequest) {
    start = new Date();
    Currency currency = currencyMapper.toEntity(currencyRequest);
    currencyService.updateCurrency(id, currency);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Currency", HttpStatus.OK.value(),
        "Success", "Currency has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteCurrency(@PathVariable @Valid Long id) {
    start = new Date();
    currencyService.deleteCurrency(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Currency", HttpStatus.OK.value(),
        "Success", "Currency has been deleted.", null), HttpStatus.OK);
  }
}
