package com.turbointernational.metadata.domain.part.bom;
import com.turbointernational.metadata.domain.part.Part;
import javax.persistence.CascadeType;
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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Configurable;

public class BOMAncestor implements Comparable<BOMAncestor> {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private Long id;
    
    private Part part;
    
    private Part ancestor;
    
    private Integer distance;
    
    private String type;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Part getAncestor() {
        return ancestor;
    }

    public void setAncestor(Part ancestor) {
        this.ancestor = ancestor;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    //</editor-fold>

    @Override
    public int compareTo(BOMAncestor o) {
        return ObjectUtils.compare(this.id, o.id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
