package com.turbointernational.metadata.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Table(name = "turbo_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(
                name = "findTurboTypesByManufacturerId",
                query = "SELECT o FROM TurboType o JOIN o.manufacturer " +
                        "WHERE o.manufacturer.id = :manufacturerId ORDER BY o.name"
        ),
        @NamedQuery(
                name="findTurboTypesByManufacturerIdAndName",
                query = "SELECT o FROM TurboType o JOIN o.manufacturer " +
                        "WHERE o.manufacturer.id = :manufacturerId AND o.name = :name"
        ),
        @NamedQuery(
                name = "numTurboTypesOfManufacturer",
                query = "select count(tt) from TurboType tt where tt.manufacturer.id=:manufacturerId"
        )
})
public class TurboType implements Comparable<TurboType>, Serializable {

    private static final long serialVersionUID = 4173140550967854600L;

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @OneToOne
    @JoinColumn(name = "manfr_id", nullable = false)
    @JsonView(View.Summary.class)
    private Manufacturer manufacturer;

    @Column(nullable = false)
    @JsonView(View.Summary.class)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields)
                .exclude("*.class").serialize(this);
    }

    public static TurboType fromJsonToTurboType(String json) {
        return new JSONDeserializer<TurboType>().use(null, TurboType.class).deserialize(json);
    }

    public static String toJsonArray(Collection<TurboType> collection) {
        return new JSONSerializer()
                .include("manufacturer.id")
                .exclude("manufacturer.*")
                .exclude("*.class")
                .serialize(collection);
    }

    public static String toJsonArray(Collection<TurboType> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields)
                .exclude("*.class")
                .serialize(collection);
    }

    public static Collection<TurboType> fromJsonArrayToTurboTypes(String json) {
        return new JSONDeserializer<List<TurboType>>().use(null, ArrayList.class).use("values", TurboType.class).deserialize(json);
    }

    //</editor-fold>

    @Override
    public int compareTo(TurboType o) {
        return this.name.compareTo(o.getName());
    }
}
