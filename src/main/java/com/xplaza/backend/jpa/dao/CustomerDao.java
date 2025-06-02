/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long customerId;

  private String firstName;
  private String lastName;
  private String houseNo;
  private String streetName;
  private Integer postcode;
  private String area;
  private String city;
  private String country;
  private String mobileNo;
  private String email;
  private Date dateOfBirth;
  private String password;
  private String salt;
  private String otp;
  private Date createdAt;
  private Date updatedAt;
}