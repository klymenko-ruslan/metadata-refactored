package com.turbointernational.metadata.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author jrodriguez
 */
@Cacheable
@Entity
@Table(name = "sales_note_attachment")
public class SalesNoteAttachment implements Serializable {

    private static final long serialVersionUID = -8697352209448012944L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sales_note_id")
    private SalesNote salesNote;

    @Temporal(TIMESTAMP)
    @Column(name = "create_date")
    @JsonView(View.Summary.class)
    private Date createDate;

    @OneToOne
    @JoinColumn(name = "create_uid", nullable = false)
    @JsonView(View.Summary.class)
    private User creator;

    @Temporal(TIMESTAMP)
    @Column(name = "write_date")
    private Date updateDate;

    @OneToOne
    @JoinColumn(name = "write_uid", nullable = false)
    private User updater;

    @Column(name = "filename")
    @JsonView(View.Summary.class)
    private String filename;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SalesNote getSalesNote() {
        return salesNote;
    }

    public void setSalesNote(SalesNote salesNote) {
        this.salesNote = salesNote;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    //<editor-fold defaultstate="collapsed" desc="Lifecycle">
    @Value("${attachments.salesNote}")
    @Transient
    private File attachmentDir;

    public File getFile() {
        return new File(attachmentDir, filename);
    }

    @PreRemove
    public void deleteFileOnRemove() {
        getFile().delete();
    }
    //</editor-fold>

}
