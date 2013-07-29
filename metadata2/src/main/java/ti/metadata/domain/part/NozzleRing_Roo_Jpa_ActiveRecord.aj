// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.part;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import ti.metadata.domain.part.NozzleRing;

privileged aspect NozzleRing_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager NozzleRing.entityManager;
    
    public static final EntityManager NozzleRing.entityManager() {
        EntityManager em = new NozzleRing().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long NozzleRing.countNozzleRings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM NozzleRing o", Long.class).getSingleResult();
    }
    
    public static List<NozzleRing> NozzleRing.findAllNozzleRings() {
        return entityManager().createQuery("SELECT o FROM NozzleRing o", NozzleRing.class).getResultList();
    }
    
    public static NozzleRing NozzleRing.findNozzleRing(Long partId) {
        if (partId == null) return null;
        return entityManager().find(NozzleRing.class, partId);
    }
    
    public static List<NozzleRing> NozzleRing.findNozzleRingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM NozzleRing o", NozzleRing.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void NozzleRing.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void NozzleRing.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            NozzleRing attached = NozzleRing.findNozzleRing(this.partId);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void NozzleRing.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void NozzleRing.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public NozzleRing NozzleRing.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        NozzleRing merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
