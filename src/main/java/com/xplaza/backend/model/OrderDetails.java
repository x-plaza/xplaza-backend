/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_details")
public class OrderDetails {
  @Id
  @Column(name = "order_id")
  private Long order_id;

  private String invoice_number;

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
  private String customer_id;

  @Column(name = "customer_name")
  private String customer_name;

  @Column(name = "mobile_no")
  private String mobile_no;

  @Column(name = "shop_id")
  private Long shop_id;

  @Column(name = "shop_name")
  private String shop_name;

  private String allotted_time;

  @Column(name = "received_time")
  private Date received_time;

  public String getReceived_time() {
    if (received_time != null)
      return new SimpleDateFormat("dd MMM yyyy HH:mm").format(received_time);
    return null;
  }

  @Column(name = "delivery_cost")
  private Long delivery_cost;

  @Column(name = "fk_payment_type_id")
  private Long payment_type_id;

  @Column(name = "payment_type_name")
  private String payment_type_name;

  @Column(name = "fk_status_id")
  private Long status_id;

  @Column(name = "status_name")
  private String status_name;

  @Column(name = "delivery_id")
  private Long delivery_id;

  @Column(name = "person_name")
  private String delivery_person;

  @Column(name = "contact_no")
  private String contact_no;

  @Column(name = "coupon_id")
  private Long coupon_id;

  @Column(name = "coupon_code")
  private String coupon_code;

  @Column(name = "coupon_amount")
  private Long coupon_amount;

  @Column(name = "date_to_deliver")
  private Date date_to_deliver;

  public String getDate_to_deliver() {
    if (date_to_deliver != null)
      return new SimpleDateFormat("dd MMM yyyy").format(date_to_deliver);
    return null;
  }

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "currency_name")
  private String currency_name;

  @Column(name = "currency_sign")
  private String currency_sign;

  @Column(name = "additional_info")
  private String additional_info;

  @Column(name = "remarks")
  private String remarks;

  @OneToMany(mappedBy = "orderDetails")
  private List<OrderItemList> orderItemLists;
}
