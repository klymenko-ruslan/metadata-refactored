// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.Manufacturer;
import com.turbointernational.metadata.domain.type.TurboType;

privileged aspect TurboType_Roo_JavaBean {
    
    public Manufacturer TurboType.getManufacturer() {
        return this.manufacturer;
    }
    
    public void TurboType.setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String TurboType.getName() {
        return this.name;
    }
    
    public void TurboType.setName(String name) {
        this.name = name;
    }
    
    public Long TurboType.getImportPk() {
        return this.importPk;
    }
    
    public void TurboType.setImportPk(Long importPk) {
        this.importPk = importPk;
    }
    
}
