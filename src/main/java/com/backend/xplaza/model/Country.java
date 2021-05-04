package com.backend.xplaza.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name="countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="country_id")
    private long id;

    @Column(name="country_name")
    private String name;

    @Column(name="country_code")
    private String code;

    public Country() {}

    /*public Country(long id, String name, String code) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }*/
}