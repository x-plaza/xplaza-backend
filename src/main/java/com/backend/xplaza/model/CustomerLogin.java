package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name="customers")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class CustomerLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="customer_id")
    //@ApiModelProperty(hidden=true)
    private Long id;

    @Column(name="customer_name")
    private String name;

    @Column(name="email")
    private String email;

    private boolean authentication;

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public CustomerLogin() {}
}
