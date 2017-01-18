package com.turbointernational.metadata.entity.chlogsrc;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/18/17.
 */
@Entity
@Table(name = "changelog_source")
@AssociationOverrides({
        @AssociationOverride(name = "pk.changelog",
                joinColumns = @JoinColumn(name = "chnagelog_id")),
        @AssociationOverride(name = "pk.source",
                joinColumns = @JoinColumn(name = "source_id"))})
public class ChangelogSource implements Serializable {

    @EmbeddedId
    private ChangelogSourceId pk;

    @Column(name = "description")
    private String description;

    @Column(name = "raiting")
    private Integer raiting;

    public ChangelogSource() {

    }

    public ChangelogSource(ChangelogSourceId pk) {
        this.pk = pk;
    }

    public ChangelogSourceId getPk() {
        return pk;
    }

    public void setPk(ChangelogSourceId pk) {
        this.pk = pk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRaiting() {
        return raiting;
    }

    public void setRaiting(Integer raiting) {
        this.raiting = raiting;
    }

}
