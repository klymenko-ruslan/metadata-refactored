// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain;

import com.turbointernational.metadata.domain.Interchange;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

privileged aspect Interchange_Roo_Jpa_Entity {
    
    declare @type: Interchange: @Entity;
    
    declare @type: Interchange: @Table(name = "INTERCHANGE_HEADER");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Interchange.id;
    
    @Version
    @Column(name = "version")
    private Integer Interchange.version;
    
    public Long Interchange.getId() {
        return this.id;
    }
    
    public void Interchange.setId(Long id) {
        this.id = id;
    }
    
    public Integer Interchange.getVersion() {
        return this.version;
    }
    
    public void Interchange.setVersion(Integer version) {
        this.version = version;
    }
    
}
