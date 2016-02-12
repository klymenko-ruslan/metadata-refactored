package com.turbointernational.metadata.domain.other;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.type.ManufacturerType;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Cacheable
@Entity
@Table(name="MANFR", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class Manufacturer implements Serializable {
    
    public static final Long TI_ID = 11L;
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;
    
    @Column(nullable=false)
    @JsonView({View.Summary.class})
    private String name;
    
    @OneToOne
    @JoinColumn(name="manfr_type_id", nullable=false)
    @JsonView({View.Detail.class})
    private ManufacturerType type;

    @Column(name = "import_pk")
    private Long importPK;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_manfr_id")
    @JsonView({View.Detail.class})
    private Manufacturer parent;
    
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
    
    public ManufacturerType getType() {
        return type;
    }
    
    public void setType(ManufacturerType type) {
        this.type = type;
    }

    public Long getImportPK() {
        return importPK;
    }

    public void setImportPK(Long importPK) {
        this.importPK = importPK;
    }

    public Manufacturer getParent() {
        return parent;
    }
    
    public void setParent(Manufacturer parent) {
        this.parent = parent;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static Manufacturer fromJsonToManufacturer(String json) {
        return new JSONDeserializer<Manufacturer>().use(null, Manufacturer.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<Manufacturer> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<Manufacturer> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Manufacturer> fromJsonArrayToManufacturers(String json) {
        return new JSONDeserializer<List<Manufacturer>>().use(null, ArrayList.class).use("values", Manufacturer.class).deserialize(json);
    }
    //</editor-fold>
    
}
