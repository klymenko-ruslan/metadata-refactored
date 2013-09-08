package com.turbointernational.metadata.domain.changelog;

import com.turbointernational.metadata.domain.security.User;
import java.security.Principal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;

/**
 *
 * @author jrodriguez
 */
@Configurable
@Entity
@Table(name = "changelog")
@RooJpaActiveRecord
public class Changelog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="change_date", nullable = false)
    private Date changeDate;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name="description", nullable = false)
    private String description;

    @Lob
    @Column(name="data")
    private String data;
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public static Changelog log(Principal principal, String description, String data) {
        Changelog changelog = new Changelog();
        
        changelog.description = description;
        changelog.changeDate = new Date();
        changelog.data = data.toString();
        changelog.user = User.getByPrincipal(principal);
        
        changelog.persist();
        
        return changelog;
    }
    
}
