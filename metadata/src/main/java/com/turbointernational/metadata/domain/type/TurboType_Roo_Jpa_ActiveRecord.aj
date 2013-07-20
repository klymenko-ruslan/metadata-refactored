// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.type.TurboType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect TurboType_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager TurboType.entityManager;
    
    public static final EntityManager TurboType.entityManager() {
        EntityManager em = new TurboType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long TurboType.countTurboTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TurboType o", Long.class).getSingleResult();
    }
    
    public static List<TurboType> TurboType.findAllTurboTypes() {
        return entityManager().createQuery("SELECT o FROM TurboType o", TurboType.class).getResultList();
    }
    
    public static TurboType TurboType.findTurboType(Long id) {
        if (id == null) return null;
        return entityManager().find(TurboType.class, id);
    }
    
    public static List<TurboType> TurboType.findTurboTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TurboType o", TurboType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void TurboType.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void TurboType.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            TurboType attached = TurboType.findTurboType(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void TurboType.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void TurboType.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public TurboType TurboType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TurboType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
