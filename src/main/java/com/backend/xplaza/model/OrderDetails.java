package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@Table(name="orders")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    @ApiModelProperty(hidden= true)
    private long id;

    @Column(name="total_price")
    private double total_price;

    @Column(name="discount_amount")
    private double discount_amount;

    @Column(name="grand_total_price")
    private double grand_total_price;

    @Column(name="delivery_address")
    private String delivery_address;

    @Column(name="customer_name")
    private String customer_name;

    @Column(name="mobile_no")
    private String mobile_no;

    @Column(name="fk_shop_id")
    private long shop_id;

    @Column(name="shop_name")
    private String shop_name;

    @Column(name="fk_delivery_schedule_id")
    private long delivery_schedule_id;

    private String allotted_time;

    @Column(name="received_time")
    private LocalDateTime received_time;

    @Column(name="fk_delivery_cost_id")
    private long delivery_cost_id;

    @Column(name="delivery_cost")
    private long delivery_cost;

    @Column(name="fk_payment_type_id")
    private long payment_type_id;

    @Column(name="payment_type_name")
    private long payment_type_name;

    @Column(name="fk_status_id")
    private long status_id;

    @Column(name="status_name")
    private long status_name;

    @Column(name="fk_delivery_id")
    private long delivery_id;

    @Column(name="person_name")
    private long delivery_person;

    @Column(name="contact_no")
    private long contact_no;

    @Column(name="fk_coupon_id")
    private long coupon_id;

    @Column(name="coupon_code")
    private long coupon_code;

    @Column(name="coupon_amount")
    private long coupon_amount;

    @Embedded
    private OrderItemList orderItemList;

    public OrderDetails(){}
}
