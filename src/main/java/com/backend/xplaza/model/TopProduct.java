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
@Table(name="top_product")
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TopProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden= true)
    private Long id;

    @Column(name="product_id")
    private Long product_id;

    @Column(name="product_name")
    private String name;

    @Column(name="monthly_sold_unit")
    private Double monthly_sold_unit;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="shop_id",insertable = false,updatable = false)
    private Dashboard dashboard;

    public TopProduct(){}
}
