package com.turbointernational.metadata.domain.other;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dmytro.trunykov@zorallabs.com 2016-08-20.
 */
@Entity
@Table(name = "cool_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class CoolType implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(View.Summary.class)
    private String name;
    //</editor-fold>

    public CoolType() {
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
