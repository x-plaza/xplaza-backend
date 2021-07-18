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
@Table(name="top_customer")
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TopCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @ApiModelProperty(hidden= true)
    private Long id;

    @Column(name="customer_id")
    private Long customer_id;

    @Column(name="customer_name")
    private String name;

    @Column(name="total_order_amount")
    private Double total_order_amount;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="shop_id",insertable = false,updatable = false)
    private Dashboard dashboard;

    public TopCustomer(){}
}
