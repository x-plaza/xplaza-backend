package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderResponse {
    @Id
    private String invoice_number;

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    private Double grand_total_price;

    public void setGrand_total_price (Double grand_total_price) {
        this.grand_total_price = grand_total_price;
    }

    public OrderResponse(){}
}
