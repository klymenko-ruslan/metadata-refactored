// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.other.Manufacturer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Manufacturer_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Manufacturer.entityManager;
    
    public static final EntityManager Manufacturer.entityManager() {
        EntityManager em = new Manufacturer().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Manufacturer.countManufacturers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Manufacturer o", Long.class).getSingleResult();
    }
    
    public static List<Manufacturer> Manufacturer.findAllManufacturers() {
        return entityManager().createQuery("SELECT o FROM Manufacturer o", Manufacturer.class).getResultList();
    }
    
    public static Manufacturer Manufacturer.findManufacturer(Long id) {
        if (id == null) return null;
        return entityManager().find(Manufacturer.class, id);
    }
    
    public static List<Manufacturer> Manufacturer.findManufacturerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Manufacturer o", Manufacturer.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Manufacturer.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Manufacturer.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Manufacturer attached = Manufacturer.findManufacturer(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Manufacturer.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Manufacturer.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Manufacturer Manufacturer.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Manufacturer merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
