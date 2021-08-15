package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Time;

@Data
@Entity
@AllArgsConstructor
@Table(name="delivery_schedules")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class DeliverySchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delivery_schedule_id")
    //@ApiModelProperty(hidden=true)
    private Long delivery_schedule_id;

    @Column(name="delivery_schedule_start")
    private Time delivery_schedule_start;

    @Column(name="delivery_schedule_end")
    private Time delivery_schedule_end;

    @Column(name="fk_day_id")
    private Long day_id;

    @ApiModelProperty(hidden=true)
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="fk_day_id", insertable = false,updatable = false)
    private DeliveryScheduleList deliveryScheduleList;

    public DeliverySchedule() {}
}
