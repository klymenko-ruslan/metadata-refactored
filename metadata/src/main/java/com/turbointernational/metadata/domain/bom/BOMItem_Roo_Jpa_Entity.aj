// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.bom;

import com.turbointernational.metadata.domain.bom.BOMItem;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

privileged aspect BOMItem_Roo_Jpa_Entity {
    
    declare @type: BOMItem: @Entity;
    
    declare @type: BOMItem: @Table(name = "BOM");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long BOMItem.id;
    
    @Version
    @Column(name = "version")
    private Integer BOMItem.version;
    
    public Long BOMItem.getId() {
        return this.id;
    }
    
    public void BOMItem.setId(Long id) {
        this.id = id;
    }
    
    public Integer BOMItem.getVersion() {
        return this.version;
    }
    
    public void BOMItem.setVersion(Integer version) {
        this.version = version;
    }
    
}
