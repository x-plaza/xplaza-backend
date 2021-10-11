package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

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
    //@ApiModelProperty(hidden=true)
    private Long invoice_number;

    @Column(name="total_price")
    private Double total_price;

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }

    @Column(name="discount_amount")
    private Double discount_amount;

    public Double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(Double discount_amount) {
        this.discount_amount = discount_amount;
    }

    @Column(name="net_total")
    private Double net_total;

    public Double getNet_total() {
        return net_total;
    }

    public void setNet_total(Double net_total) {
        this.net_total = net_total;
    }

    @Column(name="grand_total_price")
    private Double grand_total_price;

    public void setGrand_total_price(Double grand_total_price) {
        this.grand_total_price = grand_total_price;
    }

    @Column(name="delivery_address")
    private String delivery_address;

    @Column(name="customer_id")
    private Long customer_id;

    @Column(name="shop_id")
    private Long shop_id;

    @Column(name="delivery_cost_id")
    private Long delivery_cost_id;

    @Column(name="fk_payment_type_id")
    private Long payment_type_id;

    @Column(name="fk_status_id")
    private Long status_id;

    @Column(name="coupon_id")
    private Long coupon_id;

    public Long getCoupon_id() {
        return coupon_id;
    }

    @Column(name="received_time")
    private Date received_time;

    @Column(name="date_to_deliver")
    private Date date_to_deliver;

    @Column(name="fk_currency_id")
    private Long currency_id;

    @Column(name="additional_info")
    private String additional_info;

    @Column(name="remarks")
    private String remarks;

    @Column(name="customer_name")
    private String customer_name;

    @Column(name="shop_name")
    private String shop_name;

    @Column(name="delivery_schedule_start")
    private Time delivery_schedule_start;

    @Column(name="delivery_schedule_end")
    private Time delivery_schedule_end;

    @Column(name="delivery_cost")
    private Double delivery_cost;

    public Double getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(Double delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    @Column(name="coupon_code")
    private String coupon_code;

    @Column(name="coupon_amount")
    private Double coupon_amount;

    public Double getCoupon_amount() {
        return coupon_amount;
    }

    public void setCoupon_amount(Double coupon_amount) {
        this.coupon_amount = coupon_amount;
    }

    @Column(name="mobile_no")
    private String mobile_no;

    public Order(){}
}
