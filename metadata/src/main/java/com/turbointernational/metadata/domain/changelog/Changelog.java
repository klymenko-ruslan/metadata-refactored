package com.turbointernational.metadata.domain.changelog;

import com.turbointernational.metadata.domain.security.User;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jrodriguez
 */
@Configurable
@Entity
@Table(name = "changelog")
public class Changelog implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="properties">
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
    
    public static Changelog log(String description, String data) {
        Changelog changelog = new Changelog();
        
        changelog.description = description;
        changelog.changeDate = new Date();
        changelog.data = data;
        changelog.user = User.getCurrentUser();
        
        changelog.persist();
        
        return changelog;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Changelog().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    //</editor-fold>
}
