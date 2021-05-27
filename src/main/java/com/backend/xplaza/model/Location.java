package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name="locations")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="location_id")
    @ApiModelProperty(hidden= true)
    private long id;

    @Column(name="location_name")
    private String name;

    @Column(name="fk_city_id")
    private long city_id;

    /*@JsonManagedReference
    @OneToMany(mappedBy = "location")
    private List<Shop> shops;*/

    public Location() {}
}
