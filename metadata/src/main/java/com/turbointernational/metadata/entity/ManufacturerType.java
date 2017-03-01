package com.turbointernational.metadata.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Cacheable
@Entity
@Table(name = "manfr_type",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class ManufacturerType implements Serializable {

    private static final long serialVersionUID = -1124175033958260950L;

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @JsonView(View.Summary.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonView(View.Summary.class)
    @Column(name = "name", nullable = false)
    private String name;

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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="json">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static ManufacturerType fromJsonToManufacturerType(String json) {
        return new JSONDeserializer<ManufacturerType>().use(null, ManufacturerType.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ManufacturerType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ManufacturerType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ManufacturerType> fromJsonArrayToManufacturerTypes(String json) {
        return new JSONDeserializer<List<ManufacturerType>>().use(null, ArrayList.class).use("values", ManufacturerType.class).deserialize(json);
    }
    //</editor-fold>
}
