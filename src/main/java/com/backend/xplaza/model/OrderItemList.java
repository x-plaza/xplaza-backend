/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_details")
public class OrderItemList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long id;

  @Column(name = "order_item_name")
  private String item_name;

  @Column(name = "order_item_category")
  private String item_category;

  @Column(name = "order_item_quantity")
  private Long quantity;

  @Column(name = "order_item_quantity_type")
  private String quantity_type;

  @Column(name = "order_item_unit_price")
  private Double unit_price;

  @Column(name = "order_item_total_price")
  private Double item_total_price;

  @Column(name = "order_item_image")
  private String item_image;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "order_id", insertable = false, updatable = false)
  private OrderDetails orderDetails;
}
