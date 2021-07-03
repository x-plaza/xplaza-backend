package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

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
    private long id;

    @Column(name="coupon_code")
    private String coupon_code;

    @Column(name="coupon_amount")
    private float amount;

    @Column(name="max_coupon_amount")
    private float max_amount;

    @Column(name="fk_currency_id")
    private long currency_id;

    @Column(name="fk_discount_type_id")
    private long discount_type_id;

    @Column(name="coupon_start_date")
    private Date start_date;

    @Column(name="coupon_end_date")
    private Date end_date;

    @Column(name="is_active")
    private boolean is_active;

    public Coupon() {}
}
