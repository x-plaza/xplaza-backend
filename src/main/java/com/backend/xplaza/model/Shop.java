package com.backend.xplaza.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name="shops")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shop_id")
    private long id;

    @Column(name="shop_name")
    private String name;

    @Column(name="shop_description")
    private String description;

    @Column(name="shop_address")
    private String address;

    @Column(name="fk_location_id")
    private long location_id;

    public Shop() {}
}
