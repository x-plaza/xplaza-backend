package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name="coupon_shop_list")
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class CouponShopList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden= true)
    @JsonIgnore
    private Long id;

    private Long shop_id;

    public Long getShop_id() {
        return shop_id;
    }

    private String shop_name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="coupon_id",insertable = false,updatable = false)
    private CouponDetails couponDetails;

    public CouponShopList() {}
}
