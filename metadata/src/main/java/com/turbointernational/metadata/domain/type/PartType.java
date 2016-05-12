package com.turbointernational.metadata.domain.type;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

@Cacheable
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllPartTypes", query = "FROM PartType AS pt ORDER BY pt.name"),
        @NamedQuery(name = "findPartTypeByValue", query = "FROM PartType AS pt WHERE pt.value=:value")
})
@Table(name = "PART_TYPE")
public class PartType implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(View.Summary.class)
    private String name;

    @Column(name = "import_pk")
    private Long importPK;

    /**
     * Used externally
     */
    @Column(nullable = false, unique = true)
    @JsonView(View.Summary.class)
    private String value;

    @Column(name = "magento_attribute_set")
    @JsonView(View.Summary.class)
    private String magentoAttributeSet;

    // TODO: Is this a duplicate of parentTypes?
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_part_type_id")
    @JsonView({View.Detail.class})
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

    public Long getImportPK() {
        return importPK;
    }

    public void setImportPK(Long importPK) {
        this.importPK = importPK;
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
