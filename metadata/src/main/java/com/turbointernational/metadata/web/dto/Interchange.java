package com.turbointernational.metadata.web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.service.GraphDbService.GetInterchangeResponse;
import com.turbointernational.metadata.util.View;

import flexjson.JSONSerializer;

public class Interchange extends PartGroup {

    // <editor-fold defaultstate="collapsed" desc="Properties">

    public Interchange(Long id, Part[] parts) {
        this.id = id;
        this.parts = parts;
    }

    public static Interchange from(PartDao partDao, GetInterchangeResponse o) {
        Long[] partIds = o.getParts();
        int n = partIds.length;
        Part[] parts = new Part[n];
        for (int i = 0; i < n; i++) {
            parts[i] = Part.from(partDao, partIds[i]);
        }
        return new Interchange(o.getHeaderId(), parts);
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
