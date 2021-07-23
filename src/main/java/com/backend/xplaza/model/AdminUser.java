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
@Table(name="admin_users")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="admin_user_id")
    private Long id;

    public Long getId() {
        return id;
    }

    @Column(name="full_name")
    private String full_name;

    public String getFull_name() {
        return full_name;
    }

    @Column(name="user_name")
    private String user_name;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Column(name="password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ApiModelProperty(hidden= true)
    @Column(name="salt")
    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name="fk_role_id")
    private Long role_id;

    public Long getRole_id() {
        return role_id;
    }

    /*@ApiModelProperty(hidden= true)
    @Column(name="is_confirmed")
    private Boolean is_confirmed;

    public void setIs_confirmed(Boolean is_confirmed) {
        this.is_confirmed = is_confirmed;
    }

    public Boolean getIs_confirmed() {
        return is_confirmed;
    }*/

    @Column(name="confirmation_code")
    private String confirmation_code;

    public String getConfirmation_code() {
        return confirmation_code;
    }

    @OneToMany(mappedBy = "adminUser")
    private List<AdminUserShopLink> adminUserShopLinks;

    public List<AdminUserShopLink> getAdminUserShopLinks() {
        return adminUserShopLinks;
    }

    public void setAdminUserShopLinks(List<AdminUserShopLink> adminUserShopLinks) {
        this.adminUserShopLinks = adminUserShopLinks;
    }

    public AdminUser() {}
}
