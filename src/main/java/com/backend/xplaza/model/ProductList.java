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
    @ApiModelProperty(hidden=true)
    private Long id;

    @Column(name="product_name")
    private String name;

    @Column(name="product_description")
    private String description;

    @Column(name="fk_brand_id")
    private Long brand_id;

    @Column(name="brand_name")
    private String brand_name;

    @Column(name="fk_category_id")
    private Long category_id;

    @Column(name="category_name")
    private String category_name;

    @Column(name="fk_product_var_type_id")
    private Long product_var_type_id;

    @Column(name="var_type_name")
    private String product_var_type_name;

    private Double product_var_type_value;

    @Column(name="product_buying_price")
    private Double buying_price;

    @Column(name="product_selling_price")
    private Double selling_price;

    @Column(name="fk_currency_id")
    private Long currency_id;

    @Column(name="currency_name")
    private String currency_name;

    @Column(name="fk_shop_id")
    private Long shop_id;

    @Column(name="shop_name")
    private String shop_name;

    @Column(name="quantity")
    private Long quantity;

    public ProductList() {}
}
