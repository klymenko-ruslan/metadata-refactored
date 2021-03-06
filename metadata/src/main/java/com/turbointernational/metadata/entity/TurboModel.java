package com.turbointernational.metadata.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Cacheable
@Entity
@Table(name = "turbo_model")
@NamedQueries({
  @NamedQuery(
    name="findTurboModelsByTurboTypeId",
    query = "SELECT o FROM TurboModel o JOIN o.turboType tt WHERE o.turboType.id = :turboTypeId ORDER BY o.name"
  ),
  @NamedQuery(
    name="findTurboModelByTurboTypeIdAndName",
    query = "SELECT o FROM TurboModel o JOIN o.turboType tt " +
            "WHERE o.turboType.id = :turboTypeId AND o.name = :name"
  )
})
public class TurboModel implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Properties">

    private static final long serialVersionUID = 5354494538542055708L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(View.Summary.class)
    private String name;

    @JsonView(View.Detail.class)
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "turbo_type_id")
    private TurboType turboType;

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

    public TurboType getTurboType() {
        return turboType;
    }

    public void setTurboType(TurboType turboType) {
        this.turboType = turboType;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static TurboModel fromJsonToTurboModel(String json) {
        return new JSONDeserializer<TurboModel>().use(null, TurboModel.class).deserialize(json);
    }

    public static String toJsonArray(Collection<TurboModel> collection) {
        return new JSONSerializer()
                .include("id")
                .include("name")
                .include("version")
                .include("turboType.id")
                .include("turboType.version")
                .exclude("*")
                .serialize(collection);
    }

    public static String toJsonArray(Collection<TurboModel> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<TurboModel> fromJsonArrayToTurboModels(String json) {
        return new JSONDeserializer<List<TurboModel>>().use(null, ArrayList.class).use("values", TurboModel.class).deserialize(json);
    }

    //</editor-fold>

}
