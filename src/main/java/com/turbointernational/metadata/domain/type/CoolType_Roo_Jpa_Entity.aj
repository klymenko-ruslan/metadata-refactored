// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.type.CoolType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

privileged aspect CoolType_Roo_Jpa_Entity {
    
    declare @type: CoolType: @Entity;
    
    declare @type: CoolType: @Table(name = "COOL_TYPE");
    
    @Version
    @Column(name = "version")
    private Integer CoolType.version;
    
    public Integer CoolType.getVersion() {
        return this.version;
    }
    
    public void CoolType.setVersion(Integer version) {
        this.version = version;
    }
    
}
