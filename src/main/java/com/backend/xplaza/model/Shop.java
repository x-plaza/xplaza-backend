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
@Table(name="shops")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shop_id")
    //@ApiModelProperty(hidden=true)
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(name="shop_name")
    private String name;

    @Column(name="shop_owner")
    private String owner;

    @Column(name="shop_address")
    private String address;

    @Column(name="shop_description")
    private String description;

    @Column(name="fk_location_id")
    private Long location_id;

    public Shop() {}
}
