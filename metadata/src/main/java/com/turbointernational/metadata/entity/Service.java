package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by dmitro.trunykov@zorallabs.com on 2017-02-15.
 */
@Entity
@Table(name = "service")
public class Service implements Serializable {

    @Id
    @Column(name = "id")
    @JsonView({View.Summary.class})
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @JsonView({View.Summary.class})
    private String name;

    @Column(name = "description")
    @JsonView({View.Summary.class})
    private String description;

    @Column(name = "required_source", nullable = false)
    @JsonView({View.Summary.class})
    private boolean requiredSource;

    public Service() {

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

    public boolean isRequiredSource() {
        return requiredSource;
    }

    public void setRequiredSource(boolean requiredSource) {
        this.requiredSource = requiredSource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
