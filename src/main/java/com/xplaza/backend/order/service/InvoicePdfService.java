/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.order.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.xplaza.backend.order.domain.entity.CustomerOrder;
import com.xplaza.backend.order.domain.entity.CustomerOrderItem;

/**
 * Generates a tax invoice PDF for a completed order using OpenPDF (LGPL).
 * Returns the bytes so callers can store them in MinIO or stream to email.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InvoicePdfService {

  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
      .withZone(java.time.ZoneOffset.UTC);

  public byte[] generate(CustomerOrder order, java.util.List<CustomerOrderItem> items) {
    try (var out = new ByteArrayOutputStream()) {
      var doc = new Document();
      PdfWriter.getInstance(doc, out);
      doc.open();

      doc.add(new Paragraph("INVOICE", new Font(Font.HELVETICA, 22, Font.BOLD)));
      doc.add(new Paragraph("Order: " + order.getOrderNumber(), new Font(Font.HELVETICA, 10)));
      doc.add(new Paragraph("Date: " + DATE_FMT.format(order.getCreatedAt()), new Font(Font.HELVETICA, 10)));
      doc.add(new Paragraph(" "));

      var table = new PdfPTable(4);
      table.setWidthPercentage(100);
      table.setWidths(new float[] { 4, 1, 2, 2 });
      addHeader(table, "Description");
      addHeader(table, "Qty");
      addHeader(table, "Unit Price");
      addHeader(table, "Line Total");

      if (items != null) {
        for (var i : items) {
          table.addCell(safe(i.getProductName()));
          table.addCell(String.valueOf(i.getQuantity()));
          table.addCell(String.valueOf(i.getUnitPrice()));
          table.addCell(String.valueOf(i.getTotalPrice()));
        }
      }
      doc.add(table);

      doc.add(new Paragraph(" "));
      doc.add(rightAligned("Subtotal: " + order.getSubtotal()));
      doc.add(rightAligned("Tax: " + order.getTaxAmount()));
      doc.add(rightAligned("Discount: " + order.getDiscountAmount()));
      doc.add(rightAligned("Shipping: " + order.getShippingCost()));
      doc.add(rightAligned("Total: " + order.getGrandTotal() + " " + order.getCurrency()));

      doc.close();
      return out.toByteArray();
    } catch (Exception e) {
      log.error("Failed to generate invoice PDF for order {}", order.getOrderNumber(), e);
      throw new IllegalStateException("invoice_generation_failed", e);
    }
  }

  private static void addHeader(PdfPTable table, String text) {
    var cell = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD)));
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    table.addCell(cell);
  }

  private static Paragraph rightAligned(String text) {
    var p = new Paragraph(text, new Font(Font.HELVETICA, 11));
    p.setAlignment(Element.ALIGN_RIGHT);
    return p;
  }

  private static String safe(String s) {
    return s == null ? "" : s;
  }
}
