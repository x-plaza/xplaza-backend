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
@Table(name="product_variation_types")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ProductVarType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_var_type_id")
    private long id;

    @Column(name="var_type_name")
    private String name;

    @Column(name="var_type_description")
    private String description;

    public ProductVarType() {}
}
