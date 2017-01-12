package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/12/17.
 */
@Entity
@Table(name = "source_attachment")
public class SourceAttachment {

    //<editor-fold defaultstate="collapsed" desc="properties">

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    @JsonView(View.Summary.class)
    @Column(name = "name", nullable = false)
    private String name;

    @JsonView(View.Summary.class)
    @Column(name = "description")
    private String description;

    public SourceAttachment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //</editor-fold>

}
