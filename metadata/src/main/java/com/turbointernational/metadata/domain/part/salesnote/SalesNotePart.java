package com.turbointernational.metadata.domain.part.salesnote;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.web.View;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
    
    /**
     * Convenience method for new sales note parts.
     */
    public SalesNotePart(SalesNote salesNote, Part part, boolean primary, User user) {
        this(new SalesNotePartId(salesNote, part),
                
              // Create
              new Date(), user,
                
              // Update
              new Date(), user,
                
              // Primary Part
              primary ? true : null);
    }
    
    public SalesNotePartId getPk() {
        return pk;
    }

    public void setPk(SalesNotePartId pk) {
        this.pk = pk;
    }
    
    public SalesNote getSalesNote() {
        return getPk().getSalesNote();
    }
    
    public void setSalesNote(SalesNote salesNote) {
        getPk().setSalesNote(salesNote);
    }
    
    @JsonView({View.DetailWithPartsAndAttachments.class})
    public Part getPart() {
        return getPk().getPart();
    }
    
    public void setPart(Part part) {
        getPk().setPart(part);
    }
    
    @JsonView({View.DetailWithParts.class})
    public long getPartId() {
        return pk.getPart().getId();
    }
    
    @JsonView({View.DetailWithParts.class})
    public String getPartNumber() {
        return pk.getPart().getManufacturerPartNumber();
    }
    
    @JsonView({View.DetailWithParts.class})
    public Manufacturer getManufacturer() {
        return pk.getPart().getManufacturer();
    }
    
    @JsonView({View.Summary.class})
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonView({View.Summary.class})
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @JsonView({View.Summary.class})
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @JsonView({View.Summary.class})
    public User getUpdater() {
        return updater;
    }

    @JsonView({View.Summary.class})
    public void setUpdater(User updater) {
        this.updater = updater;
    }

    @Deprecated
    /**
     * @deprecated Note: May be null.
     * @see #isPrimary()
     */
    public Boolean getPrimary() {
        return primary;
    }

    @JsonView({View.Summary.class})
    public boolean isPrimary() {
        return Objects.equals(Boolean.TRUE, primary);
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
    
}