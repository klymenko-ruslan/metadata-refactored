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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;
    
    private String filename;

    public String getFilename() {
        return filename;
    }
}
