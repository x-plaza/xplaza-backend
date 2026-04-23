/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.marketing.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

/**
 * Marketing campaign.
 */
@Entity
@Table(name = "campaigns", indexes = {
    @Index(name = "idx_campaign_status", columnList = "status"),
    @Index(name = "idx_campaign_dates", columnList = "start_date, end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "campaign_id")
  private Long campaignId;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 30)
  private CampaignType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  @Builder.Default
  private CampaignStatus status = CampaignStatus.DRAFT;

  // Discount configuration
  @Enumerated(EnumType.STRING)
  @Column(name = "discount_type", length = 20)
  private DiscountType discountType;

  @Column(name = "discount_value", precision = 15, scale = 2)
  private BigDecimal discountValue;

  @Column(name = "max_discount", precision = 15, scale = 2)
  private BigDecimal maxDiscount;

  @Column(name = "min_purchase", precision = 15, scale = 2)
  private BigDecimal minPurchase;

  // Usage limits
  @Column(name = "total_uses_limit")
  private Integer totalUsesLimit;

  @Column(name = "per_customer_limit")
  @Builder.Default
  private Integer perCustomerLimit = 1;

  @Column(name = "current_uses")
  @Builder.Default
  private Integer currentUses = 0;

  // Dates
  @Column(name = "start_date", nullable = false)
  private Instant startDate;

  @Column(name = "end_date", nullable = false)
  private Instant endDate;

  // Targeting
  @Column(name = "target_customer_segments", columnDefinition = "TEXT")
  private String targetCustomerSegments;

  @Column(name = "target_categories", columnDefinition = "TEXT")
  private String targetCategories;

  @Column(name = "target_products", columnDefinition = "TEXT")
  private String targetProducts;

  @Column(name = "excluded_products", columnDefinition = "TEXT")
  private String excludedProducts;

  @Column(name = "target_brands", columnDefinition = "TEXT")
  private String targetBrands;

  // Display
  @Column(name = "banner_image_url", length = 500)
  private String bannerImageUrl;

  @Column(name = "thumbnail_url", length = 500)
  private String thumbnailUrl;

  @Column(name = "display_on_homepage")
  @Builder.Default
  private Boolean displayOnHomepage = false;

  @Column(name = "display_priority")
  @Builder.Default
  private Integer displayPriority = 0;

  // Stacking rules
  @Column(name = "stackable")
  @Builder.Default
  private Boolean stackable = false;

  @Column(name = "exclusive")
  @Builder.Default
  private Boolean exclusive = false;

  // Tracking
  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  @Builder.Default
  private Instant updatedAt = Instant.now();

  @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  private List<CampaignProduct> products = new ArrayList<>();

  public enum CampaignType {
    /** Percentage discount */
    PERCENTAGE_DISCOUNT,
    /** Fixed amount discount */
    FIXED_DISCOUNT,
    /** Buy X get Y free */
    BUY_X_GET_Y,
    /** Bundle deal */
    BUNDLE,
    /** Free shipping */
    FREE_SHIPPING,
    /** Flash sale */
    FLASH_SALE,
    /** Seasonal sale */
    SEASONAL,
    /** Clearance */
    CLEARANCE,
    /** Loyalty reward */
    LOYALTY,
    /** First purchase */
    FIRST_PURCHASE
  }

  public enum CampaignStatus {
    DRAFT,
    SCHEDULED,
    ACTIVE,
    PAUSED,
    ENDED,
    CANCELLED
  }

  public enum DiscountType {
    PERCENTAGE,
    FIXED_AMOUNT,
    FREE_ITEM,
    FREE_SHIPPING
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  /**
   * Check if campaign is currently active.
   */
  public boolean isActive() {
    if (status != CampaignStatus.ACTIVE) {
      return false;
    }
    Instant now = Instant.now();
    return now.isAfter(startDate) && now.isBefore(endDate);
  }

  /**
   * Check if campaign has available uses.
   */
  public boolean hasAvailableUses() {
    if (totalUsesLimit == null) {
      return true;
    }
    return currentUses < totalUsesLimit;
  }

  /**
   * Check if customer can use campaign.
   */
  public boolean canCustomerUse(int customerUseCount) {
    if (perCustomerLimit == null) {
      return true;
    }
    return customerUseCount < perCustomerLimit;
  }

  /**
   * Record campaign use.
   */
  public void recordUse() {
    this.currentUses++;
  }

  /**
   * Calculate the cash discount for a flat subtotal. Backwards-compatible shim
   * for callers that do not have shipping or line-item context. For percentage
   * and fixed-amount campaigns this is exact; for FREE_SHIPPING and BOGO/BUNDLE
   * the caller should use {@link #calculateDiscount(DiscountContext)} which can
   * see shipping cost and individual lines.
   */
  public BigDecimal calculateDiscount(BigDecimal subtotal) {
    return calculateDiscount(DiscountContext.of(subtotal));
  }

  /**
   * Rich discount calculation for all campaign/discount types, using the pre-tax
   * subtotal, the shipping cost and the cart line items the campaign targets.
   * Returns the total cash value to subtract from the order.
   *
   * <p>
   * Behaviour by type:
   * <ul>
   * <li>{@link DiscountType#PERCENTAGE}: percentage of subtotal.</li>
   * <li>{@link DiscountType#FIXED_AMOUNT}: fixed cash discount.</li>
   * <li>{@link DiscountType#FREE_SHIPPING}: returns the shipping cost so the
   * checkout/cart maths zero out shipping for the buyer.</li>
   * <li>{@link DiscountType#FREE_ITEM} combined with
   * {@link CampaignType#BUY_X_GET_Y}: applies a BOGO calculation on the targeted
   * SKUs (cheapest qualifying line is free per X bought).</li>
   * <li>{@link CampaignType#BUNDLE}: requires every targeted product to be
   * present in the cart; if so applies the campaign's discount value as a flat
   * amount (or {@code maxDiscount}, whichever is smaller).</li>
   * </ul>
   * Always honours {@link #minPurchase} and {@link #maxDiscount}.
   */
  public BigDecimal calculateDiscount(DiscountContext ctx) {
    BigDecimal subtotal = ctx.subtotal();
    if (subtotal == null || subtotal.signum() <= 0) {
      return BigDecimal.ZERO;
    }
    if (minPurchase != null && subtotal.compareTo(minPurchase) < 0) {
      return BigDecimal.ZERO;
    }

    BigDecimal discount = switch (effectiveDiscountType()) {
    case PERCENTAGE -> percentageDiscount(subtotal);
    case FIXED_AMOUNT -> discountValue == null ? BigDecimal.ZERO : discountValue;
    case FREE_SHIPPING -> ctx.shippingCost() == null ? BigDecimal.ZERO : ctx.shippingCost();
    case FREE_ITEM -> bogoDiscount(ctx.lineItems());
    };

    if (type == CampaignType.BUNDLE) {
      discount = bundleDiscount(ctx.lineItems());
    }

    if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
      discount = maxDiscount;
    }
    return discount.max(BigDecimal.ZERO);
  }

  /**
   * Projects {@link CampaignType} onto {@link DiscountType} when the type is
   * implicit (e.g. a {@code FREE_SHIPPING} campaign with no explicit
   * {@code discountType}).
   */
  private DiscountType effectiveDiscountType() {
    if (discountType != null) {
      return discountType;
    }
    return switch (type) {
    case FREE_SHIPPING -> DiscountType.FREE_SHIPPING;
    case BUY_X_GET_Y -> DiscountType.FREE_ITEM;
    case PERCENTAGE_DISCOUNT, FLASH_SALE, SEASONAL, CLEARANCE, LOYALTY, FIRST_PURCHASE -> DiscountType.PERCENTAGE;
    case FIXED_DISCOUNT, BUNDLE -> DiscountType.FIXED_AMOUNT;
    };
  }

  private BigDecimal percentageDiscount(BigDecimal subtotal) {
    if (discountValue == null) {
      return BigDecimal.ZERO;
    }
    return subtotal.multiply(discountValue).divide(java.math.BigDecimal.valueOf(100), 2,
        java.math.RoundingMode.HALF_UP);
  }

  /**
   * BOGO / Buy-X-Get-Y. The {@code discountValue} field holds the X (number of
   * items the customer must buy to get one free). Picks the cheapest qualifying
   * line across the cart so the buyer always gets the most-affordable freebie.
   *
   * <p>
   * Respects {@link #targetProducts}: when the campaign is scoped to specific
   * SKUs, only those cart lines are counted for the qty threshold <em>and</em> as
   * potential freebies. Lines outside the campaign scope are ignored so unrelated
   * products can never trip the BOGO or serve as the freebie.
   */
  private BigDecimal bogoDiscount(List<DiscountLine> lines) {
    if (lines == null || lines.isEmpty()) {
      return BigDecimal.ZERO;
    }
    List<DiscountLine> eligible = lines;
    if (targetProducts != null && !targetProducts.isBlank()) {
      var targetIds = new java.util.HashSet<>(parseTargetProductIds());
      if (targetIds.isEmpty()) {
        return BigDecimal.ZERO;
      }
      eligible = lines.stream()
          .filter(l -> l.productId() != null && targetIds.contains(l.productId()))
          .toList();
      if (eligible.isEmpty()) {
        return BigDecimal.ZERO;
      }
    }
    int x = (discountValue == null ? 1 : discountValue.intValue());
    if (x < 1) {
      x = 1;
    }
    int totalQty = eligible.stream().mapToInt(DiscountLine::quantity).sum();
    int freebies = totalQty / (x + 1);
    if (freebies <= 0) {
      return BigDecimal.ZERO;
    }
    BigDecimal cheapest = eligible.stream()
        .map(DiscountLine::unitPrice)
        .min(BigDecimal::compareTo)
        .orElse(BigDecimal.ZERO);
    return cheapest.multiply(BigDecimal.valueOf(freebies));
  }

  /**
   * BUNDLE. Every product id in {@code targetProducts} (CSV) must be present with
   * quantity ≥ 1; if so the bundle discount equals {@code discountValue}.
   */
  private BigDecimal bundleDiscount(List<DiscountLine> lines) {
    if (discountValue == null || targetProducts == null || targetProducts.isBlank()) {
      return BigDecimal.ZERO;
    }
    var requiredIds = parseTargetProductIds();
    if (requiredIds.isEmpty()) {
      return BigDecimal.ZERO;
    }
    if (lines == null || lines.isEmpty()) {
      return BigDecimal.ZERO;
    }
    var present = new java.util.HashSet<Long>();
    for (var l : lines) {
      if (l.quantity() > 0) {
        present.add(l.productId());
      }
    }
    return present.containsAll(requiredIds) ? discountValue : BigDecimal.ZERO;
  }

  private List<Long> parseTargetProductIds() {
    var out = new ArrayList<Long>();
    for (var raw : targetProducts.split(",")) {
      var s = raw.trim();
      if (s.isEmpty()) {
        continue;
      }
      try {
        out.add(Long.parseLong(s));
      } catch (NumberFormatException ignored) {
        // skip malformed entries
      }
    }
    return out;
  }

  /**
   * Cart context required to calculate any campaign type. Only the
   * {@code subtotal} is mandatory; other fields default to neutral values when
   * absent so legacy callers using {@link #calculateDiscount(BigDecimal)} get the
   * previous behaviour.
   */
  public record DiscountContext(
      BigDecimal subtotal,
      BigDecimal shippingCost,
      List<DiscountLine> lineItems
  ) {
    public static DiscountContext of(BigDecimal subtotal) {
      return new DiscountContext(subtotal, BigDecimal.ZERO, List.of());
    }
  }

  /**
   * Lightweight projection of a cart line — just what BOGO/BUNDLE need.
   */
  public record DiscountLine(
      Long productId,
      int quantity,
      BigDecimal unitPrice
  ) {
  }

  /**
   * Activate the campaign.
   */
  public void activate() {
    this.status = CampaignStatus.ACTIVE;
  }

  /**
   * Pause the campaign.
   */
  public void pause() {
    this.status = CampaignStatus.PAUSED;
  }

  /**
   * End the campaign.
   */
  public void end() {
    this.status = CampaignStatus.ENDED;
  }
}
