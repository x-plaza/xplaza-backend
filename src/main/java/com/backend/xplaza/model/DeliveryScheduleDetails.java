package com.backend.xplaza.model;

import com.backend.xplaza.common.SqlTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Time;
import java.text.SimpleDateFormat;

@Data
@Entity
@AllArgsConstructor
@Table(name="delivery_schedules")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class DeliveryScheduleDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delivery_schedule_id")
    //@ApiModelProperty(hidden=true)
    private Long delivery_schedule_id;

    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    @Column(name="delivery_schedule_start")
    private Time delivery_schedule_start;

    public String getDelivery_schedule_start() {
        if(delivery_schedule_start != null) return new SimpleDateFormat("HH:mm").format(delivery_schedule_start);
        return null;
    }

    @JsonFormat(pattern = "HH:mm")
    @JsonDeserialize(using = SqlTimeDeserializer.class)
    @Column(name="delivery_schedule_end")
    private Time delivery_schedule_end;

    public String getDelivery_schedule_end() {
        if(delivery_schedule_end != null) return new SimpleDateFormat("HH:mm").format(delivery_schedule_end);
        return null;
    }

    @Column(name="fk_day_id")
    private Long day_id;

    @Column(name="day_name")
    private String day_name;

    public DeliveryScheduleDetails() {}
}
