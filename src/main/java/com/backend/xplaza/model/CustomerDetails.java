package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Table(name="customers")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class CustomerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    //@ApiModelProperty(hidden=true)
    private Long id;

    @Column(name="first_name")
    private String first_name;

    @Column(name="last_name")
    private String last_name;

    @Column(name="house_no")
    private String house_no;

    @Column(name="street_name")
    private String street_name;

    @Column(name="postcode")
    private Long postcode;

    @Column(name="area")
    private String area;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="mobile_no")
    private String mobile_no;

    @Column(name="email")
    private String email;

    @Column(name="date_of_birth")
    private Date date_of_birth;

    @Column(name="password")
    private String password;

    @Column(name="salt")
    private String salt;

    @Column(name="otp")
    private String otp;

    @Column(name="created_at")
    private Date created_at;

    @Column(name="updated_at")
    private Date updated_at;

    public CustomerDetails() {}

}
