package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name="coupon_shop_link")
@IdClass(CouponShopLinkId.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class CouponShopLink {
    @Id
    @ApiModelProperty(hidden=true)
    @Column(name="coupon_id")
    private Long coupon_id;

    public void setCoupon_id(Long coupon_id) {
        this.coupon_id = coupon_id;
    }

    @Id
    @Column(name="shop_id")
    private Long shop_id;

    public Long getShop_id() {
        return shop_id;
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="coupon_id",insertable = false,updatable = false)
    private Coupon coupon;

    public CouponShopLink() {}
}
