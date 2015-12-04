package com.turbointernational.metadata.domain.part;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Sets;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="interchange_header")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Interchange.class)
public class Interchange implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;
    
    @Column
    @JsonView({View.Summary.class})
    private String name;
    
    @JsonView({View.Summary.class})
    private String description;
    
    @OneToMany(mappedBy = "interchange", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
    @JsonView(View.SummaryWithInterchangeParts.class)
    private Set<Part> parts = Sets.newTreeSet();
    
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<Part> getParts() {
        return parts;
    }
    
    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }
    
    @JsonView({View.Summary.class})
    public boolean isAlone() {
        return parts.size() == 1;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Serialization">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static Interchange fromJsonToInterchange(String json) {
        return new JSONDeserializer<Interchange>().use(null, Interchange.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<Interchange> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<Interchange> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Interchange> fromJsonArrayToInterchanges(String json) {
        return new JSONDeserializer<List<Interchange>>().use(null, ArrayList.class).use("values", Interchange.class).deserialize(json);
    }
    //</editor-fold>
    
}
