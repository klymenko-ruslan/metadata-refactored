package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.util.View;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

import static javax.persistence.CascadeType.ALL;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/18/17.
 */
@Embeddable
public class ChangelogSourceId implements Serializable {

    @ManyToOne
    @JsonView(View.Summary.class)
    @JoinColumn(name = "lnk_id", nullable = false)
    private ChangelogSourceLink link;

    @ManyToOne
    @JsonView(View.Summary.class)
    @JoinColumn(name = "changelog_id", nullable = false)
    private Changelog changelog;

    @ManyToOne
    @JsonView(View.Summary.class)
    @JoinColumn(name = "source_id", nullable = false)
    private Source source;

    public ChangelogSourceId() {
    }

    public ChangelogSourceId(ChangelogSourceLink link, Changelog changelog, Source source) {
        this.link = link;
        this.changelog = changelog;
        this.source = source;
    }

    public ChangelogSourceLink getLink() {
        return link;
    }

    public void setLink(ChangelogSourceLink link) {
        this.link = link;
    }

    public Changelog getChangelog() {
        return changelog;
    }

    public void setChangelog(Changelog changelog) {
        this.changelog = changelog;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChangelogSourceId that = (ChangelogSourceId) o;

        if (!link.equals(that.link)) return false;
        if (!changelog.equals(that.changelog)) return false;
        return source.equals(that.source);
    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + changelog.hashCode();
        result = 31 * result + source.hashCode();
        return result;
    }

}
