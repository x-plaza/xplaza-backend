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
public class AdminLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="admin_user_id")
    @ApiModelProperty(hidden=true)
    private Long id;

    private Boolean authentication;

    public void setAuthentication(Boolean auth) {
        this.authentication = auth;
    }

    @Embedded
    private AuthData authData;

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    @OneToMany(mappedBy = "adminLogin")
    private List<LoginUserShopList> shopList;

    public void setShopList(List<LoginUserShopList> shopList) {
        this.shopList = shopList;
    }

    @OneToMany(mappedBy = "adminLogin")
    private List<Permission> permissions;

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public AdminLogin() {}
}
