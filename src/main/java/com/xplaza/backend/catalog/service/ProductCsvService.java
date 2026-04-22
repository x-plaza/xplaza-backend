/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.catalog.domain.entity.Product;
import com.xplaza.backend.catalog.domain.repository.ProductRepository;

/**
 * Bulk product CSV import/export. The CSV columns are intentionally minimal
 * (name, slug, description, price, currency, quantity) so it can serve as the
 * canonical inventory format for vendors importing from external PIM systems.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCsvService {

  private final ProductRepository productRepository;

  private static final String[] HEADERS = { "id", "name", "slug", "description", "price", "currency", "quantity" };

  public byte[] exportAll() {
    try (var out = new ByteArrayOutputStream();
         var writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
         var printer = new CSVPrinter(writer, CSVFormat.DEFAULT.builder().setHeader(HEADERS).build())) {
      for (Product p : productRepository.findAll()) {
        printer.printRecord(p.getProductId(), p.getProductName(), p.getSlug(), p.getProductDescription(),
            p.getProductSellingPrice(), p.getCurrency() == null ? "" : p.getCurrency().getCurrencyName(),
            p.getQuantity());
      }
      printer.flush();
      return out.toByteArray();
    } catch (Exception e) {
      throw new IllegalStateException("export_failed", e);
    }
  }

  @Transactional
  public ImportSummary importCsv(InputStream input) {
    int created = 0, updated = 0, failed = 0;
    try (var reader = new InputStreamReader(input, StandardCharsets.UTF_8);
         var parser = CSVParser.parse(reader, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
      for (var record : parser) {
        try {
          Long id = parseLong(record.isMapped("id") ? record.get("id") : null);
          var product = id == null ? new Product() : productRepository.findById(id).orElse(new Product());
          product.setProductName(record.get("name"));
          product.setSlug(record.isMapped("slug") ? record.get("slug") : null);
          product.setProductDescription(record.isMapped("description") ? record.get("description") : null);
          product.setProductSellingPrice(parseDouble(record.get("price")));
          product.setQuantity(parseInt(record.get("quantity")));
          var saved = productRepository.save(product);
          if (id == null) created++; else updated++;
          log.debug("Imported product id={} name={}", saved.getProductId(), saved.getProductName());
        } catch (Exception e) {
          log.warn("Failed to import row {}: {}", record.getRecordNumber(), e.getMessage());
          failed++;
        }
      }
    } catch (Exception e) {
      throw new IllegalStateException("import_failed", e);
    }
    return new ImportSummary(created, updated, failed);
  }

  private static Long parseLong(String s) { return s == null || s.isBlank() ? null : Long.valueOf(s.trim()); }
  private static Double parseDouble(String s) { return s == null || s.isBlank() ? null : Double.valueOf(s.trim()); }
  private static Integer parseInt(String s) { return s == null || s.isBlank() ? null : Integer.valueOf(s.trim()); }

  public record ImportSummary(int created, int updated, int failed) {
    public List<String> messages() {
      return List.of("created=" + created, "updated=" + updated, "failed=" + failed);
    }
  }
}
