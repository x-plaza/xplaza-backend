package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
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

    @Column(name="fk_product_id")
    private Long product_id;

    @Column(name="order_item_name")
    private String item_name;

    @Column(name="order_item_category")
    private String item_category;

    @Column(name="order_item_quantity")
    private Long quantity;

    @Column(name="order_item_quantity_type")
    private String quantity_type;

    @Column(name="order_item_unit_price")
    private Double unit_price;

    @Column(name="order_item_total_price")
    private Double item_total_price;

    @Column(name="order_item_image")
    private String item_image;

    @Column(name="fk_currency_id")
    private Long currency_id;

    public OrderItem(){}
}
