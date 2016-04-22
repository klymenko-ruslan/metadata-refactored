package com.turbointernational.metadata.domain.criticaldimension;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Id
    private Integer id;

    @Column
    private String name;

    @OneToMany(fetch = EAGER)
    @JoinTable(name = "CRIT_DIM_ENUM_VAL",
            joinColumns = @JoinColumn(name = "crit_dim_enum_id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<CriticalDimensionEnumVal> values;

    public CriticalDimensionEnum() {
    }

    public CriticalDimensionEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
