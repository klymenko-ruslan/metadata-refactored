package com.turbointernational.metadata.web.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * A lightweight DTO class to transfer main part's properties on the client side.
 *
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(ALWAYS)
public class PartDesc {

    @JsonView(View.Summary.class)
    private Long id; // part ID

    @JsonView(View.Summary.class)
    private String manufacturerPartNumber;

    public PartDesc() {
    }

    public PartDesc(Long id, String manufacturerPartNumber) {
        this.id = id;
        this.manufacturerPartNumber = manufacturerPartNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturerPartNumber() {
        return manufacturerPartNumber;
    }

    public void setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
    }

}
