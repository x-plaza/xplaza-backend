/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "temp_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempOrderDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tempOrderId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_customer_id")
  private CustomerDao customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_status_id")
  private StatusCatalogueDao status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_shop_id")
  private ShopDao shop;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_delivery_schedule_id")
  private DeliveryScheduleDao deliverySchedule;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_delivery_cost_id")
  private DeliveryCostDao deliveryCost;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_payment_type_id")
  private PaymentTypeDao paymentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_delivery_id")
  private DeliveryDao delivery;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_coupon_id")
  private CouponDao coupon;

  private Double totalAmount;
  private Date createdAt;

  @OneToMany(mappedBy = "tempOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<TempOrderItemDao> tempOrderItems = new ArrayList<>();
}