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
@Table(name = "order_items")
public class OrderItemPlaceList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long id;

  @Column(name = "order_item_name")
  private String item_name;

  public String getItem_name() {
    return item_name + " (" + item_var_type_value + item_var_type_name + ")";
  }

  @Column(name = "order_item_var_type_name")
  private String item_var_type_name;

  @Column(name = "order_item_var_type_value")
  private Long item_var_type_value;

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

  @ManyToOne(cascade = CascadeType.ALL)
  @JsonBackReference
  @JoinColumn(name = "fk_order_id", referencedColumnName = "order_id", insertable = false, updatable = false)
  private OrderPlaceList orderPlaceList;
}
