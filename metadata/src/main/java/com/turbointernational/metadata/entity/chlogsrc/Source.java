package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/12/17.
 */
@Entity
@Table(name = "source")
@NamedQueries(
    @NamedQuery(name = "findChangelogSourceByName", query = "from Source s where s.name=:name")
)
public class Source implements Serializable {

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

    //</editor-fold>

}
