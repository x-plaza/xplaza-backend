/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.xplaza.backend.model.OrderDetails;
import com.xplaza.backend.model.OrderList;
import com.xplaza.backend.model.OrderPlace;
import com.xplaza.backend.model.OrderPlaceList;
import com.xplaza.backend.model.OrderResponse;
import com.xplaza.backend.model.PlatformInfo;
import com.xplaza.backend.model.ProductInventory;
import com.xplaza.backend.service.OrderService;
import com.xplaza.backend.service.PlatformInfoService;
import com.xplaza.backend.service.RoleService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController {
  @Autowired
  private OrderService orderService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private PlatformInfoService platformInfoService;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getOrdersByAdmin(
      @RequestParam(value = "user_id") @Valid Long admin_user_id,
      @RequestParam(value = "status", required = false) @Valid String status,
      @RequestParam(value = "order_date", required = false) @Valid String order_date)
      throws JsonProcessingException, JSONException, ParseException {
    start = new Date();
    List<OrderList> dtos;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    String role_name = roleService.getRoleNameByUserID(admin_user_id);
    if (role_name == null)
      dtos = null;
    else if (role_name.equals("Master Admin")) {
      if (status == null && order_date == null)
        dtos = orderService.listOrdersByAdmin();
      else if (status != null && order_date == null)
        dtos = orderService.listOrdersByStatusByAdmin(status);
      else {
        if (status == null)
          status = "Pending";
        Date date = formatter.parse(order_date);
        dtos = orderService.listOrdersByFilterByAdmin(status, date);
      }
    } else {
      if (status == null && order_date == null)
        dtos = orderService.listOrdersByAdminUserID(admin_user_id);
      else if (status != null && order_date == null)
        dtos = orderService.listOrdersByStatusAndAdminUserID(status, admin_user_id);
      else {
        if (status == null)
          status = "Pending";
        Date date = formatter.parse(order_date);
        dtos = orderService.listOrdersByFilterAndAdminUserID(status, date, admin_user_id);
      }
    }

    // Update Invoice number
    PlatformInfo platformInfo = platformInfoService.listPlatform();
    if (dtos != null) {
      dtos.forEach(i -> i.setInvoice_number(platformInfo.getInvoice() + "#" + i.getOrder_id()));
    }

    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Order List By Admin\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/by-customer")
  public ResponseEntity<String> getOrdersByCustomer(
      @RequestParam(value = "customer_id") @Valid Long customer_id,
      @RequestParam(value = "status", required = false) @Valid String status,
      @RequestParam(value = "order_date", required = false) @Valid String order_date)
      throws JsonProcessingException, JSONException, ParseException {
    start = new Date();
    List<OrderPlaceList> dtos = null;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    if (status == null && order_date == null)
      dtos = orderService.listOrdersByCustomer(customer_id);
    else if (status != null && order_date == null)
      dtos = orderService.listOrdersByStatusByCustomer(customer_id, status);
    else if (status != null) {
      Date date = formatter.parse(order_date);
      dtos = orderService.listOrdersByFilterByCustomer(customer_id, status, date);
    }
    // Update Invoice number
    PlatformInfo platformInfo = platformInfoService.listPlatform();
    if (dtos != null) {
      dtos.forEach(i -> i.setInvoice_number(platformInfo.getInvoice() + "#" + i.getOrder_id()));
    }

    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Order List By Customer\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/{id}", "/by-customer/{id}" })
  public ResponseEntity<String> getOrderDetails(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    OrderDetails dtos = orderService.listOrderDetails(id);
    // Update Invoice number
    PlatformInfo platformInfo = platformInfoService.listPlatform();
    dtos.setInvoice_number(platformInfo.getInvoice() + "#" + dtos.getOrder_id());
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Order Details\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addOrder(@RequestBody @Valid OrderPlace order)
      throws ParseException, JsonProcessingException {
    start = new Date();

    // Check product availability
    ProductInventory productInventory = orderService.checkProductAvailability(order);
    if (!productInventory.is_available()) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.FORBIDDEN.value(), "Error",
          "Sorry! The item " + productInventory.getName() + " has " + productInventory.getMax_available_quantity()
              + " unit(s) left. " +
              "Please update your order accordingly.",
          null), HttpStatus.FORBIDDEN);
    }

    // Validate Coupon
    if (order.getCoupon_id() != null) {
      if (!orderService.checkCouponValidity(order, "id")) {
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.FORBIDDEN.value(),
            "Error", "Coupon is not valid!", null), HttpStatus.FORBIDDEN);
      }
      if (!orderService.checkCouponValidity(order, "code")) {
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.FORBIDDEN.value(),
            "Error", "Coupon is not valid!", null), HttpStatus.FORBIDDEN);
      }
    }

    // Set Order Prices
    order = orderService.setOrderPrices(order);

    // Place Order
    order = orderService.addOrder(order);

    // Add Invoice Code
    ObjectMapper mapper = new ObjectMapper();
    OrderResponse dtos = new OrderResponse();
    PlatformInfo platformInfo = platformInfoService.listPlatform();
    dtos.setInvoice_number(platformInfo.getInvoice() + "#" + order.getInvoice_number());
    dtos.setGrand_total_price(order.getGrand_total_price());

    // Send email
    orderService.sendOrderDetailsToCustomer(order, dtos, platformInfo);
    orderService.sendOrderDetailsToShopAdmin(order, dtos, platformInfo);

    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order", HttpStatus.CREATED.value(),
        "Success", "Order has been created.", mapper.writeValueAsString(dtos)), HttpStatus.CREATED);
  }

  @PutMapping("/status-update")
  public ResponseEntity<ApiResponse> updateOrderStatus(@RequestParam("invoice_number") @Valid Long invoice_number,
      @RequestParam("status") @Valid Long status,
      @RequestParam(value = "remarks", required = false) @Valid String remarks) {
    start = new Date();
    orderService.updateOrderStatus(invoice_number, remarks, status);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Order Status", HttpStatus.OK.value(),
        "Success", "Order Status has been updated.", null), HttpStatus.OK);
  }
}
