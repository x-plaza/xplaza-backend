package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Table(name="coupons")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class CouponList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="coupon_id")
    @ApiModelProperty(hidden=true)
    private long id;

    @Column(name="coupon_code")
    private String coupon_code;

    @Column(name="is_active")
    private boolean is_active;

    @Column(name="coupon_amount")
    private float amount;

    @Column(name="max_coupon_amount")
    private float max_amount;

    @Column(name="fk_currency_id")
    private long currency_id;

    @Column(name="currency_name")
    private String currency_name;

    @Column(name="fk_discount_type_id")
    private long discount_type_id;

    @Column(name="discount_type_name")
    private String discount_type_name;

    @Column(name="coupon_start_date")
    private Date start_date;

    public String getStart_date() {
        if(start_date != null) return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(start_date);
        return null;
    }

    @Column(name="coupon_end_date")
    private Date end_date;

    public String getEnd_date() {
        if(end_date != null) return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(end_date);
        return null;
    }

    public CouponList() {}
}
