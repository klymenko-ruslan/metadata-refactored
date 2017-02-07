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

    public enum Role { BOM_PARENT, BOM_CHILD, PART0, PART1 }

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

    @Column(name = "role", nullable = false)
    @Enumerated(STRING)
    @JsonView(View.Summary.class)
    private Role role;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">

    public ChangelogPart() {
    }

    public ChangelogPart(Long id, Changelog changelog, Part part, Role role) {
        this(changelog, part, role);
        this.id = id;
    }

    public ChangelogPart(Changelog changelog, Part part, Role role) {
        this.changelog = changelog;
        this.part = part;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //</editor-fold>

}
