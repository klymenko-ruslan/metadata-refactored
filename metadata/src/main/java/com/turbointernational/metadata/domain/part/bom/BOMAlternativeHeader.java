package com.turbointernational.metadata.domain.part.bom;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BOM_ALT_HEADER")
public class BOMAlternativeHeader implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;
    
    @Column(nullable=false)
    @JsonView({View.Summary.class})
    private String name;
    
    @JsonView({View.Summary.class})
    private String description;
    
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static BOMAlternativeHeader fromJsonToBOMAlternativeHeader(String json) {
        return new JSONDeserializer<BOMAlternativeHeader>().use(null, BOMAlternativeHeader.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<BOMAlternativeHeader> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<BOMAlternativeHeader> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BOMAlternativeHeader> fromJsonArrayToBOMAlternativeHeaders(String json) {
        return new JSONDeserializer<List<BOMAlternativeHeader>>().use(null, ArrayList.class).use("values", BOMAlternativeHeader.class).deserialize(json);
    }
    //</editor-fold>
}
