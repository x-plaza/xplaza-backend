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
public class OrderItemList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_item_id")
    @ApiModelProperty(hidden= true)
    private long id;

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

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="fk_order_id",insertable = false,updatable = false)
    private OrderDetails orderDetails;

    public OrderItemList() {}
}
