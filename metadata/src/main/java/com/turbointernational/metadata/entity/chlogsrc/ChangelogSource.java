package com.turbointernational.metadata.entity.chlogsrc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/18/17.
 */
@Entity
@Table(name = "changelog_source")
@NamedQueries({
        @NamedQuery(
                name = "getChangelogSourceCountForSource",
                query = "select count(*) from ChangelogSource chs where chs.pk.source.id=:srcId")
})
@JsonInclude(ALWAYS)
public class ChangelogSource implements Serializable {

    @EmbeddedId
    @JsonView(View.Summary.class)
    private ChangelogSourceId pk;

    @JsonView(View.Summary.class)
    @Column(name = "rating")
    private Integer rating;

    public ChangelogSource() {
    }

    public ChangelogSource(ChangelogSourceId pk, Integer rating) {
        this.pk = pk;
        this.rating = rating;
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

    public Integer getRating() {
        return rating;
    }

    public void setRaiting(Integer rating) {
        this.rating = rating;
    }

}
