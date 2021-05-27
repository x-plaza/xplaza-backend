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
public class ProductList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    @ApiModelProperty(hidden= true)
    private long id;

    @Column(name="product_name")
    private String name;

    @Column(name="product_description")
    private String description;

    @Column(name="fk_brand_id")
    private long brand_id;

    @Column(name="brand_name")
    private String brand_name;

    @Column(name="fk_category_id")
    private long category_id;

    @Column(name="category_name")
    private String category_name;

    @Column(name="fk_product_var_type_id")
    private long product_var_type_id;

    @Column(name="var_type_name")
    private String product_var_type_name;

    private long product_var_type_option;

    @Column(name="product_buying_price")
    private float buying_price;

    @Column(name="product_selling_price")
    private float selling_price;

    @Column(name="fk_currency_id")
    private long currency_id;

    @Column(name="currency_name")
    private String currency_name;

    @Column(name="fk_shop_id")
    private long shop_id;

    @Column(name="shop_name")
    private String shop_name;

    @Column(name="quantity")
    private Long quantity;

    public ProductList() {}
}
