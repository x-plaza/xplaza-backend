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
@Table(name="product_images")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ProductImageList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_image_id")
    @ApiModelProperty(hidden=true)
    private Long id;

    @Column(name="product_image_name")
    private String name;

    @Column(name="product_image_path")
    private String path;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "fk_product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
    private ProductList productList;

    public ProductImageList() { }
}
