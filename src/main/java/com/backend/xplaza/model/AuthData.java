package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Embeddable;

@Data
@Embeddable
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class AuthData {
    private String user_name;
    private long role_id;
    private String role_name;
    private long shop_id;
    private String shop_name;
    private boolean authentication;

    public void setAuthentication(boolean auth) {
        this.authentication = auth;
    }

    public AuthData() {}
}
