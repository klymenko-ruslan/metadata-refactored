package com.turbointernational.metadata.entity.part;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.ProductImageDao;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jrodriguez
 */

@Cacheable
@Entity
@Table(name = "product_image")
@NamedQueries(
        @NamedQuery(
                name = "findProductImagesForPart",
                query = "SELECT DISTINCT pi FROM ProductImage pi WHERE pi.part.id IN :productIds ORDER BY pi.id"
        )
)
public class ProductImage implements Comparable<ProductImage>, Serializable {


    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;

    // alter table product_image add column publish tinyint(1) not null default 1;
    @Column(name = "publish", nullable = false)
    @JsonView({View.Summary.class})
    private Boolean publish;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    @Column(name = "filename")
    @JsonView({View.Summary.class})
    private String filename;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPublish() {
        return publish;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
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
                .include("publish")
                .exclude("*")
                .serialize(this);
    }

    @Override
    public int compareTo(ProductImage t) {
        return id.compareTo(t.id);
    }

}
