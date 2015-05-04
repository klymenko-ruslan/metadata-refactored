package com.turbointernational.metadata.domain.type;
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

@Cacheable
@Entity
@Table(name = "PART_TYPE")
public class PartType implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private String name;
    
    /**
     * Used externally
     */
    @Column(nullable=false)
    private String value;
    
    @Column(name="magento_attribute_set")
    private String magentoAttributeSet;
    
    // TODO: Is this a duplicate of parentTypes?
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_part_type_id")
    private PartType parent;
    
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getMagentoAttributeSet() {
        return magentoAttributeSet;
    }
    
    public void setMagentoAttributeSet(String magentoName) {
        this.magentoAttributeSet = magentoName;
    }
    
    public PartType getParent() {
        return parent;
    }
    
    public void setParent(PartType parent) {
        this.parent = parent;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="json">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }
    
    public static PartType fromJsonToPartType(String json) {
        return new JSONDeserializer<PartType>().use(null, PartType.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<PartType> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<PartType> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<PartType> fromJsonArrayToPartTypes(String json) {
        return new JSONDeserializer<List<PartType>>().use(null, ArrayList.class).use("values", PartType.class).deserialize(json);
    }
    //</editor-fold>

}
