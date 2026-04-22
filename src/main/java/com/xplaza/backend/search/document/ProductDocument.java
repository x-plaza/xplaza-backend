/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.search.document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "products")
@Setting(settingPath = "/elasticsearch/product-settings.json")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocument {

  @Id
  private String id;

  @Field(type = FieldType.Long)
  private Long productId;

  @Field(type = FieldType.Text, analyzer = "english", searchAnalyzer = "english", copyTo = { "search_all" })
  private String name;

  @Field(type = FieldType.Search_As_You_Type)
  private String nameSuggest;

  @Field(type = FieldType.Text, analyzer = "english", copyTo = { "search_all" })
  private String description;

  @Field(type = FieldType.Keyword)
  private String slug;

  @Field(type = FieldType.Keyword)
  private String brand;

  @Field(type = FieldType.Keyword)
  private String category;

  @Field(type = FieldType.Keyword)
  private List<String> tags;

  @Field(type = FieldType.Double)
  private Double price;

  @Field(type = FieldType.Keyword)
  private String currency;

  @Field(type = FieldType.Long)
  private Long shopId;

  @Field(type = FieldType.Keyword)
  private String shopName;

  @Field(type = FieldType.Integer)
  private Integer quantity;

  @Field(type = FieldType.Boolean)
  private Boolean published;

  @Field(type = FieldType.Double)
  private Double averageRating;

  @Field(type = FieldType.Integer)
  private Integer reviewCount;

  @Field(type = FieldType.Object)
  private Map<String, Object> attributes;

  @Field(type = FieldType.Date)
  private Instant indexedAt;

  @Field(type = FieldType.Text)
  private String search_all;
}
