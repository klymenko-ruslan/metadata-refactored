// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.security;

import com.turbointernational.metadata.domain.security.Group;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

privileged aspect Group_Roo_Jpa_Entity {
    
    declare @type: Group: @Entity;
    
    declare @type: Group: @Table(name = "GROUPS");
    
    @Version
    @Column(name = "version")
    private Integer Group.version;
    
    public Integer Group.getVersion() {
        return this.version;
    }
    
    public void Group.setVersion(Integer version) {
        this.version = version;
    }
    
}
