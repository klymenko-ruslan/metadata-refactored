package com.turbointernational.metadata.domain.criticaldimension;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.GenerationType.*;

/**
 * Created by trunikov on 22.04.16.
 */
@Cacheable
@Entity
@Table(name = "CRIT_DIM_ENUM_VAL")
// public List<CriticalDimensionEnumVal> getCritDimEnumVals(Integer enumId)
@NamedQueries({
    @NamedQuery(
            name = "getAllCritDimEnums",
            query = "FROM CriticalDimensionEnumVal as cdev WHERE cdev.criticalDimensionEnumId=:enumId ORDER BY id ASC"
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
