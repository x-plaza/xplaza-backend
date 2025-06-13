/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "deliveries")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long deliveryId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_order_id")
  OrderDao order;

  String deliveryStatus;

  Date deliveryDate;
}