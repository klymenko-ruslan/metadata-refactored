// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain;

import com.turbointernational.metadata.domain.AttributeType;
import com.turbointernational.metadata.domain.Part;
import com.turbointernational.metadata.domain.PartAttribute;

privileged aspect PartAttribute_Roo_JavaBean {
    
    public Part PartAttribute.getPart() {
        return this.part;
    }
    
    public void PartAttribute.setPart(Part part) {
        this.part = part;
    }
    
    public AttributeType PartAttribute.getAttributeType() {
        return this.attributeType;
    }
    
    public void PartAttribute.setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }
    
    public String PartAttribute.getValue() {
        return this.value;
    }
    
    public void PartAttribute.setValue(String value) {
        this.value = value;
    }
    
}
