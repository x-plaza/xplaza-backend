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

    @Column(name="grand_total_price")
    private Double grand_total_price;

    public void setGrand_total_price(Double grand_total_price) {
        this.grand_total_price = grand_total_price;
    }

    @Column(name="delivery_address")
    private String delivery_address;

    @Column(name="received_time")
    private Date received_time;

    @Column(name="fk_customer_id")
    private Long customer_id;

    @Column(name="fk_shop_id")
    private Long shop_id;

    @Column(name="fk_delivery_schedule_id")
    private Long delivery_schedule_id;

    @Column(name="fk_delivery_cost_id")
    private Long delivery_cost_id;

    @Column(name="fk_payment_type_id")
    private Long payment_type_id;

    @Column(name="fk_status_id")
    private Long status_id;

    @Column(name="fk_coupon_id")
    private Long coupon_id;

    @Column(name="date_to_deliver")
    private Date date_to_deliver;

    @Column(name="additional_info")
    private String additional_info;

    @Column(name="remarks")
    private String remarks;

    public Order(){}
}
