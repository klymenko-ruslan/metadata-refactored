package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/20/17.
 */
@Entity
@Table(name = "changelog_source_link")
public class ChangelogSourceLink {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Column(name = "description")
    @JsonView(View.Summary.class)
    private String description;

    public ChangelogSourceLink() {
    }

    public ChangelogSourceLink(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
