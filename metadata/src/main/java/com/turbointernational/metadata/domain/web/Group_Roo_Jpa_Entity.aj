// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.web;

import com.turbointernational.metadata.domain.web.Group;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Group_Roo_Jpa_Entity {
    
    declare @type: Group: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Group.id;
    
    @Version
    @Column(name = "version")
    private Integer Group.version;
    
    public Long Group.getId() {
        return this.id;
    }
    
    public void Group.setId(Long id) {
        this.id = id;
    }
    
    public Integer Group.getVersion() {
        return this.version;
    }
    
    public void Group.setVersion(Integer version) {
        this.version = version;
    }
    
}
