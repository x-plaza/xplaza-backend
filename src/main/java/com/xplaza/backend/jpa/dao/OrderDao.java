/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long orderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_payment_type_id")
  PaymentTypeDao paymentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_status_id")
  StatusCatalogueDao status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  CurrencyDao currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_customer_id")
  CustomerDao customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_shop_id")
  ShopDao shop;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_coupon_id")
  CouponDao coupon;

  Long invoiceNumber;
  Double totalPrice;
  Double discountAmount;
  Double netTotal;
  Double grandTotalPrice;
  String deliveryAddress;
  Long customerId;
  Long shopId;
  Long deliveryCostId;
  Long paymentTypeId;
  Long statusId;
  Long couponId;
  Date receivedTime;
  Date dateToDeliver;
  Long currencyId;
  String additionalInfo;
  String remarks;
  String customerName;
  String shopName;
  Double deliveryCost;
  String couponCode;
  Double couponAmount;
  String mobileNo;

  Double totalAmount; // legacy field
  Date createdAt;
  Date updatedAt;

  @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  List<OrderItemDao> orderItems;
}