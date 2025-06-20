/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "temp_orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TempOrderDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long tempOrderId;

  Double totalPrice;

  Double discountAmount;

  Double grandTotalPrice;

  String deliveryAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_customer_id")
  CustomerDao customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_shop_id")
  ShopDao shop;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_delivery_schedule_id")
  DeliveryScheduleDao deliverySchedule;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_delivery_cost_id")
  DeliveryCostDao deliveryCost;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_payment_type_id")
  PaymentTypeDao paymentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_status_id")
  StatusCatalogueDao status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_delivery_id")
  DeliveryDao delivery;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_coupon_id")
  CouponDao coupon;

  @OneToMany(mappedBy = "tempOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  List<TempOrderItemDao> tempOrderItems;
}