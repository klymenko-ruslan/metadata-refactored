// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.part;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import ti.metadata.domain.part.Kit;

privileged aspect Kit_Roo_Jpa_Entity {
    
    declare @type: Kit: @Entity;
    
    declare @type: Kit: @Table(name = "kit");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PART_ID")
    private Long Kit.partId;
    
    @Version
    @Column(name = "version")
    private Integer Kit.version;
    
    public Long Kit.getPartId() {
        return this.partId;
    }
    
    public void Kit.setPartId(Long id) {
        this.partId = id;
    }
    
    public Integer Kit.getVersion() {
        return this.version;
    }
    
    public void Kit.setVersion(Integer version) {
        this.version = version;
    }
    
}
