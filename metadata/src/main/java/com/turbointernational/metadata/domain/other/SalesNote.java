package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.security.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
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
@Table(name="sales_note", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
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
    @JoinColumn(name="update_uid", nullable=false)
    private User updater;
    
    private SalesNoteState state;
    
    private Boolean published;
    
    @OneToMany(mappedBy = "id")
    @JoinTable(name="sales_note_part",
            indexes = @Index(columnList = "sales_note_id,part_id"),
            joinColumns = @JoinColumn(name = "sales_note_id"),
            inverseJoinColumns = @JoinColumn(name="part_id"))
    private List<Part> parts;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    //</editor-fold>
    
}
