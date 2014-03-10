package com.turbointernational.metadata.domain.changelog;

import com.turbointernational.metadata.domain.security.User;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jrodriguez
 */
@Configurable
@Entity
@Table(name = "changelog")
public class Changelog {
    
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
        changelog.data = data.toString();
        changelog.user = User.getCurrentUser();
        
        changelog.persist();
        
        return changelog;
    }
    
    @Deprecated
    public static Changelog log(Principal principal, String description, String data) {
        return log(description, data);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="activerecord">
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Changelog().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countChangelogs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Changelog o", Long.class).getSingleResult();
    }
    
    public static List<Changelog> findAllChangelogs() {
        return entityManager().createQuery("SELECT o FROM Changelog o", Changelog.class).getResultList();
    }
    
    public static Changelog findChangelog(Long id) {
        if (id == null) return null;
        return entityManager().find(Changelog.class, id);
    }
    
    public static List<Changelog> findChangelogEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Changelog o", Changelog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Changelog attached = findChangelog(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Changelog merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Changelog merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    //</editor-fold>
}
