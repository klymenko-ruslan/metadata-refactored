package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.CriticalDimension;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.service.SearchService;
import com.turbointernational.metadata.service.SearchableEntity;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2017-01-12.
 */
@Entity
@Table(name = "source")
@NamedQueries({
        @NamedQuery(name = "findChangelogSourceByName", query = "from Source s where s.name=:name"),
        @NamedQuery(
                name = "findLastPickedChangelogSources",
                query = "select distinct s from Source s where s.id in(" +
                        "   select s2.id " +
                        "   from Source s2 join s2.changelogSourceLinks lnk " +
                        "   where lnk.createUser.id = :userId " +
                        "   order by lnk.created desc)"),
        @NamedQuery(
                name = "getNumChangelogSourcesForSourceName",
                query = "select count(*) from Source s where s.sourceName.id=:sourceNameId")
})
@JsonInclude(ALWAYS)
public class Source implements SearchableEntity, Serializable {

    private final static Logger log = LoggerFactory.getLogger(Source.class);

    //<editor-fold defaultstate="collapsed" desc="Properties">

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView(View.Summary.class)
    private Long id;

    @ManyToOne
    @JsonView(View.Summary.class)
    @JoinColumn(name = "source_name_id", nullable = false)
    private SourceName sourceName;

    @JsonView(View.Summary.class)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @JsonView(View.Summary.class)
    @Column(name = "description")
    private String description;

    @JsonView(View.Summary.class)
    @Column(name = "url")
    private String url;

    @JsonView(View.Summary.class)
    @Column(name = "created")
    @Temporal(TIMESTAMP)
    private Date created;

    @JsonView(View.Detail.class)
    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private User createUser;

    @JsonView(View.Summary.class)
    @Column(name = "updated")
    @Temporal(TIMESTAMP)
    private Date updated;

    @JsonView(View.Detail.class)
    @ManyToOne
    @JoinColumn(name = "update_user_id")
    private User updateUser;

    @JsonView(View.Detail.class)
    @OneToMany(mappedBy = "source", fetch = LAZY, orphanRemoval = true)
    private List<SourceAttachment> attachments = new ArrayList<>();

    @ManyToMany(cascade = ALL, fetch = LAZY)
    @JoinTable(name="changelog_source",
            joinColumns=@JoinColumn(name="source_id"),
            inverseJoinColumns=@JoinColumn(name="lnk_id")
    )
    private List<ChangelogSourceLink> changelogSourceLinks = new ArrayList<>();

    @Transient
    private Date lastLinked;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">

    public Source() {

    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SourceName getSourceName() {
        return sourceName;
    }

    public void setSourceName(SourceName sourceName) {
        this.sourceName = sourceName;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }

    public List<SourceAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SourceAttachment> attachments) {
        this.attachments = attachments;
    }

    public List<ChangelogSourceLink> getChangelogSourceLinks() {
        return changelogSourceLinks;
    }

    public void setChangelogSourceLinks(List<ChangelogSourceLink> changelogSourceLinks) {
        this.changelogSourceLinks = changelogSourceLinks;
    }

    @JsonView(View.Summary.class)
    public Date getLastLinked() {
        return lastLinked;
    }

    public void setLastLinked(Date lastLinked) {
        this.lastLinked = lastLinked;
    }

    protected JSONSerializer getSearchSerializer() {
        return new JSONSerializer()
                .include("id")
                .include("name")
                .include("description")
                .include("url")
                .include("sourceName.*")
                .include("created")
                .include("createdUser.*")
                .include("updated")
                .include("updatedUser.*")
                .include("partIds")
                .exclude("*.class");
    }

    @PostRemove
    @Override
    public void removeSearchIndex() throws Exception {
        log.info("Removing from search index.");
        SearchService.instance().deleteChangelogSource(this);
    }

    @PostPersist
    @PostUpdate
    @Override
    public void updateSearchIndex() throws Exception {
        log.info("Updating search index.");
        SearchService.instance().indexChangelogSource(this);
    }

    @Override
    public void beforeIndexing() {
    }

    @Override
    public String toSearchJson(List<CriticalDimension> criticalDimensions) {
        return getSearchSerializer().exclude("*").serialize(this);
    }

    @Override
    public String getSearchId() {
        return id.toString();
    }

    //</editor-fold>

}
