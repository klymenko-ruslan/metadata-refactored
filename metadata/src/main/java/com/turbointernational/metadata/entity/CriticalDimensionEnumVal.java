package com.turbointernational.metadata.entity;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.cache.annotation.Cacheable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by trunikov on 22.04.16.
 */
@Cacheable
@Entity
@Table(name = "crit_dim_enum_val")
@NamedQueries({
    @NamedQuery(
            name = "getAllCritDimEnumVals",
            query = "SELECT cdev FROM CriticalDimensionEnumVal cdev WHERE cdev.criticalDimensionEnumId=:enumId ORDER BY cdev.id ASC"
    ),
    @NamedQuery(
            name = "findCritDimEnumValByName",
            query = "SELECT cdev FROM CriticalDimensionEnumVal cdev WHERE cdev.criticalDimensionEnumId=:enumId AND cdev.val=:name"
    )
})
@JsonInclude(ALWAYS)
public class CriticalDimensionEnumVal {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView({View.Summary.class})
    private Integer id;

    @Column(name = "val", nullable = false, length = 64)
    @JsonView({View.Summary.class})
    private String val;

    @Column(name = "crit_dim_enum_id", nullable = false)
    @JsonView({View.Summary.class})
    private Integer criticalDimensionEnumId;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public Integer getCriticalDimensionEnumId() {
        return criticalDimensionEnumId;
    }

    public void setCriticalDimensionEnumId(Integer criticalDimensionEnumId) {
        this.criticalDimensionEnumId = criticalDimensionEnumId;
    }
    //</editor-fold>

}
