// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.other.Manufacturer;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Version;

privileged aspect Manufacturer_Roo_Jpa_Entity {
    
    declare @type: Manufacturer: @Entity;
    
    @Version
    @Column(name = "version")
    private Integer Manufacturer.version;
    
    public Integer Manufacturer.getVersion() {
        return this.version;
    }
    
    public void Manufacturer.setVersion(Integer version) {
        this.version = version;
    }
    
}
