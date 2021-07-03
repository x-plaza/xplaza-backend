package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @ApiModelProperty(hidden= true)
    private long id;

    @Embedded
    private Revenue revenue;

    @OneToMany(mappedBy = "dashboard")
    private List<TopCustomer> topCustomers;

    @OneToMany(mappedBy = "dashboard")
    private List<TopProduct> topProducts;

    @OneToMany(mappedBy = "dashboard")
    private List<ProductToStock> productToStocks;

    public Dashboard(){}
}
