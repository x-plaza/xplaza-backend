/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Immutable;

@Table(name = "order_details")
@Immutable
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class OrderDetailsDao {
  @Id
  private Long id;
  private Long orderId;
  private Double totalPrice;
  private Double discountAmount;
  private Double netTotal;
  private Double grandTotalPrice;
  private String deliveryAddress;
  private Long customerId;
  private Long shopId;
  private Long paymentTypeId;
  private Long statusId;
  private Long couponId;
  private String receivedTime;
  private String dateToDeliver;
  private Long currencyId;
  private String additionalInfo;
  private String remarks;
  private String customerName;
  private String mobileNo;
  private String shopName;
  private String statusName;
  private String allottedTime;
  private Double deliveryCost;
  private String paymentTypeName;
  private Long deliveryId;
  private String personName;
  private String contactNo;
  private String couponCode;
  private Double couponAmount;
  private String orderItemName;
  private String orderItemCategory;
  private Integer orderItemQuantity;
  private String orderItemQuantityType;
  private Double orderItemUnitPrice;
  private Double orderItemTotalPrice;
  private String orderItemImage;
  private Long orderItemId;
  private String currencyName;
  private String currencySign;
}
