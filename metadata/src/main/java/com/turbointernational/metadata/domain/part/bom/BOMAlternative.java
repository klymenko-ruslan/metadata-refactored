package com.turbointernational.metadata.domain.part.bom;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.web.View;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;

@Entity
@Table(name = "bom_alt_item")
public class BOMAlternative implements Comparable<BOMAlternative>, Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "bom_id")
    private BOMItem bomItem;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bom_alt_header_id")
    @JsonView({View.Summary.class})
    private BOMAlternativeHeader header;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "part_id")
    @JsonView({View.SummaryWithBOMDetail.class})
    private Part part;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BOMItem getBomItem() {
        return bomItem;
    }

    public void setBomItem(BOMItem bomItem) {
        this.bomItem = bomItem;
    }

    public BOMAlternativeHeader getHeader() {
        return header;
    }

    public void setHeader(BOMAlternativeHeader header) {
        this.header = header;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="json">
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public static BOMAlternative fromJsonToBOMAlternative(String json) {
        return new JSONDeserializer<BOMAlternative>().use(null, BOMAlternative.class).deserialize(json);
    }

    public static String toJsonArray(Collection<BOMAlternative> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<BOMAlternative> collection, String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<BOMAlternative> fromJsonArrayToBOMAlternatives(String json) {
        return new JSONDeserializer<List<BOMAlternative>>().use(null, ArrayList.class).use("values", BOMAlternative.class).deserialize(json);
    }

    //</editor-fold>

    @Override
    public int compareTo(BOMAlternative t) {
        return ObjectUtils.compare(this.getHeader().getId(), t.getHeader().getId());
    }

}
