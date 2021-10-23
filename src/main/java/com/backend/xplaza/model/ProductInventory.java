package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name="products")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    //@ApiModelProperty(hidden=true)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="product_name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="quantity")
    private Long max_available_quantity;

    public void setMax_available_quantity(Long max_available_quantity) {
        this.max_available_quantity = max_available_quantity;
    }

    public Long getMax_available_quantity() {
        return max_available_quantity;
    }

    private boolean is_available;

    public boolean getIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public ProductInventory() {}
}
