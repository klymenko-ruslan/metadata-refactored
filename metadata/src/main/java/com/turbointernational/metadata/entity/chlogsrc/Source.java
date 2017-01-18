package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonView;
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

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/12/17.
 */
@Entity
@Table(name = "source")
@NamedQueries(
    @NamedQuery(name = "findChangelogSourceByName", query = "from Source s where s.name=:name")
)
public class Source implements SearchableEntity, Serializable {

    private final static Logger log = LoggerFactory.getLogger(Source.class);

    //<editor-fold defaultstate="collapsed" desc="properties">

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
    private Date created;

    @JsonView(View.Summary.class)
    @Column(name = "updated")
    private Date updated;

    @JsonView(View.Detail.class)
    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private User createUser;

    @JsonView(View.Detail.class)
    @ManyToOne
    @JoinColumn(name = "update_user_id")
    private User updateUser;

    @JsonView({View.Detail.class})
    @OneToMany(mappedBy = "pk.source", fetch = LAZY, cascade = ALL)
    private List<ChangelogSource> changelogSources = new ArrayList<>();

    public Source() {
    }

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

    public List<ChangelogSource> getChangelogSources() {
        return changelogSources;
    }

    public void setChangelogSources(List<ChangelogSource> changelogSources) {
        this.changelogSources = changelogSources;
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
        // Nothing.
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
