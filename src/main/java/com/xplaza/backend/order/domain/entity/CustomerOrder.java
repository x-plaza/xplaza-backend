/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.order.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * CustomerOrder represents a confirmed purchase from a customer.
 *
 * An order is created when a customer completes checkout. It captures: - The
 * items purchased (from cart) - Payment information - Shipping details -
 * Pricing at time of purchase
 */
@Entity
@Table(name = "customer_orders", indexes = {
    @Index(name = "idx_cust_orders_customer", columnList = "customer_id"),
    @Index(name = "idx_cust_orders_shop", columnList = "shop_id"),
    @Index(name = "idx_cust_orders_status", columnList = "status"),
    @Index(name = "idx_cust_orders_created", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class CustomerOrder {

  @Id
  @Column(name = "order_id")
  @Builder.Default
  private UUID orderId = UUID.randomUUID();

  @Column(name = "order_number", nullable = false, unique = true, length = 50)
  private String orderNumber;

  @Column(name = "customer_id", nullable = false)
  private Long customerId;

  @Column(name = "shop_id", nullable = false)
  private Long shopId;

  @Column(name = "cart_id")
  private UUID cartId;

  @Column(name = "parent_order_id")
  private UUID parentOrderId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  @Builder.Default
  private OrderStatus status = OrderStatus.PENDING;

  // Pricing snapshot at time of order
  @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
  private BigDecimal subtotal;

  @Column(name = "discount_amount", precision = 15, scale = 2)
  @Builder.Default
  private BigDecimal discountAmount = BigDecimal.ZERO;

  @Column(name = "shipping_cost", precision = 15, scale = 2)
  @Builder.Default
  private BigDecimal shippingCost = BigDecimal.ZERO;

  @Column(name = "tax_amount", precision = 15, scale = 2)
  @Builder.Default
  private BigDecimal taxAmount = BigDecimal.ZERO;

  @Column(name = "grand_total", nullable = false, precision = 15, scale = 2)
  private BigDecimal grandTotal;

  @Column(name = "currency", length = 3)
  @Builder.Default
  private String currency = "USD";

  // Shipping information (snapshot from customer address)
  @Column(name = "shipping_address_id")
  private Long shippingAddressId;

  @Column(name = "shipping_first_name", length = 100)
  private String shippingFirstName;

  @Column(name = "shipping_last_name", length = 100)
  private String shippingLastName;

  @Column(name = "shipping_phone", length = 20)
  private String shippingPhone;

  @Column(name = "shipping_address_line1", length = 255)
  private String shippingAddressLine1;

  @Column(name = "shipping_address_line2", length = 255)
  private String shippingAddressLine2;

  @Column(name = "shipping_city", length = 100)
  private String shippingCity;

  @Column(name = "shipping_state", length = 100)
  private String shippingState;

  @Column(name = "shipping_postal_code", length = 20)
  private String shippingPostalCode;

  @Column(name = "shipping_country_code", length = 2)
  private String shippingCountryCode;

  @Column(name = "shipping_instructions", columnDefinition = "TEXT")
  private String shippingInstructions;

  // Billing information
  @Column(name = "billing_address_id")
  private Long billingAddressId;

  @Column(name = "billing_same_as_shipping")
  @Builder.Default
  private Boolean billingSameAsShipping = true;

  // Delivery scheduling
  @Column(name = "requested_delivery_date")
  private LocalDate requestedDeliveryDate;

  @Column(name = "delivery_slot_start")
  private LocalTime deliverySlotStart;

  @Column(name = "delivery_slot_end")
  private LocalTime deliverySlotEnd;

  @Column(name = "estimated_delivery_date")
  private LocalDate estimatedDeliveryDate;

  @Column(name = "actual_delivery_date")
  private LocalDate actualDeliveryDate;

  // Payment info
  @Column(name = "payment_type_id")
  private Long paymentTypeId;

  @Column(name = "payment_method", length = 50)
  private String paymentMethod;

  @Column(name = "payment_status", length = 30)
  @Builder.Default
  private String paymentStatus = "PENDING";

  @Column(name = "payment_transaction_id")
  private UUID paymentTransactionId;

  // Coupon info
  @Column(name = "coupon_id")
  private Long couponId;

  @Column(name = "coupon_code", length = 50)
  private String couponCode;

  @Column(name = "coupon_discount_amount", precision = 15, scale = 2)
  private BigDecimal couponDiscountAmount;

  // Additional info
  @Column(name = "customer_notes", columnDefinition = "TEXT")
  private String customerNotes;

  @Column(name = "internal_notes", columnDefinition = "TEXT")
  private String internalNotes;

  // Timestamps
  @Column(name = "placed_at")
  private Instant placedAt;

  @Column(name = "confirmed_at")
  private Instant confirmedAt;

  @Column(name = "shipped_at")
  private Instant shippedAt;

  @Column(name = "delivered_at")
  private Instant deliveredAt;

  @Column(name = "cancelled_at")
  private Instant cancelledAt;

  @Column(name = "cancellation_reason", length = 255)
  private String cancellationReason;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  @Builder.Default
  private Instant updatedAt = Instant.now();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  @NotAudited
  private List<CustomerOrderItem> items = new ArrayList<>();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  @NotAudited
  private List<OrderStatusHistory> statusHistory = new ArrayList<>();

  public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED,
    RETURN_REQUESTED,
    RETURNING,
    RETURNED,
    PARTIALLY_RETURNED
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public void addItem(CustomerOrderItem item) {
    items.add(item);
    item.setOrder(this);
  }

  public int getTotalItemCount() {
    return items.stream()
        .mapToInt(CustomerOrderItem::getQuantity)
        .sum();
  }

  public void changeStatus(OrderStatus newStatus, String reason, String changedBy) {
    OrderStatusHistory history = OrderStatusHistory.builder()
        .order(this)
        .previousStatus(this.status)
        .newStatus(newStatus)
        .reason(reason)
        .changedBy(changedBy)
        .changedAt(Instant.now())
        .build();
    statusHistory.add(history);
    this.status = newStatus;
    updateStatusTimestamp(newStatus);
  }

  private void updateStatusTimestamp(OrderStatus status) {
    Instant now = Instant.now();
    switch (status) {
    case CONFIRMED -> this.confirmedAt = now;
    case SHIPPED -> this.shippedAt = now;
    case DELIVERED -> this.deliveredAt = now;
    case CANCELLED -> this.cancelledAt = now;
    default -> {
    }
    }
  }

  public boolean canBeCancelled() {
    return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
  }

  public boolean canBeModified() {
    return status == OrderStatus.PENDING;
  }

  public String getFormattedShippingAddress() {
    StringBuilder sb = new StringBuilder();
    sb.append(shippingFirstName).append(" ").append(shippingLastName).append("\n");
    sb.append(shippingAddressLine1);
    if (shippingAddressLine2 != null && !shippingAddressLine2.isBlank()) {
      sb.append(", ").append(shippingAddressLine2);
    }
    sb.append("\n").append(shippingCity);
    if (shippingState != null && !shippingState.isBlank()) {
      sb.append(", ").append(shippingState);
    }
    sb.append(" ").append(shippingPostalCode);
    sb.append("\n").append(shippingCountryCode);
    return sb.toString();
  }
}
