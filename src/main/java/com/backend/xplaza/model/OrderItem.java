package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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

    public Long getOrder_id() {
        return order_id;
    }

    @Column(name="product_id")
    private Long product_id;

    public Long getProduct_id() {
        return product_id;
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

    public OrderItem(){}
}
