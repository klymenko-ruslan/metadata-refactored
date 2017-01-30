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
@NamedQueries(
        @NamedQuery(
                name = "getChangelogSourceCountForSource",
                query = "select count(*) from ChangelogSource chs where chs.pk.source.id=:srcId")
)
@JsonInclude(ALWAYS)
public class ChangelogSource implements Serializable {

    @EmbeddedId
    @JsonView(View.Summary.class)
    private ChangelogSourceId pk;

    @JsonView(View.Summary.class)
    @Column(name = "raiting")
    private Integer raiting;

    public ChangelogSource() {
    }

    public ChangelogSource(ChangelogSourceId pk, Integer raiting) {
        this.pk = pk;
        this.raiting = raiting;
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

    public Integer getRaiting() {
        return raiting;
    }

    public void setRaiting(Integer raiting) {
        this.raiting = raiting;
    }

}
