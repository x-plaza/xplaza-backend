/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.text.ParseException;
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
import com.xplaza.backend.http.dto.CouponRequestDTO;
import com.xplaza.backend.http.dto.CouponResponseDTO;
import com.xplaza.backend.mapper.CouponMapper;
import com.xplaza.backend.service.CouponService;
import com.xplaza.backend.service.entity.CouponEntity;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController extends BaseController {
  @Autowired
  private CouponService couponService;

  @Autowired
  private CouponMapper couponMapper;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getCoupons(@RequestParam(value = "user_id", required = true) @Valid Long user_id)
      throws JsonProcessingException {
    start = new Date();
    List<CouponEntity> entities = couponService.listCoupons();
    List<CouponResponseDTO> dtos = entities.stream().map(couponMapper::toResponseDTO).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Coupon List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getCouponDetails(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    CouponEntity entity = couponService.listCoupon(id);
    CouponResponseDTO dto = couponMapper.toResponseDTO(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Coupon By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dto) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/validate-coupon")
  public ResponseEntity<ApiResponse> validateCoupon(
      @RequestParam(value = "coupon_code", required = true) @Valid String coupon_code,
      @RequestParam(value = "net_order_amount", required = true) @Valid Double net_order_amount,
      @RequestParam(value = "shop_id", required = true) @Valid Long shop_id) throws ParseException {
    start = new Date();
    if (!couponService.checkCouponValidity(coupon_code, net_order_amount, shop_id)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Validate Coupon", HttpStatus.FORBIDDEN.value(),
          "Error", "Coupon is not valid!", null), HttpStatus.FORBIDDEN);
    }
    Double coupon_amount = couponService.calculateCouponAmount(coupon_code, net_order_amount);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Validate Coupon", HttpStatus.OK.value(), "Success",
        "Coupon is valid.", coupon_amount.toString()), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addCoupon(@RequestBody @Valid CouponRequestDTO couponRequestDTO) {
    start = new Date();
    CouponEntity entity = couponMapper.toEntity(couponRequestDTO);
    couponService.addCoupon(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Coupon", HttpStatus.CREATED.value(),
        "Success", "Coupon has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateCoupon(@RequestBody @Valid CouponRequestDTO couponRequestDTO) {
    start = new Date();
    CouponEntity entity = couponMapper.toEntity(couponRequestDTO);
    couponService.updateCoupon(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Coupon", HttpStatus.OK.value(),
        "Success", "Coupon has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteCoupon(@PathVariable @Valid Long id) {
    start = new Date();
    couponService.deleteCoupon(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Coupon", HttpStatus.OK.value(),
        "Success", "Coupon has been deleted.", null), HttpStatus.OK);
  }
}
