package com.turbointernational.metadata.domain.part;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jrodriguez
 */

@Cacheable
@Entity
@Table(name = "product_image")
public class ProductImage implements Comparable<ProductImage>, Serializable {


    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;
    
    @JsonView({View.Summary.class})
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
        return ProductImageDao.getResizedFilename(part.getId(), id, size);
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
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
