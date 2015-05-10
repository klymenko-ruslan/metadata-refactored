package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.security.User;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author jrodriguez
 */
@Cacheable
@Entity
@Table(name="sales_note_attachment", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class SalesNoteAttachment implements Serializable {
    
    @Id
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
    @JoinColumn(name="update_uid", nullable=false)
    private User updater;
    
    @Column(name="filename")
    private String filename;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
