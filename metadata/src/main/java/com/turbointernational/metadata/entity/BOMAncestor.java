package com.turbointernational.metadata.entity;

import com.turbointernational.metadata.entity.part.Part;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class BOMAncestor implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    private Part part;
    
    private Part ancestor;
    
    private Integer distance;
    
    private String type;

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
    //</editor-fold>turn ObjectUtils.compare(this.id, o.id);

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}