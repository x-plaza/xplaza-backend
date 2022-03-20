package com.backend.xplaza.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name="platform_info")
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class PlatformInfo {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden=true)
    private String name;

    public String getName() {
        return name;
    }

    @ApiModelProperty(hidden=true)
    private String invoice;

    public String getInvoice() {
        return invoice;
    }

    private String cell_no;

    public String getCell_no() {
        return cell_no;
    }

    private String additional_info;

    public String getAdditional_info() {
        return additional_info;
    }

    private String banner_image;

    public String getBanner_image() {
        return banner_image;
    }

    private String banner_image_path;

    public String getBanner_image_path() {
        return banner_image_path;
    }

    public PlatformInfo() {}
}
