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
@Table(name = "top_customer")
public class TopCustomer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "customer_id")
  private Long customer_id;

  @Column(name = "customer_name")
  private String name;

  @Column(name = "total_order_amount")
  private Double total_order_amount;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "shop_id", insertable = false, updatable = false)
  private Dashboard dashboard;
}
