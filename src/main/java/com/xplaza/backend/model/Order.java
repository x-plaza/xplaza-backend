/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.sql.Time;
import java.util.Date;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long invoice_number;

  @Column(name = "total_price")
  private Double total_price;

  @Column(name = "discount_amount")
  private Double discount_amount;

  @Column(name = "net_total")
  private Double net_total;

  @Column(name = "grand_total_price")
  private Double grand_total_price;

  @Column(name = "delivery_address")
  private String delivery_address;

  @Column(name = "customer_id")
  private Long customer_id;

  @Column(name = "shop_id")
  private Long shop_id;

  @Column(name = "delivery_cost_id")
  private Long delivery_cost_id;

  @Column(name = "fk_payment_type_id")
  private Long payment_type_id;

  @Column(name = "fk_status_id")
  private Long status_id;

  @Column(name = "coupon_id")
  private Long coupon_id;

  @Column(name = "received_time")
  private Date received_time;

  @Column(name = "date_to_deliver")
  private Date date_to_deliver;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "additional_info")
  private String additional_info;

  @Column(name = "remarks")
  private String remarks;

  @Column(name = "customer_name")
  private String customer_name;

  @Column(name = "shop_name")
  private String shop_name;

  @Column(name = "delivery_schedule_start")
  private Time delivery_schedule_start;

  @Column(name = "delivery_schedule_end")
  private Time delivery_schedule_end;

  @Column(name = "delivery_cost")
  private Double delivery_cost;

  @Column(name = "coupon_code")
  private String coupon_code;

  @Column(name = "coupon_amount")
  private Double coupon_amount;

  @Column(name = "mobile_no")
  private String mobile_no;
}
