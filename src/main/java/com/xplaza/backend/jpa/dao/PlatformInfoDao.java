/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "platform_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformInfoDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long platformInfoId;

  private String infoKey;
  private String infoValue;
}