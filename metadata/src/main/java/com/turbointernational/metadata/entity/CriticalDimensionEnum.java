package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by trunikov on 22.04.16.
 */
@Cacheable
@Entity
@Table(name = "crit_dim_enum")
@NamedQueries({
    @NamedQuery(
            name = "getAllCritDimEnums",
            query = "FROM CriticalDimensionEnum ORDER BY id ASC"
    ),
    @NamedQuery(
            name = "findCritDimEnumByName",
            query = "FROM CriticalDimensionEnum WHERE name=:name"
    )
})
@JsonInclude(ALWAYS)
//
public class CriticalDimensionEnum {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView({View.Summary.class})
    private Integer id;

    @Column(nullable = false, length = 64)
    @JsonView({View.Summary.class})
    private String name;
    //</editor-fold>

    @OneToMany(fetch = EAGER)
    @JsonView({View.Detail.class})
    @JoinTable(name = "crit_dim_enum_val",
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
