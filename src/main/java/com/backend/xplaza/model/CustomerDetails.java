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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Column(name="date_of_birth")
    private Date date_of_birth;

    @Column(name="otp")
    private String otp;

    public String getOtp() {
        return otp;
    }

    @Column(name="salt")
    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

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
