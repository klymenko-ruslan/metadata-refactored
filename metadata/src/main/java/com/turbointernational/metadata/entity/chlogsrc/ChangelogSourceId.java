package com.turbointernational.metadata.entity.chlogsrc;

import com.turbointernational.metadata.entity.Changelog;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/18/17.
 */
@Embeddable
public class ChangelogSourceId implements Serializable {

    @ManyToOne
    private Changelog changelog;

    @ManyToOne
    private Source source;

    public ChangelogSourceId() {
    }

    public ChangelogSourceId(Changelog changelog, Source source) {
        this.changelog = changelog;
        this.source = source;
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

        if (!changelog.equals(that.changelog)) return false;
        return source.equals(that.source);
    }

    @Override
    public int hashCode() {
        int result = changelog.hashCode();
        result = 31 * result + source.hashCode();
        return result;
    }

}
