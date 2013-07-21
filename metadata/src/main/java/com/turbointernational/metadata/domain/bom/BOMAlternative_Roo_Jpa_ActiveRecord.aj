// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.bom;

import com.turbointernational.metadata.domain.bom.BOMAlternative;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect BOMAlternative_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager BOMAlternative.entityManager;
    
    public static final EntityManager BOMAlternative.entityManager() {
        EntityManager em = new BOMAlternative().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long BOMAlternative.countBOMAlternatives() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BOMAlternative o", Long.class).getSingleResult();
    }
    
    public static List<BOMAlternative> BOMAlternative.findAllBOMAlternatives() {
        return entityManager().createQuery("SELECT o FROM BOMAlternative o", BOMAlternative.class).getResultList();
    }
    
    public static BOMAlternative BOMAlternative.findBOMAlternative(Long id) {
        if (id == null) return null;
        return entityManager().find(BOMAlternative.class, id);
    }
    
    public static List<BOMAlternative> BOMAlternative.findBOMAlternativeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BOMAlternative o", BOMAlternative.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void BOMAlternative.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void BOMAlternative.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            BOMAlternative attached = BOMAlternative.findBOMAlternative(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void BOMAlternative.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void BOMAlternative.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public BOMAlternative BOMAlternative.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BOMAlternative merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
