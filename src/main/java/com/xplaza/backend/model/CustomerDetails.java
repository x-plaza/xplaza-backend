/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class CustomerDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customer_id")
  private Long id;

  @Column(name = "first_name")
  private String first_name;

  @Column(name = "last_name")
  private String last_name;

  @Column(name = "house_no")
  private String house_no;

  @Column(name = "street_name")
  private String street_name;

  @Column(name = "postcode")
  private String postcode;

  @Column(name = "area")
  private String area;

  @Column(name = "city")
  private String city;

  @Column(name = "country")
  private String country;

  @Column(name = "mobile_no")
  private String mobile_no;

  @Column(name = "email")
  private String email;

  @Column(name = "date_of_birth")
  private Date date_of_birth;

  public String getDate_of_birth() {
    if (date_of_birth != null)
      return new SimpleDateFormat("dd MMM yyyy").format(date_of_birth);
    return null;
  }

  @Column(name = "otp")
  private String otp;

  @Column(name = "salt")
  private String salt;

  @Column(name = "password")
  private String password;
}
