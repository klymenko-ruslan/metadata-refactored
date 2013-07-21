// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.bom;

import com.turbointernational.metadata.domain.bom.BOMAlternative;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

privileged aspect BOMAlternative_Roo_Jpa_Entity {
    
    declare @type: BOMAlternative: @Entity;
    
    declare @type: BOMAlternative: @Table(name = "BOM_ALT_ITEM");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long BOMAlternative.id;
    
    @Version
    @Column(name = "version")
    private Integer BOMAlternative.version;
    
    public Long BOMAlternative.getId() {
        return this.id;
    }
    
    public void BOMAlternative.setId(Long id) {
        this.id = id;
    }
    
    public Integer BOMAlternative.getVersion() {
        return this.version;
    }
    
    public void BOMAlternative.setVersion(Integer version) {
        this.version = version;
    }
    
}
