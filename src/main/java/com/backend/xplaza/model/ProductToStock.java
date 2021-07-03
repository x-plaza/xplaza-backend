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
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ProductToStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    @ApiModelProperty(hidden= true)
    private long id;

    @Column(name="product_name")
    private String name;

    @Column(name="remaining_unit")
    private double remaining_unit;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="id",insertable = false,updatable = false)
    private Dashboard dashboard;

    public ProductToStock(){}
}
