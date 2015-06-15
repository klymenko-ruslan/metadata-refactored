package com.turbointernational.metadata.domain.part.salesnote;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.security.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jrodriguez
 */
@Cacheable
@Entity
@Table(name="sales_note_part")
@AssociationOverrides({
		@AssociationOverride(name = "pk.salesNote", 
			joinColumns = @JoinColumn(name = "sales_note_id")),
		@AssociationOverride(name = "pk.part", 
			joinColumns = @JoinColumn(name = "part_id")) })
public class SalesNotePart implements Serializable {
    
    @EmbeddedId
    private SalesNotePartId pk;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;
    
    @OneToOne
    @JoinColumn(name="create_uid", nullable=false)
    private User creator;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="write_date")
    private Date updateDate;
    
    @OneToOne
    @JoinColumn(name="write_uid", nullable=false)
    private User updater;
    
    @Column(name="primary_part")
    private Boolean primary;

    public SalesNotePart() {
    }

    public SalesNotePart(SalesNotePartId pk, Date createDate, User creator, Date updateDate, User updater, Boolean primary) {
        this.pk = pk;
        this.createDate = createDate;
        this.creator = creator;
        this.updateDate = updateDate;
        this.updater = updater;
        this.primary = primary;
    }
    
    public SalesNotePartId getPk() {
        return pk;
    }

    public void setPk(SalesNotePartId pk) {
        this.pk = pk;
    }
    
//    @Transient
    public SalesNote getSalesNote() {
        return getPk().getSalesNote();
    }
    
    public void setSalesNote(SalesNote salesNote) {
        getPk().setSalesNote(salesNote);
    }
    
//    @Transient
    public Part getPart() {
        return getPk().getPart();
    }
    
    public void setPart(Part part) {
        getPk().setPart(part);
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public User getUpdater() {
        return updater;
    }

    public void setUpdater(User updater) {
        this.updater = updater;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
    
}
