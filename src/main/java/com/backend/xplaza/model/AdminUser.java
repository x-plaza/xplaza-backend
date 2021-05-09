package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

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
    private long id;

    @Column(name="user_name")
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="salt")
    private String salt;

    @Column(name="fk_role_id")
    private long role_id;

    @Column(name="fk_shop_id")
    private long shop_id;

    public AdminUser() {}
}
