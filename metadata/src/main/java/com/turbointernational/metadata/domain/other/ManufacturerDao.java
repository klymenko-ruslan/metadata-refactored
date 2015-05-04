package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.GenericDao;
import static com.turbointernational.metadata.domain.other.Manufacturer.TI_ID;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jrodriguez
 */
@Repository
public class ManufacturerDao extends GenericDao<Manufacturer> {
    
    public ManufacturerDao() {
        super(Manufacturer.class);
    }
    
    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public List<Manufacturer> findAllManufacturers() {
        return em.createQuery("SELECT o FROM Manufacturer o", Manufacturer.class).getResultList();
    }
    
    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public Manufacturer findManufacturer(Long id) {
        if (id == null) return null;
        return em.find(Manufacturer.class, id);
    }
    
    @com.googlecode.ehcache.annotations.Cacheable(cacheName = "manufacturers")
    public Manufacturer TI() {
        return findManufacturer(TI_ID);
    }
}
