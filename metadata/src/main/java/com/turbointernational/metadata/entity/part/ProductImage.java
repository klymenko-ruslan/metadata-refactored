package com.turbointernational.metadata.entity.part;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.ProductImageDao;
import com.turbointernational.metadata.util.View;

import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;

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

    private static final long serialVersionUID = -6456333513214491899L;

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    private Long id;

    @Column(name = "publish", nullable = false)
    @JsonView({View.Summary.class})
    private Boolean publish;

    @Column(name = "main", nullable = false)
    @JsonView({View.Summary.class})
    private Boolean main;

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

    public Boolean getMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }

    //</editor-fold>

    public String toJson() {
        return new JSONSerializer()
                .transform(new HibernateTransformer(), this.getClass())
                .include("id")
                .include("filename")
                .include("publish")
                .include("main")
                .exclude("*")
                .serialize(this);
    }

    @Override
    public int compareTo(ProductImage t) {
        return id.compareTo(t.id);
    }

}
