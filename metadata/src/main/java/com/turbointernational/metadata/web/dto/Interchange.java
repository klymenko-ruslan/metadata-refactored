package com.turbointernational.metadata.web.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetInterchangeResponse;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetPartResponse;
import com.turbointernational.metadata.util.View;

import flexjson.JSONSerializer;

public class Interchange implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Properties">

    private static final long serialVersionUID = -4602654865509535526L;

    @JsonView({ View.Summary.class })
    private final Long id;

    @JsonView({ View.Summary.class })
    private final Part[] parts;

    public Interchange(Long id, Part[] parts) {
        this.id = id;
        this.parts = parts;
    }

    public static Interchange from(GetInterchangeResponse o) {
        GetPartResponse[] restParts = o.getParts();
        int n = restParts.length;
        Part[] parts = new Part[n];
        for(int i = 0; i < n; i++) {
            parts[i] = Part.from(restParts[i]);
        }
        return new Interchange(o.getHeaderId(), parts);
    }

    public Long getId() {
        return id;
    }

    public Part[] getParts() {
        return parts;
    }

    @JsonView({ View.Summary.class })
    public boolean isAlone() {
        return parts.length == 0;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    /*
     * public String toJson(String[] fields) { return new
     * JSONSerializer().include(fields).exclude("*.class").serialize(this); }
     *
     * public static Interchange fromJsonToInterchange(String json) { return new
     * JSONDeserializer<Interchange>().use(null,
     * Interchange.class).deserialize(json); }
     *
     * public static String toJsonArray(Collection<Interchange> collection) {
     * return new JSONSerializer().exclude("*.class").serialize(collection); }
     *
     * public static String toJsonArray(Collection<Interchange> collection,
     * String[] fields) { return new
     * JSONSerializer().include(fields).exclude("*.class").serialize(collection)
     * ; }
     *
     * public static Collection<Interchange> fromJsonArrayToInterchanges(String
     * json) { return new JSONDeserializer<List<Interchange>>().use(null,
     * ArrayList.class).use("values", Interchange.class).deserialize(json); }
     */
    // </editor-fold>

}
