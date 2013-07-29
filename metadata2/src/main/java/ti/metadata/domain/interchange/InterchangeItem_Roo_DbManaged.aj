// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.interchange;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import ti.metadata.domain.interchange.InterchangeHeader;
import ti.metadata.domain.interchange.InterchangeItem;
import ti.metadata.domain.part.Part;

privileged aspect InterchangeItem_Roo_DbManaged {
    
    @OneToOne
    @JoinColumn(name = "part_id", nullable = false, insertable = false, updatable = false)
    private Part InterchangeItem.part;
    
    @ManyToOne
    @JoinColumn(name = "interchange_header_id", referencedColumnName = "ID", nullable = false)
    private InterchangeHeader InterchangeItem.interchangeHeaderId;
    
    public Part InterchangeItem.getPart() {
        return part;
    }
    
    public void InterchangeItem.setPart(Part part) {
        this.part = part;
    }
    
    public InterchangeHeader InterchangeItem.getInterchangeHeaderId() {
        return interchangeHeaderId;
    }
    
    public void InterchangeItem.setInterchangeHeaderId(InterchangeHeader interchangeHeaderId) {
        this.interchangeHeaderId = interchangeHeaderId;
    }
    
}
