// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.part.CompressorWheel;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CompressorWheel_Roo_Jpa_ActiveRecord {
    
    public static long CompressorWheel.countCompressorWheels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CompressorWheel o", Long.class).getSingleResult();
    }
    
    public static List<CompressorWheel> CompressorWheel.findAllCompressorWheels() {
        return entityManager().createQuery("SELECT o FROM CompressorWheel o", CompressorWheel.class).getResultList();
    }
    
    public static CompressorWheel CompressorWheel.findCompressorWheel(Long id) {
        if (id == null) return null;
        return entityManager().find(CompressorWheel.class, id);
    }
    
    public static List<CompressorWheel> CompressorWheel.findCompressorWheelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CompressorWheel o", CompressorWheel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public CompressorWheel CompressorWheel.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CompressorWheel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
