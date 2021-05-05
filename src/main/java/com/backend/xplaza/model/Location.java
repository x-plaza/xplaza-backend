package com.backend.xplaza.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name="locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="location_id")
    private long id;

    @Column(name="location_name")
    private String name;

    @Column(name="fk_country_id")
    private long country_id;

    /*
    @ManyToOne
    @JoinColumn (name="fk_country_id")
    @JsonBackReference
    private Country country;
    */

    public Location() {}
}
