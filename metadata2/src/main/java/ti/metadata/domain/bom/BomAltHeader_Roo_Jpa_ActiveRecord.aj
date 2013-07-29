// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.bom;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import ti.metadata.domain.bom.BomAltHeader;

privileged aspect BomAltHeader_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager BomAltHeader.entityManager;
    
    public static final EntityManager BomAltHeader.entityManager() {
        EntityManager em = new BomAltHeader().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long BomAltHeader.countBomAltHeaders() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BomAltHeader o", Long.class).getSingleResult();
    }
    
    public static List<BomAltHeader> BomAltHeader.findAllBomAltHeaders() {
        return entityManager().createQuery("SELECT o FROM BomAltHeader o", BomAltHeader.class).getResultList();
    }
    
    public static BomAltHeader BomAltHeader.findBomAltHeader(Long id) {
        if (id == null) return null;
        return entityManager().find(BomAltHeader.class, id);
    }
    
    public static List<BomAltHeader> BomAltHeader.findBomAltHeaderEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BomAltHeader o", BomAltHeader.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void BomAltHeader.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void BomAltHeader.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            BomAltHeader attached = BomAltHeader.findBomAltHeader(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void BomAltHeader.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void BomAltHeader.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public BomAltHeader BomAltHeader.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BomAltHeader merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
