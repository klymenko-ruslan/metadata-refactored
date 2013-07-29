// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.part;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import ti.metadata.domain.other.TurboModel;
import ti.metadata.domain.part.Part;
import ti.metadata.domain.part.Turbo;
import ti.metadata.domain.type.CoolType;

privileged aspect Turbo_Roo_DbManaged {
    
    @OneToOne
    @JoinColumn(name = "PART_ID", nullable = false, insertable = false, updatable = false)
    private Part Turbo.part;
    
    @ManyToOne
    @JoinColumn(name = "cool_type_id", referencedColumnName = "ID")
    private CoolType Turbo.coolTypeId;
    
    @ManyToOne
    @JoinColumn(name = "turbo_model_id", referencedColumnName = "ID", nullable = false)
    private TurboModel Turbo.turboModelId;
    
    public Part Turbo.getPart() {
        return part;
    }
    
    public void Turbo.setPart(Part part) {
        this.part = part;
    }
    
    public CoolType Turbo.getCoolTypeId() {
        return coolTypeId;
    }
    
    public void Turbo.setCoolTypeId(CoolType coolTypeId) {
        this.coolTypeId = coolTypeId;
    }
    
    public TurboModel Turbo.getTurboModelId() {
        return turboModelId;
    }
    
    public void Turbo.setTurboModelId(TurboModel turboModelId) {
        this.turboModelId = turboModelId;
    }
    
}
