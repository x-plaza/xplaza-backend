/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.util.Date;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.http.dto.OrderDetailsRequestDTO;
import com.xplaza.backend.http.dto.OrderDetailsResponseDTO;
import com.xplaza.backend.mapper.OrderDetailsMapper;
import com.xplaza.backend.service.OrderService;
import com.xplaza.backend.service.PlatformInfoService;
import com.xplaza.backend.service.RoleService;
import com.xplaza.backend.service.entity.OrderDetailsEntity;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController {
  @Autowired
  private OrderService orderService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private PlatformInfoService platformInfoService;

  @Autowired
  private OrderDetailsMapper orderDetailsMapper;

  private Date start, end;
  private Long responseTime;

  @GetMapping("/{id}")
  public ResponseEntity<OrderDetailsResponseDTO> getOrder(@PathVariable @Valid Long id) {
    OrderDetailsEntity entity = orderService.getOrderDetailsEntity(id);
    OrderDetailsResponseDTO dto = orderDetailsMapper.toResponseDTO(entity);
    return ResponseEntity.ok(dto);
  }

  @PostMapping
  public ResponseEntity<Void> addOrder(@RequestBody @Valid OrderDetailsRequestDTO requestDTO) {
    OrderDetailsEntity entity = orderDetailsMapper.toEntity(requestDTO);
    orderService.addOrder(entity);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping
  public ResponseEntity<Void> updateOrder(@RequestBody @Valid OrderDetailsRequestDTO requestDTO) {
    OrderDetailsEntity entity = orderDetailsMapper.toEntity(requestDTO);
    orderService.updateOrder(entity);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable @Valid Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.ok().build();
  }
}
