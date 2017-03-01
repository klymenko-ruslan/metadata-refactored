package com.turbointernational.metadata.entity.part.types.kit;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com 2016-08-20.
 */
@Entity
@Table(name = "kit_type", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class KitType implements Serializable {

    private static final long serialVersionUID = 2861237940352358511L;

    // <editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(View.Summary.class)
    private String name;
    // </editor-fold>

    public KitType() {
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

}
