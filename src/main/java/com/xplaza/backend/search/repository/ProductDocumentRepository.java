/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.search.document.ProductDocument;

@Repository
public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, String> {
}
