package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

@Data
@Entity
@AllArgsConstructor
@Table(name="confirmation_tokens")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private Long token_id;

    @Column(name="confirmation_token")
    private String confirmation_token;

    public String getConfirmation_token() {
        return confirmation_token;
    }

    @Column(name="user_email")
    private String email;

    public String getEmail() {
        return email;
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;

    public ConfirmationToken(String email, String type) {
        Random rnd = new Random();
        int number = rnd.nextInt(99999999);
        if(type == "OTP") confirmation_token = String.format("%08d", number);
        else confirmation_token = String.format("%06d", number);
        this.email = email;
        created_date = new Date();
    }

    /*
    @OneToOne(targetEntity = AdminUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "admin_user_id")
    private AdminUser adminUser;

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public ConfirmationToken(AdminUser adminUser) {
        this.adminUser = adminUser;
        created_date = new Date();
        confirmation_token = UUID.randomUUID().toString();
    }*/

    public ConfirmationToken() {}
}
