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
@Table(name="orders")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    @ApiModelProperty(hidden=true)
    private Long invoice_number;

    @Column(name="total_price")
    private Double total_price;

    @Column(name="discount_amount")
    private Double discount_amount;

    @Column(name="grand_total_price")
    private Double grand_total_price;

    @Column(name="delivery_address")
    private String delivery_address;

    @Column(name="fk_customer_id")
    private String customer_id;

    @Column(name="customer_name")
    private String customer_name;

    @Column(name="mobile_no")
    private String mobile_no;

    private String allotted_time;

    @Column(name="received_time")
    private Date received_time;

    public String getReceived_time() {
        if(received_time != null) return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(received_time);
        return null;
    }

    @Column(name="fk_shop_id")
    private Long shop_id;

    @Column(name="shop_name")
    private String shop_name;

    @Column(name="fk_status_id")
    private Long status_id;

    @Column(name="status_name")
    private String status_name;

    @Column(name="date_to_deliver")
    private Date date_to_deliver;

    public String getDate_to_deliver() {
        if(date_to_deliver != null) return new SimpleDateFormat("dd MMM yyyy").format(date_to_deliver);
        return null;
    }

    @Column(name="fk_currency_id")
    private Long currency_id;

    @Column(name="currency_name")
    private String currency_name;

    @Column(name="currency_sign")
    private String currency_sign;

    public OrderList(){}
}
