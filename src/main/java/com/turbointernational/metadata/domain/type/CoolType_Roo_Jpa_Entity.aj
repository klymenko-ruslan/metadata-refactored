// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.type.CoolType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

privileged aspect CoolType_Roo_Jpa_Entity {
    
    declare @type: CoolType: @Entity;
    
    declare @type: CoolType: @Table(name = "COOL_TYPE");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long CoolType.id;
    
    @Version
    @Column(name = "version")
    private Integer CoolType.version;
    
    public Long CoolType.getId() {
        return this.id;
    }
    
    public void CoolType.setId(Long id) {
        this.id = id;
    }
    
    public Integer CoolType.getVersion() {
        return this.version;
    }
    
    public void CoolType.setVersion(Integer version) {
        this.version = version;
    }
    
}
