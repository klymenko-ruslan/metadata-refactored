// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain;

import com.turbointernational.metadata.domain.Manufacturer;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Manufacturer_Roo_Jpa_Entity {
    
    declare @type: Manufacturer: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Manufacturer.id;
    
    @Version
    @Column(name = "version")
    private Integer Manufacturer.version;
    
    public Long Manufacturer.getId() {
        return this.id;
    }
    
    public void Manufacturer.setId(Long id) {
        this.id = id;
    }
    
    public Integer Manufacturer.getVersion() {
        return this.version;
    }
    
    public void Manufacturer.setVersion(Integer version) {
        this.version = version;
    }
    
}
