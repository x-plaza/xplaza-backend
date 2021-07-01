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
@Table(name="products")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    @ApiModelProperty(hidden=true)
    private long id;

    @Column(name="product_name")
    private String name;

    @Column(name="product_description")
    private String description;

    @Column(name="fk_brand_id")
    private long brand_id;

    @Column(name="fk_category_id")
    private long category_id;

    @Column(name="fk_product_var_type_id")
    private long product_var_type_id;

    private long product_var_type_option;

    @Column(name="product_buying_price")
    private float buying_price;

    @Column(name="product_selling_price")
    private float selling_price;

    @Column(name="fk_currency_id")
    private long currency_id;

    @Column(name="fk_shop_id")
    private long shop_id;

    @Column(name="quantity")
    private Long quantity;

    public Product() {}
}

