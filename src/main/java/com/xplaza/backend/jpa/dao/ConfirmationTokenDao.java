/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "confirmation_tokens")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationTokenDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long tokenId;

  String confirmationToken;

  @Column(name = "user_email")
  String email;

  @Temporal(TemporalType.TIMESTAMP)
  Date createdDate;

  @Temporal(TemporalType.TIMESTAMP)
  Date validTill;
}
