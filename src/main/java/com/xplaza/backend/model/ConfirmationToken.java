/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "confirmation_tokens")
public class ConfirmationToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "token_id")
  private Long token_id;

  @Column(name = "confirmation_token")
  private String confirmation_token;

  @Column(name = "user_email")
  private String email;

  @Temporal(TemporalType.TIMESTAMP)
  private Date created_date;

  @Temporal(TemporalType.TIMESTAMP)
  private Date valid_till;

  public ConfirmationToken(String email, String type) {
    Random rnd = new Random();
    int number;
    if (Objects.equals(type, "OTP")) {
      number = rnd.nextInt(99999999);
      confirmation_token = String.format("%08d", number);
    } else {
      number = rnd.nextInt(999999);
      confirmation_token = String.format("%06d", number);
    }
    this.email = email;
    created_date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, 30);
    this.valid_till = calendar.getTime();
  }
}
