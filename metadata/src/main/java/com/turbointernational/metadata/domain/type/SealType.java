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
@Table(name="SEAL_TYPE", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class SealType implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @JsonView(View.Summary.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonView(View.Summary.class)
    @Column(nullable=false)
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
    
    public static SealType fromJsonToSealType(String json) {
        return new JSONDeserializer<SealType>().use(null, SealType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<SealType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<SealType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<SealType> fromJsonArrayToSealTypes(String json) {
        return new JSONDeserializer<List<SealType>>().use(null, ArrayList.class).use("values", SealType.class).deserialize(json);
    }
    //</editor-fold>
}
