// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.bom.BOMItem;
import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import java.util.List;

privileged aspect Part_Roo_JavaBean {
    
    public Long Part.getId() {
        return this.id;
    }
    
    public void Part.setId(Long id) {
        this.id = id;
    }
    
    public String Part.getManufacturerPartNumber() {
        return this.manufacturerPartNumber;
    }
    
    public void Part.setManufacturerPartNumber(String manufacturerPartNumber) {
        this.manufacturerPartNumber = manufacturerPartNumber;
    }
    
    public Manufacturer Part.getManufacturer() {
        return this.manufacturer;
    }
    
    public void Part.setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public String Part.getName() {
        return this.name;
    }
    
    public void Part.setName(String name) {
        this.name = name;
    }
    
    public String Part.getDescription() {
        return this.description;
    }
    
    public void Part.setDescription(String description) {
        this.description = description;
    }
    
    public Boolean Part.getInactive() {
        return this.inactive;
    }
    
    public void Part.setInactive(Boolean inactive) {
        this.inactive = inactive;
    }
    
    public List<Interchange> Part.getInterchanges() {
        return this.interchanges;
    }
    
    public void Part.setInterchanges(List<Interchange> interchanges) {
        this.interchanges = interchanges;
    }
    
    public List<BOMItem> Part.getBom() {
        return this.bom;
    }
    
    public void Part.setBom(List<BOMItem> bom) {
        this.bom = bom;
    }
    
}
