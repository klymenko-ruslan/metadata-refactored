package com.turbointernational.metadata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

@Entity
@Table(name = "role")
public class Role implements Comparable<Role>, Serializable {

    private static final long serialVersionUID = -5571812817151251168L;

    public static final String ROLE_CHLOGSRC_SKIP = "ROLE_CHLOGSRC_SKIP";

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Detail.class, View.Summary.class})
    private Long id;

    @JsonView({View.Detail.class, View.Summary.class})
    @Column(name = "name")
    private String name;

    @JsonView({View.Detail.class, View.Summary.class})
    @Column(name = "display")
    private String display;

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

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
    //</editor-fold>

    @Override
    public int compareTo(Role t) {
        return this.getName().compareTo(t.getName());
    }
}
