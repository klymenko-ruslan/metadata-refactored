package com.turbointernational.metadata.entity.chlogsrc;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/12/17.
 */
@Entity
@Table(name = "source_name")
@NamedQueries({
        @NamedQuery(name = "findChangelogSourceNameByName", query = "SELECT sn FROM SourceName sn WHERE sn.name=:name"),
        @NamedQuery(name = "findAllChangelogSourceNames", query = "SELECT sn FROM SourceName sn ORDER BY sn.name")
})
public class SourceName implements Serializable {

    private static final long serialVersionUID = -8202277534967335041L;

    //<editor-fold defaultstate="collapsed" desc="Properties">

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @JsonView(View.Summary.class)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">

    public SourceName() {
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

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
