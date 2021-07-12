package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Table(name="admin_user_shop_link")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class AdminUserShopLink {
    @Id
    @ApiModelProperty(hidden=true)
    @Column(name="admin_user_id")
    private long id;

    @Column(name="shop_id")
    private long shop_id;

    public long getShop_id() {
        return shop_id;
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="admin_user_id",insertable = false,updatable = false)
    private AdminUser adminUser;

    public AdminUserShopLink() {}

}
