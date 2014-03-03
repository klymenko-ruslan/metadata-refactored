package com.turbointernational.metadata.domain.security;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Role implements Comparable<Role>, Serializable {
    public static final long serialVersionUID = 1L;
    
    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String display;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDisplay() {
        return display;
    }
    
    public void setDisplay(String display) {
        this.display = display;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="activerecord">
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new Role().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countRoles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Role o", Long.class).getSingleResult();
    }
    
    public static List<Role> findAllRoles() {
        return entityManager().createQuery("SELECT o FROM Role o", Role.class).getResultList();
    }
    
    public static Role findRole(Long id) {
        if (id == null) return null;
        return entityManager().find(Role.class, id);
    }
    
    public static List<Role> findRoleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Role o", Role.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Role attached = findRole(this.id);
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
    public Role merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Role merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    //</editor-fold>
    
    @Override
    public int compareTo(Role t) {
        return this.getName().compareTo(t.getName());
    }
}
