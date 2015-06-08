package com.turbointernational.metadata.domain.other;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.security.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
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
@Table(name="sales_note")
public class SalesNote {
    
    public static enum SalesNoteState {
        Draft,
        Submitted,
        Approved,
        Rejected;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
    
    @Enumerated(EnumType.STRING)
    private SalesNoteState state;
    
    @Lob
    private String comment;
    
    private Boolean published;
    
    @JsonIgnore
    @OneToMany
    @JoinTable(name="sales_note_part",
            indexes = @Index(columnList = "sales_note_id,part_id"),
            joinColumns = @JoinColumn(name = "sales_note_id"),
            inverseJoinColumns = @JoinColumn(name="part_id"))
    private List<Part> parts;
    
    @OneToMany(mappedBy="salesNote")
    private List<SalesNoteAttachment> attachments;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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

    public SalesNoteState getState() {
        return state;
    }

    public void setState(SalesNoteState state) {
        this.state = state;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @JsonIgnore
    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public List<SalesNoteAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SalesNoteAttachment> attachments) {
        this.attachments = attachments;
    }
    //</editor-fold>
    
}
