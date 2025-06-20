/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long userId;

  @OneToOne
  @JoinColumn(name = "fk_customer_id")
  CustomerDao customer;

  String userName;

  String userEmail;

  String password;

  String salt;
}