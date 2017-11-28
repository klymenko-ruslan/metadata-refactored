package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import flexjson.JSONSerializer;

public class Interchange extends PartGroup {

    // <editor-fold defaultstate="collapsed" desc="Properties">

    public Interchange() {
        // Don't remove. It can be used during deserealization of DTOs.
    }

    public Interchange(Long id, Part[] parts) {
        this.id = id;
        this.parts = parts;
    }

    @JsonView({ View.Summary.class })
    public boolean isAlone() {
        return parts.length == 0;
    }
    // </editor-fold>

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

}
