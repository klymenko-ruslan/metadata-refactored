package com.turbointernational.metadata.domain.criticaldimension;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.FetchType.EAGER;

/**
 * Created by trunikov on 22.04.16.
 */
@Cacheable
@Entity
@Table(name = "CRIT_DIM_ENUM")
@JsonInclude(ALWAYS)
public class CriticalDimensionEnum {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @JsonView({View.Summary.class})
    private Integer id;

    @Column
    @JsonView({View.Summary.class})
    private String name;
    //</editor-fold>

    @OneToMany(fetch = EAGER)
    @JsonView({View.Summary.class})
    @JoinTable(name = "CRIT_DIM_ENUM_VAL",
            joinColumns = @JoinColumn(name = "crit_dim_enum_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<CriticalDimensionEnumVal> values;

    public CriticalDimensionEnum() {
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CriticalDimensionEnumVal> getValues() {
        return values;
    }

    public void setValues(List<CriticalDimensionEnumVal> values) {
        this.values = values;
    }
    //</editor-fold>

}
