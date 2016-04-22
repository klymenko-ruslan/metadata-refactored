package com.turbointernational.metadata.domain.criticaldimension;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

/**
 * Created by trunikov on 22.04.16.
 */
@Cacheable
@Entity
@Table(name = "CRIT_DIM_ENUM_VAL")
@JsonInclude(ALWAYS)
public class CriticalDimensionEnumVal {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @JsonView({View.Summary.class})
    private Integer id;

    @Column(name = "val")
    @JsonView({View.Summary.class})
    private String val;
    //</editor-fold>

    public CriticalDimensionEnumVal() {
    }

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
}
