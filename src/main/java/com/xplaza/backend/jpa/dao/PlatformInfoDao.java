/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "platform_info")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlatformInfoDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String name;

  String invoice;

  String cellNo;

  String additionalInfo;

  String bannerImage;

  String bannerImagePath;
}