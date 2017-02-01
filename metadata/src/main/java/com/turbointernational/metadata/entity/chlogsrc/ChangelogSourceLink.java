package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;

import java.io.Serializable;
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
@NamedQueries({
        @NamedQuery(
                name = "findLastChangelogSourceLinkForSource",
                query = "select max(l.created) " +
                        "from ChangelogSourceLink l join l.sources s " +
                        "where s.id=:sourceId"
        ),
        @NamedQuery(
                name = "getChangelogSourceLinkForChangelog",
                query = "select chs.pk.link from ChangelogSource chs " +
                        "where chs.pk.link.changelog.id=:changelogId")
})
@JsonInclude(ALWAYS)
public class ChangelogSourceLink implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "changelog_id", nullable = false, unique = true)
    private Changelog changelog;

    @Temporal(TIMESTAMP)
    @Column(name = "created", nullable = false)
    @JsonView(View.Summary.class)
    private Date created;

    @JsonView({View.ChangelogSourceDetailed.class, View.Detail.class})
    @ManyToOne
    @JoinColumn(name = "create_user_id", nullable = false)
    private User createUser;

    @Column(name = "description")
    @JsonView(View.Summary.class)
    private String description;

    @Column(name = "part_id")
    @JsonView(View.Summary.class)
    private Long partId;

    @JsonView(View.ChangelogSourceDetailed.class)
    @OneToMany(mappedBy = "pk.link")
    private List<ChangelogSource> changelogSources = new ArrayList<>();

    @ManyToMany(cascade = ALL, fetch = LAZY)
    @JoinTable(name="changelog_source",
            joinColumns=@JoinColumn(name="lnk_id"),
            inverseJoinColumns=@JoinColumn(name="source_id")
    )
    private List<Source> sources = new ArrayList<>();

    public ChangelogSourceLink() {
    }

    public ChangelogSourceLink(Changelog changelog, User createUser, Date created, String description, Long partId) {
        this.changelog = changelog;
        this.createUser = createUser;
        this.created = created;
        this.description = description;
        this.partId = partId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Changelog getChangelog() {
        return changelog;
    }

    public void setChangelog(Changelog changelog) {
        this.changelog = changelog;
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

    public List<ChangelogSource> getChangelogSources() {
        return changelogSources;
    }

    public void setChangelogSources(List<ChangelogSource> changelogSources) {
        this.changelogSources = changelogSources;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }
}
