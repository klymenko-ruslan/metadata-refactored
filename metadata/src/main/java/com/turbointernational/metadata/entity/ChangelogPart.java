package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-02-03.
 */
@Entity
@Table(name = "changelog_part")
@JsonInclude(ALWAYS)
public class ChangelogPart implements Serializable {

    public enum Type { BOM_PARENT, BOM_CHILD }

    //<editor-fold defaultstate="collapsed" desc="Properties">

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "changelog_id", nullable = false)
    @JsonView(View.Summary.class)
    private Changelog changelog;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    @JsonView(View.Summary.class)
    private Part part;

    @Column(name = "typ", nullable = false)
    @Enumerated(STRING)
    @JsonView(View.Summary.class)
    private Type type;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">

    public ChangelogPart() {
    }

    public ChangelogPart(Long id, Changelog changelog, Part part, Type type) {
        this(changelog, part, type);
        this.id = id;
    }

    public ChangelogPart(Changelog changelog, Part part, Type type) {
        this.changelog = changelog;
        this.part = part;
        this.type = type;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    //</editor-fold>

}
