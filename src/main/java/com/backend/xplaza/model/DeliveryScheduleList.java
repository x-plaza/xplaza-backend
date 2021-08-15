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
@Table(name="day_names")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class DeliveryScheduleList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="day_id")
    @ApiModelProperty(hidden=true)
    private Long day_id;

    @Column(name="day_name")
    private String day_name;

    @OneToMany(mappedBy = "deliveryScheduleList")
    private List<DeliverySchedule> delivery_schedules;

    public DeliveryScheduleList() {}
}
