package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/12/17.
 */
@Entity
@Table(name = "source_name")
public class SourceName implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @JsonView(View.Summary.class)
    @Column(name = "name")
    private String name;

    public SourceName() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //</editor-fold>

}
