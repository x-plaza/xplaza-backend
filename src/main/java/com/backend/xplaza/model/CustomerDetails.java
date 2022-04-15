package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.text.SimpleDateFormat;
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
    private String postcode;

    @Column(name="area")
    private String area;

    @Column(name="city")
    private String city;

    @Column(name="country")
    private String country;

    @Column(name="mobile_no")
    private String mobile_no;

    @ApiModelProperty(hidden=true)
    @Column(name="email")
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Column(name="date_of_birth")
    private Date date_of_birth;

    public String getDate_of_birth() {
        if(date_of_birth != null) return new SimpleDateFormat("dd MMM yyyy").format(date_of_birth);
        return null;
    }
    @JsonIgnore
    @ApiModelProperty(hidden=true)
    @Column(name="otp")
    private String otp;

    public String getOtp() {
        return otp;
    }

    @JsonIgnore
    @ApiModelProperty(hidden=true)
    @Column(name="salt")
    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @JsonIgnore
    @ApiModelProperty(hidden=true)
    @Column(name="password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerDetails() {}

}
