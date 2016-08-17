package com.turbointernational.metadata.domain.part.bom;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "bom", uniqueConstraints = @UniqueConstraint(columnNames = {"parent_part_id", "child_part_id"}))
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "class", include = JsonTypeInfo.As.PROPERTY, defaultImpl = BOMItem.class)
public class BOMItem implements Comparable<BOMItem>, Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "parent_part_id", nullable = false)
    private Part parent;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "child_part_id", nullable = false)
    @JsonView({View.SummaryWithBOMDetail.class})
    private Part child;

    @Column(name = "quantity", nullable = false)
    @JsonView({View.Summary.class})
    private Integer quantity;

    @OneToMany(mappedBy = "bomItem", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id")
    @JsonView({View.SummaryWithBOMDetail.class})
    private Set<BOMAlternative> alternatives = new TreeSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Part getParent() {
        return parent;
    }

    public void setParent(Part parent) {
        this.parent = parent;
    }

    public Part getChild() {
        return child;
    }

    public void setChild(Part child) {
        this.child = child;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Set<BOMAlternative> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(Set<BOMAlternative> alternatives) {
        this.alternatives.clear();
        this.alternatives.addAll(alternatives);
    }

    public List<Part> getTIAlternates() {
        List<Part> tiAlts = new ArrayList<Part>();

        for (BOMAlternative alt : alternatives) {
            if (Manufacturer.TI_ID.equals(alt.getPart().getManufacturer().getId())) {
                tiAlts.add(alt.getPart());
            }
        }

        return tiAlts;
    }

    @PrePersist
    @PreUpdate
    public void validate() {
        if (ObjectUtils.equals(child.getId(), parent.getId())) {
            throw new IllegalStateException("Child cannot be it's own parent.");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="json">
    public String toJson() {
        return new JSONSerializer().transform(
                new HibernateTransformer(), BOMItem.class).
                include("parent.id", "parent.name", "parent.version", "parent.manufacturerPartNumber",
                        "parent.interchange", "parent.description", "parent.inactive").
                include("parent.partType.id", "parent.partType.magentoAttributeSet", "parent.partType.name", "parent.partType.value").
                exclude("parent.partType.*").
                include("parent.manufacturer.id", "parent.manufacturer.name").
                include("parent.manufacturer.type.id", "parent.manufacturer.type.name").
                exclude("parent.manufacturer.type.*").
                include("parent.interchange.alone", "parent.interchange.description",
                        "parent.interchange.id", "parent.interchange.name").
                exclude("parent.interchange.*").
                exclude("parent.*", "*.class").
                serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static BOMItem fromJsonToBOMItem(String json) {
        return new JSONDeserializer<BOMItem>().use(null, BOMItem.class).deserialize(json);
    }

    public static String toJsonArray(Collection<BOMItem> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<BOMItem> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<BOMItem> fromJsonArrayToBOMItems(String json) {
        return new JSONDeserializer<List<BOMItem>>().use(null, ArrayList.class).use("values", BOMItem.class).deserialize(json);
    }
    //</editor-fold>

    @Override
    public int compareTo(BOMItem o) {
        return ObjectUtils.compare(this.id, o.id);
    }

}
