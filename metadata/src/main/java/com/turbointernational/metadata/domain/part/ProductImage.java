package com.turbointernational.metadata.domain.part;

import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author jrodriguez
 */

@Configurable
@Cacheable
@Entity
@Table(name = "product_image")
public class ProductImage implements Comparable<ProductImage>, Serializable {

    public static String getResizedFilename(Long partId, Long imageId, int size) {
        return size + "/" + partId + "_" + imageId + ".jpg";
    }

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;
    
    private String filename;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Part getPart() {
        return part;
    }
    
    public void setPart(Part part) {
        this.part = part;
    }
    
    public String getFilename() {
        return filename;
    }
    
    public String getFilename(int size) {
        return getResizedFilename(part.getId(), id, size);
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ActiveRecord">
    @PersistenceContext
    @Transient
    private EntityManager entityManager;
    
    public static final EntityManager entityManager() {
        EntityManager em = new ProductImage().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long count() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProductImage o", Long.class).getSingleResult();
    }
    
    public static ProductImage find(Long id) {
        if (id == null) return null;
        Query q = entityManager().createQuery("SELECT DISTINCT p FROM ProductImage p WHERE p.id = :id");
        q.setParameter("id", id);
        return (ProductImage) q.getSingleResult();
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
            ProductImage attached = ProductImage.find(this.id);
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
    public void refresh() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.refresh(this);
    }
    //</editor-fold>
    
    public String toJson() {
        return new JSONSerializer()
                .transform(new HibernateTransformer(), this.getClass())
                .include("id")
                .include("filename")
                .exclude("*")
                .serialize(this);
    }

    @Override
    public int compareTo(ProductImage t) {
        return id.compareTo(t.id);
    }
    
}
