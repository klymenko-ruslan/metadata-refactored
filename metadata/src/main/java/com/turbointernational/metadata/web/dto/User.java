package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class User {

    @JsonView({ View.Summary.class })
    private Long id;

    @JsonView({ View.Summary.class })
    private String name;

    public User() {
    }

    public User(Long id, String name) {
        this.setId(id);
        this.setName(name);
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
