/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.proxy.HibernateProxy;

import com.backend.xplaza.common.SqlTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderPlace {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long invoice_number;

  @Column(name = "shop_id")
  private Long shop_id;

  @Column(name = "shop_name")
  private String shop_name;

  @Column(name = "customer_id")
  private Long customer_id;

  @Column(name = "customer_name")
  private String customer_name;

  @Column(name = "mobile_no")
  private String mobile_no;

  @Column(name = "delivery_address")
  private String delivery_address;

  @Column(name = "additional_info")
  private String additional_info;

  @Column(name = "remarks")
  private String remarks;

  @JsonFormat(pattern = "HH:mm")
  @JsonDeserialize(using = SqlTimeDeserializer.class)
  @Column(name = "delivery_schedule_start")
  private Time delivery_schedule_start;

  @JsonFormat(pattern = "HH:mm")
  @JsonDeserialize(using = SqlTimeDeserializer.class)
  @Column(name = "delivery_schedule_end")
  private Time delivery_schedule_end;

  @Column(name = "received_time")
  private Date received_time;

  @Column(name = "date_to_deliver")
  private Date date_to_deliver;

  @Column(name = "fk_status_id")
  private Long status_id;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "total_price")
  private Double total_price;

  @Column(name = "net_total")
  private Double net_total;

  @Column(name = "discount_amount")
  private Double discount_amount;

  @Column(name = "coupon_id")
  private Long coupon_id;

  @Column(name = "coupon_code")
  private String coupon_code;

  @Column(name = "coupon_amount")
  private Double coupon_amount;

  @Column(name = "delivery_cost_id")
  private Long delivery_cost_id;

  @Column(name = "delivery_cost")
  private Double delivery_cost;

  @Column(name = "grand_total_price")
  private Double grand_total_price;

  @Column(name = "fk_payment_type_id")
  private Long payment_type_id;

  @OneToMany(mappedBy = "orderPlace")
  private List<OrderItem> orderItemList;

  @Override
  public final boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null)
      return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
        : this.getClass();
    if (thisEffectiveClass != oEffectiveClass)
      return false;
    OrderPlace that = (OrderPlace) o;
    return getInvoice_number() != null && Objects.equals(getInvoice_number(), that.getInvoice_number());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
