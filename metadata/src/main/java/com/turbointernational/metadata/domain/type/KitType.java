package com.turbointernational.metadata.domain.type;

import com.fasterxml.jackson.annotation.JsonView;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Cacheable
@Entity
@Table(name="kit_type", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class KitType implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @JsonView(View.Summary.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonView(View.Summary.class)
    @Column(name = "name", nullable=false)
    private String name;

    @Column(name = "import_pk")
    private Long importPK;

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

    public Long getImportPK() {
        return importPK;
    }

    public void setImportPK(Long importPK) {
        this.importPK = importPK;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="json">
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static KitType fromJsonToKitType(String json) {
        return new JSONDeserializer<KitType>().use(null, KitType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<KitType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<KitType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<KitType> fromJsonArrayToKitTypes(String json) {
        return new JSONDeserializer<List<KitType>>().use(null, ArrayList.class).use("values", KitType.class).deserialize(json);
    }
    //</editor-fold>
}
