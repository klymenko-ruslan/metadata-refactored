// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.type;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import ti.metadata.domain.type.GasketType;

privileged aspect GasketType_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager GasketType.entityManager;
    
    public static final EntityManager GasketType.entityManager() {
        EntityManager em = new GasketType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long GasketType.countGasketTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM GasketType o", Long.class).getSingleResult();
    }
    
    public static List<GasketType> GasketType.findAllGasketTypes() {
        return entityManager().createQuery("SELECT o FROM GasketType o", GasketType.class).getResultList();
    }
    
    public static GasketType GasketType.findGasketType(Long id) {
        if (id == null) return null;
        return entityManager().find(GasketType.class, id);
    }
    
    public static List<GasketType> GasketType.findGasketTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM GasketType o", GasketType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void GasketType.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void GasketType.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            GasketType attached = GasketType.findGasketType(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void GasketType.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void GasketType.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public GasketType GasketType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        GasketType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
