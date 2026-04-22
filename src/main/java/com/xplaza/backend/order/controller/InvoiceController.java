/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.order.controller;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.exception.ResourceNotFoundException;
import com.xplaza.backend.order.domain.repository.CustomerOrderItemRepository;
import com.xplaza.backend.order.domain.repository.CustomerOrderRepository;
import com.xplaza.backend.order.service.InvoicePdfService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class InvoiceController {

  private final CustomerOrderRepository orderRepository;
  private final CustomerOrderItemRepository itemRepository;
  private final InvoicePdfService pdfService;

  @GetMapping(value = "/{orderId}/invoice", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> downloadInvoice(@PathVariable UUID orderId) {
    var order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("order_not_found"));
    var items = itemRepository.findByOrderOrderId(orderId);
    var pdf = pdfService.generate(order, items);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + order.getOrderNumber() + ".pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdf);
  }
}
