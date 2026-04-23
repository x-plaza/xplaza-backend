/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.review.domain.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Product Review submitted by a customer.
 * 
 * Features: - Multiple rating dimensions (overall, quality, value, shipping) -
 * Verified purchase badge - Images and videos - Helpful votes - Vendor
 * responses - Moderation workflow
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

  @Id
  @Column(name = "review_id")
  @Builder.Default
  private UUID reviewId = UUID.randomUUID();

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "variant_id")
  private UUID variantId;

  @Column(name = "order_id")
  private UUID orderId;

  @Column(name = "order_item_id")
  private UUID orderItemId;

  @Column(name = "customer_id", nullable = false)
  private Long customerId;

  @Column(name = "shop_id", nullable = false)
  private Long shopId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20)
  @Builder.Default
  private ReviewStatus status = ReviewStatus.PENDING;

  // Multi-dimensional ratings (1-5)
  @Column(name = "rating_overall", nullable = false)
  private Integer ratingOverall;

  @Column(name = "rating_quality")
  private Integer ratingQuality;

  @Column(name = "rating_value")
  private Integer ratingValue;

  @Column(name = "rating_shipping")
  private Integer ratingShipping;

  // Review content
  @Column(name = "title", length = 255)
  private String title;

  @Column(name = "body", columnDefinition = "TEXT")
  private String body;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "pros")
  private String[] pros;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(name = "cons")
  private String[] cons;

  // Votes (denormalized for performance)
  @Column(name = "helpful_votes")
  @Builder.Default
  private Integer helpfulVotes = 0;

  @Column(name = "unhelpful_votes")
  @Builder.Default
  private Integer unhelpfulVotes = 0;

  @Column(name = "is_verified_purchase")
  @Builder.Default
  private Boolean isVerifiedPurchase = false;

  @Column(name = "is_anonymous")
  @Builder.Default
  private Boolean isAnonymous = false;

  // Moderation
  @Column(name = "moderated_by")
  private Long moderatedBy;

  @Column(name = "moderated_at")
  private Instant moderatedAt;

  @Column(name = "reject_reason", columnDefinition = "TEXT")
  private String rejectReason;

  @Column(name = "flag_reason", columnDefinition = "TEXT")
  private String flagReason;

  // Timestamps
  @Column(name = "published_at")
  private Instant publishedAt;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  @Builder.Default
  private Instant updatedAt = Instant.now();

  // Related entities
  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  private List<ReviewImage> images = new ArrayList<>();

  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @Builder.Default
  private List<ReviewVideo> videos = new ArrayList<>();

  @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private ReviewResponse vendorResponse;

  public enum ReviewStatus {
    PENDING,
    APPROVED,
    REJECTED,
    FLAGGED
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public void approve(Long moderatorId) {
    this.status = ReviewStatus.APPROVED;
    this.moderatedBy = moderatorId;
    this.moderatedAt = Instant.now();
    this.publishedAt = Instant.now();
  }

  public void reject(Long moderatorId, String reason) {
    this.status = ReviewStatus.REJECTED;
    this.moderatedBy = moderatorId;
    this.moderatedAt = Instant.now();
    this.rejectReason = reason;
  }

  public void flag(String reason) {
    this.status = ReviewStatus.FLAGGED;
    this.flagReason = reason;
  }

  public void addHelpfulVote() {
    this.helpfulVotes = (helpfulVotes != null ? helpfulVotes : 0) + 1;
  }

  public void addUnhelpfulVote() {
    this.unhelpfulVotes = (unhelpfulVotes != null ? unhelpfulVotes : 0) + 1;
  }

  public void removeHelpfulVote() {
    if (helpfulVotes != null && helpfulVotes > 0) {
      this.helpfulVotes--;
    }
  }

  public void removeUnhelpfulVote() {
    if (unhelpfulVotes != null && unhelpfulVotes > 0) {
      this.unhelpfulVotes--;
    }
  }

  public Double getHelpfulnessPercentage() {
    int total = (helpfulVotes != null ? helpfulVotes : 0) + (unhelpfulVotes != null ? unhelpfulVotes : 0);
    if (total == 0) {
      return null;
    }
    return (helpfulVotes != null ? helpfulVotes : 0) * 100.0 / total;
  }

  public void addImage(ReviewImage image) {
    images.add(image);
    image.setReview(this);
  }

  public void addVideo(ReviewVideo video) {
    videos.add(video);
    video.setReview(this);
  }

  public void setVendorResponse(ReviewResponse response) {
    this.vendorResponse = response;
    response.setReview(this);
  }
}
