package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Data
@Embeddable
@AllArgsConstructor
@Table(name="orders_items")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderItemList {
    @Column(name="order_item_name")
    private String item_name;

    @Column(name="order_item_category")
    private String item_category;

    @Column(name="order_item_quantity")
    private long quantity;

    @Column(name="order_item_quantity_type")
    private String quantity_type;

    @Column(name="order_item_unit_price")
    private double unit_price;

    @Column(name="order_item_total_price")
    private double item_total_price;

    @Column(name="order_item_image")
    private String item_image;

    public OrderItemList() {}
}
