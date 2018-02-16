package com.turbointernational.metadata.web.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@JsonInclude(ALWAYS)
public class KitType {

    @JsonView({ View.Summary.class })
    private Long id;

    @JsonView({ View.Summary.class })
    private String name;

    public KitType() {
    }

    public KitType(Long id, String name) {
        this.id = id;
        this.name = name;
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
