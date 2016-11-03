package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jrodriguez
 */
@Cacheable
@Entity
@Table(name = "sales_note")
public class SalesNote implements Serializable {

    private final static Logger log = LoggerFactory.getLogger(SalesNote.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class, View.Detail.class})
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    @JsonView({View.Summary.class, View.Detail.class})
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "create_uid", nullable = false)
    @JsonView({View.Summary.class, View.Detail.class})
    private User creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "write_date")
    @JsonView({View.Summary.class, View.Detail.class})
    private Date updateDate;

    @OneToOne
    @JoinColumn(name = "write_uid", nullable = false)
    @JsonView({View.Summary.class, View.Detail.class})
    private User updater;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @JsonView({View.Summary.class, View.Detail.class})
    private SalesNoteState state;

    @Lob
    @Column(name = "comment")
    @JsonView({View.Summary.class, View.Detail.class})
    private String comment;

    @OneToMany(mappedBy = "pk.salesNote", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(name="sales_note_part",
//            indexes = @Index(columnList = "sales_note_id,part_id"),
//            joinColumns = @JoinColumn(name = "sales_note_id"),
//            inverseJoinColumns = @JoinColumn(name="part_id"))
    @OrderBy("id")
    @JsonView({View.DetailWithParts.class, View.DetailWithPartsAndAttachments.class})
    private List<SalesNotePart> parts = new ArrayList<>();

    @JsonView({View.DetailWithPartsAndAttachments.class})
    @OrderBy("id")
    @OneToMany(mappedBy = "salesNote", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SalesNoteAttachment> attachments = new ArrayList<>();

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

    public List<SalesNotePart> getParts() {
        return parts;
    }

    public void setParts(List<SalesNotePart> parts) {
        this.parts = parts;
    }

    public List<SalesNoteAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SalesNoteAttachment> attachments) {
        this.attachments = attachments;
    }

    @JsonView({View.DetailWithParts.class})
    public Part getPrimaryPart() {
        for (SalesNotePart snp : parts) {
            if (snp.isPrimary()) {
                return snp.getPart();
            }
        }

        throw new IllegalStateException("No primary part found.");
    }

    @JsonView({View.Summary.class, View.Detail.class})
    public long getPrimaryPartId() {
        for (SalesNotePart snp : parts) {
            if (snp.isPrimary()) {
                return snp.getPart().getId();
            }
        }

        throw new IllegalStateException("No primary part found.");
    }

}
