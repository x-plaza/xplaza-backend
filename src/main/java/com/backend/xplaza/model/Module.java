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
@Table(name="modules")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="module_id")
    @ApiModelProperty(hidden= true)
    private long id;

    @Column(name="module_name")
    private String name;

    @Column(name="module_description")
    private String description;

    public Module() {}
}
