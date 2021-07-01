package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@Table(name="orders")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    @ApiModelProperty(hidden=true)
    private long id;

    @Column(name="total_price")
    private double total_price;

    @Column(name="discount_amount")
    private double discount_amount;

    @Column(name="grand_total_price")
    private double grand_total_price;

    @Column(name="delivery_address")
    private String delivery_address;

    @Column(name="received_time")
    private LocalDateTime received_time;

    @Column(name="fk_customer_id")
    private long customer_id;

    @Column(name="fk_shop_id")
    private long shop_id;

    @Column(name="fk_delivery_schedule_id")
    private long delivery_schedule_id;

    @Column(name="fk_delivery_cost_id")
    private long delivery_cost_id;

    @Column(name="fk_payment_type_id")
    private long payment_type_id;

    @Column(name="fk_status_id")
    private long status_id;

    //@Column(name="fk_delivery_id")
    //private long delivery_id;

    @Column(name="fk_coupon_id")
    private long coupon_id;

    @Column(name="date_to_deliver")
    private LocalDate date_to_deliver;

    public Order(){}
}
