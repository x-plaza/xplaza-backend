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
@Table(name="order_items")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_item_id")
    //@ApiModelProperty(hidden=true)
    private Long id;

    @Column(name="fk_order_id")
    private Long order_id;

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public Long getOrder_id() {
        return order_id;
    }

    @Column(name="product_id")
    private Long product_id;

    public Long getProduct_id() {
        return product_id;
    }

    @Column(name="product_selling_price")
    private Double product_selling_price;

    public Double getProduct_selling_price() {
        return product_selling_price;
    }

    public void setProduct_selling_price(Double product_selling_price) {
        this.product_selling_price = product_selling_price;
    }

    @Column(name="order_item_name")
    private String item_name;

    @Column(name="order_item_var_type_name")
    private String item_var_type_name;

    @Column(name="order_item_var_type_value")
    private Long item_var_type_value;

    @Column(name="order_item_category")
    private String item_category;

    @Column(name="order_item_quantity")
    private Long quantity;

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Column(name="order_item_quantity_type")
    private String quantity_type;

    public void setQuantity_type(String quantity_type) {
        this.quantity_type = quantity_type;
    }

    @Column(name="order_item_unit_price")
    private Double unit_price;

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

    @ApiModelProperty(hidden=true)
    @Column(name="order_item_total_price")
    private Double item_total_price;

    public Double getItem_total_price() {
        return item_total_price;
    }

    public void setItem_total_price(Double item_total_price) {
        this.item_total_price = item_total_price;
    }

    @Column(name="order_item_image")
    private String item_image;

    @Column(name="fk_currency_id")
    private Long currency_id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="fk_order_id",insertable = false,updatable = false)
    private OrderPlace orderPlace;

//    public OrderItem(OrderItem oi){
//        OrderItem orderItem = new OrderItem();
//        orderItem.id = oi.id;
//        orderItem.order_id = oi.order_id;
//        orderItem.product_id = oi.product_id;
//        orderItem.item_name = oi.item_name;
//        orderItem.item_var_type_name = oi.item_var_type_name;
//        orderItem.item_var_type_value = oi.item_var_type_value;
//        orderItem.item_category = oi.item_category;
//        orderItem.quantity = oi.quantity;
//        orderItem.quantity_type = oi.quantity_type;
//        orderItem.unit_price = oi.unit_price;
//        orderItem.item_total_price = oi.item_total_price;
//        orderItem.item_image = oi.item_image;
//        orderItem.currency_id = oi.currency_id;
//    }

    public OrderItem(){}
}
