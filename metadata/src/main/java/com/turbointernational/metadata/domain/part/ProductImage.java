package com.turbointernational.metadata.domain.part;

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
@Table(name = "PRODUCT_IMAGE")
public class ProductImage {

    public static String getResizedFilename(Long partId, Long imageId, int size) {
        return partId + "_" + imageId + "_"+ size + ".jpg";
    }
    
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

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
}
