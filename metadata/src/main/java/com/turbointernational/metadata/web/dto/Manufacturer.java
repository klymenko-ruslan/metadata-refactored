package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetManufacturerResponse;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class Manufacturer {

    @JsonView({ View.Summary.class })
    private Long id;

    @JsonView({ View.Summary.class })
    private String name;

    public Manufacturer() {
    }

    public Manufacturer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Manufacturer from(GetManufacturerResponse o) {
        return new Manufacturer(o.getId(), o.getName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
