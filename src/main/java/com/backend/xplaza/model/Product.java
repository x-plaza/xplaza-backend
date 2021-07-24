package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

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
    //@ApiModelProperty(hidden=true)
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(name="product_name")
    private String name;

    @Column(name="product_description")
    private String description;

    @Column(name="fk_brand_id")
    private Long brand_id;

    @Column(name="fk_category_id")
    private Long category_id;

    @Column(name="fk_product_var_type_id")
    private Long product_var_type_id;

    private Double product_var_type_value;

    @Column(name="product_buying_price")
    private Double buying_price;

    @Column(name="product_selling_price")
    private Double selling_price;

    @Column(name="fk_currency_id")
    private Long currency_id;

    @Column(name="fk_shop_id")
    private Long shop_id;

    @Column(name="quantity")
    private Long quantity;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImage;

    public List<ProductImage> getProductImage() {
        return productImage;
    }

    public Product() {}
}

