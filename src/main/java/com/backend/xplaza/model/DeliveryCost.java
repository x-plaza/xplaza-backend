package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Entity
@Table(name="delivery_costs")
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class DeliveryCost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delivery_cost_id")
    //@ApiModelProperty(hidden=true)
    private Long id;

    @Column(name="delivery_slab_start_range")
    private Double start_range;

    public Double getStart_range() {
        return start_range;
    }

    @Column(name="delivery_slab_end_range")
    private Double end_range;

    public Double getEnd_range() {
        return end_range;
    }

    @Column(name="delivery_cost")
    private Double cost;

    public Double getCost() {
        return cost;
    }

    @Column(name="fk_currency_id")
    private Long currency_id;

    public DeliveryCost() {}
}
