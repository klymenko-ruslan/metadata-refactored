package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.service.SearchableEntity;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.SearchService;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author jrodriguez
 */
@Cacheable
@Entity
@Table(name = "sales_note_part")
@AssociationOverrides({
        @AssociationOverride(name = "pk.salesNote",
                joinColumns = @JoinColumn(name = "sales_note_id")),
        @AssociationOverride(name = "pk.part",
                joinColumns = @JoinColumn(name = "part_id"))})
public class SalesNotePart implements Serializable, SearchableEntity {

    private final static Logger log = LoggerFactory.getLogger(SalesNotePart.class);

    @EmbeddedId
    private SalesNotePartId pk;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "create_uid", nullable = false)
    private User creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "write_date")
    private Date updateDate;

    @OneToOne
    @JoinColumn(name = "write_uid", nullable = false)
    private User updater;

    @Column(name = "primary_part", nullable = false)
    private boolean primary;

    public SalesNotePart() {
    }

    public SalesNotePart(SalesNotePartId pk, Date createDate, User creator, Date updateDate, User updater, boolean primary) {
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
                primary);
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

    @JsonView({View.Summary.class})
    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * This method is called by Flexjson during serialization.
     *
     * @return
     * @see #toSearchJson()
     */
    public long getPrimaryPartId() {
        long primaryPartId = primary ? pk.getPart().getId() : pk.getSalesNote().getPrimaryPartId();
        return primaryPartId;
    }

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        SearchService.instance().deleteSalesNotePart(this);
    }

    @PostUpdate
    @PostPersist
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().indexSalesNotePart(this);
    }

    @Override
    public void beforeIndexing() {
        // Nothing.
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Serialization">
    protected JSONSerializer getSearchSerializer() {
        return new JSONSerializer()
                .include("createDate")
                .include("primary")
                .include("primaryPartId")
                .include("pk.salesNote.id")
                .include("pk.salesNote.state")
                .include("pk.salesNote.comment")
                .include("pk.part.id")
                .include("pk.part.manufacturerPartNumber")
                .exclude("*.class");
    }

    @Override
    public String toSearchJson(List<CriticalDimension> criticalDimensions) {
        return getSearchSerializer().exclude("*").serialize(this);
    }

    @Override
    public String getSearchId() {
        SalesNotePartId pk = getPk();
        return pk.getSalesNote().getId().toString() + "_" + pk.getPart().getId().toString();
    }
    //</editor-fold>

}
