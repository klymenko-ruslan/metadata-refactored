// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.bom;

import com.turbointernational.metadata.domain.bom.BOMAlternative;
import com.turbointernational.metadata.domain.bom.BOMItem;
import com.turbointernational.metadata.domain.part.Part;
import java.util.Set;

privileged aspect BOMItem_Roo_JavaBean {
    
    public Long BOMItem.getId() {
        return this.id;
    }
    
    public void BOMItem.setId(Long id) {
        this.id = id;
    }
    
    public Part BOMItem.getChild() {
        return this.child;
    }
    
    public void BOMItem.setChild(Part child) {
        this.child = child;
    }
    
    public Integer BOMItem.getQuantity() {
        return this.quantity;
    }
    
    public void BOMItem.setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Set<BOMAlternative> BOMItem.getAlternatives() {
        return this.alternatives;
    }
    
    public void BOMItem.setAlternatives(Set<BOMAlternative> alternatives) {
        this.alternatives = alternatives;
    }
    
}
