package com.turbointernational.metadata.domain.part.salesnote;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.turbointernational.metadata.domain.security.User;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class SalesNote implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="create_uid", nullable=false)
    private User creator;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="write_date")
    private Date updateDate;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="write_uid", nullable=false)
    private User updater;
    
    @Enumerated(EnumType.STRING)
    private SalesNoteState state;
    
    @Lob
    private String comment;
    
    @JsonIgnore
    @OneToMany(mappedBy = "pk.salesNote", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name="sales_note_part",
//            indexes = @Index(columnList = "sales_note_id,part_id"),
//            joinColumns = @JoinColumn(name = "sales_note_id"),
//            inverseJoinColumns = @JoinColumn(name="part_id"))
    private Set<SalesNotePart> parts = Sets.newLinkedHashSet();
    
    @OneToMany(mappedBy="salesNote", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<SalesNotePart> getParts() {
        return parts;
    }

    public void setParts(Set<SalesNotePart> parts) {
        this.parts = parts;
    }
    
    public List<SalesNoteAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SalesNoteAttachment> attachments) {
        this.attachments = attachments;
    }
}
