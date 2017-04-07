package com.turbointernational.metadata.entity.chlogsrc;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.util.View;

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

    private static final long serialVersionUID = 1348077383114181089L;

    //<editor-fold defaultstate="collapsed" desc="Properties">

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @ManyToOne(optional = false, cascade = DETACH)
    @JoinColumn(name = "changelog_id", nullable = false, unique = true, referencedColumnName = "id")
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

    @JsonView(View.ChangelogSourceDetailed.class)
    @OneToMany(mappedBy = "pk.link", fetch = LAZY)
    private List<ChangelogSource> changelogSources = new ArrayList<>();

    @ManyToMany(cascade = DETACH, fetch = LAZY)
    @JoinTable(name="changelog_source",
            joinColumns=@JoinColumn(name="lnk_id"),
            inverseJoinColumns=@JoinColumn(name="source_id")
    )
    private List<Source> sources = new ArrayList<>();

    @JsonView(View.ChangelogSourceDetailed.class)
    @JsonManagedReference
    @OneToMany(mappedBy = "changelogSourceLink", fetch = LAZY, orphanRemoval = true)
    private List<ChangelogSourceLinkDescriptionAttachment> descriptionAttachments = new ArrayList<>();

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">

    public ChangelogSourceLink() {
    }

    public ChangelogSourceLink(Changelog changelog, User createUser, Date created, String description) {
        this.changelog = changelog;
        this.createUser = createUser;
        this.created = created;
        this.description = description;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

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

    public List<ChangelogSourceLinkDescriptionAttachment> getDescriptionAttachments() {
        return descriptionAttachments;
    }

    public void setDescriptionAttachments(List<ChangelogSourceLinkDescriptionAttachment> descriptionAttachments) {
        this.descriptionAttachments = descriptionAttachments;
    }

    //</editor-fold>

}
