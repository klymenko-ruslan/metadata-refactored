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
import ti.metadata.domain.part.CompressorWheel;

privileged aspect CompressorWheel_Roo_Jpa_Entity {
    
    declare @type: CompressorWheel: @Entity;
    
    declare @type: CompressorWheel: @Table(name = "compressor_wheel");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "part_id")
    private Long CompressorWheel.partId;
    
    @Version
    @Column(name = "version")
    private Integer CompressorWheel.version;
    
    public Long CompressorWheel.getPartId() {
        return this.partId;
    }
    
    public void CompressorWheel.setPartId(Long id) {
        this.partId = id;
    }
    
    public Integer CompressorWheel.getVersion() {
        return this.version;
    }
    
    public void CompressorWheel.setVersion(Integer version) {
        this.version = version;
    }
    
}
