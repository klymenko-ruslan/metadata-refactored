// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.part.NozzleRing;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect NozzleRing_Roo_Jpa_ActiveRecord {
    
    public static long NozzleRing.countNozzleRings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM NozzleRing o", Long.class).getSingleResult();
    }
    
    public static List<NozzleRing> NozzleRing.findAllNozzleRings() {
        return entityManager().createQuery("SELECT o FROM NozzleRing o", NozzleRing.class).getResultList();
    }
    
    public static NozzleRing NozzleRing.findNozzleRing(Long id) {
        if (id == null) return null;
        return entityManager().find(NozzleRing.class, id);
    }
    
    public static List<NozzleRing> NozzleRing.findNozzleRingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM NozzleRing o", NozzleRing.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public NozzleRing NozzleRing.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        NozzleRing merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
