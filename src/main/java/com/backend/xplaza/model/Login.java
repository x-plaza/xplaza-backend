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
@Table(name="login")
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="admin_user_id")
    @ApiModelProperty(hidden=true)
    private long id;

    private boolean authentication;

    public void setAuthentication(boolean auth) {
        this.authentication = auth;
    }

    @Embedded
    private AuthData authData;

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    @OneToMany(mappedBy = "login")
    private List<LoginUserShopList> shopList;

    public void setShopList(List<LoginUserShopList> shopList) {
        this.shopList = shopList;
    }

    @OneToMany(mappedBy = "login")
    private List<Permission> permissions;

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Login() {}
}
