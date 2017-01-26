package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/20/17.
 */
@Entity
@Table(name = "changelog_source_link")
@NamedQueries(
        @NamedQuery(
                name = "findLastChangelogSourceLinkForSource",
                query = "select max(l.created) " +
                        "from ChangelogSourceLink l join l.sources s " +
                        "where s.id=:sourceId"
        )
)
@JsonInclude(ALWAYS)
public class ChangelogSourceLink {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @Temporal(TIMESTAMP)
    @Column(name = "created", nullable = false)
    @JsonView(View.Summary.class)
    private Date created;

    @JsonView(View.Detail.class)
    @ManyToOne
    @JoinColumn(name = "create_user_id", nullable = false)
    private User createUser;

    @Column(name = "description")
    @JsonView(View.Summary.class)
    private String description;

    @ManyToMany(cascade = ALL, fetch = LAZY)
    @JoinTable(name="changelog_source",
            joinColumns=@JoinColumn(name="lnk_id"),
            inverseJoinColumns=@JoinColumn(name="source_id")
    )

    private List<Source> sources = new ArrayList<>();

    public ChangelogSourceLink() {
    }

    public ChangelogSourceLink(User createUser, Date created, String description) {
        this.createUser = createUser;
        this.created = created;
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

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

}
