// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.type.TurboType;

privileged aspect TurboModel_Roo_JavaBean {
    
    public Long TurboModel.getId() {
        return this.id;
    }
    
    public void TurboModel.setId(Long id) {
        this.id = id;
    }
    
    public String TurboModel.getName() {
        return this.name;
    }
    
    public void TurboModel.setName(String name) {
        this.name = name;
    }
    
    public TurboType TurboModel.getType() {
        return this.type;
    }
    
    public void TurboModel.setType(TurboType type) {
        this.type = type;
    }
    
}
