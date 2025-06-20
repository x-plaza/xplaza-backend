/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "customers")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long customerId;

  String firstName;

  String lastName;

  String houseNo;

  String streetName;

  Integer postcode;

  String area;

  String city;

  String country;

  String mobileNo;

  String email;

  Date dateOfBirth;

  String password;

  String salt;

  String otp;

  Date createdAt;

  Date updatedAt;
}