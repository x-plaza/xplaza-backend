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
@Table(name="product_discounts")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ProductDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_discount_id")
    //@ApiModelProperty(hidden=true)
    private Long id;

    @Column(name="fk_product_id")
    private Long product_id;

    public Long getProduct_id() {
        return product_id;
    }

    @Column(name="fk_discount_type_id")
    private Long discount_type_id;

    public Long getDiscount_type_id() {
        return discount_type_id;
    }

    @Column(name="discount_amount")
    private Double discount_amount;

    public Double getDiscount_amount() {
        return discount_amount;
    }

    @Column(name="fk_currency_id")
    private Long currency_id;

    @Column(name="discount_start_date")
    private Date start_date;

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    @Column(name="discount_end_date")
    private Date end_date;

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public ProductDiscount() {}
}
