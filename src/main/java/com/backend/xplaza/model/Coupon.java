package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name="coupons")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_id")
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(name="coupon_code")
    private String coupon_code;

    public String getCoupon_code() {
        return coupon_code;
    }

    @Column(name="coupon_amount")
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    @Column(name="max_coupon_amount")
    private Double max_amount;

    public Double getMax_amount() {
        return max_amount;
    }

    @Column(name="fk_currency_id")
    private Long currency_id;

    public Long getCurrency_id() {
        return currency_id;
    }

    @Column(name="fk_discount_type_id")
    private Long discount_type_id;

    public Long getDiscount_type_id() {
        return discount_type_id;
    }

    @Column(name="coupon_start_date")
    private Date start_date;

    public Date getStart_date() {
        return start_date;
    }

    @Column(name="coupon_end_date")
    private Date end_date;

    public Date getEnd_date() {
        return end_date;
    }

    @Column(name="is_active")
    private Boolean is_active;

    public Boolean getIs_active() {
        return is_active;
    }

    @Column(name="min_shopping_amount")
    private Double min_shopping_amount;

    public Double getMin_shopping_amount() {
        return min_shopping_amount;
    }

    @OneToMany(mappedBy = "coupon")
    private List<CouponShopLink> couponShopLinks;

    public List<CouponShopLink> getCouponShopLinks() {
        return couponShopLinks;
    }

    public void setCouponShopLinks(List<CouponShopLink> couponShopLinks) {
        this.couponShopLinks = couponShopLinks;
    }

    public Coupon() {}
}
