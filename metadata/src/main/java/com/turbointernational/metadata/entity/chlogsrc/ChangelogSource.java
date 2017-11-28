package com.turbointernational.metadata.entity.chlogsrc;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/18/17.
 */
@Entity
@Table(name = "changelog_source")
@NamedQueries({
        @NamedQuery(
                name = "getChangelogSourceCountForSource",
                query = "select count(chs) from ChangelogSource chs where chs.pk.source.id=:srcId")
})
@JsonInclude(ALWAYS)
public class ChangelogSource implements Serializable {

    private static final long serialVersionUID = 8496565317943617098L;

    //<editor-fold defaultstate="collapsed" desc="Properties">

    @EmbeddedId
    @JsonView(View.Summary.class)
    private ChangelogSourceId pk;

    @JsonView(View.Summary.class)
    @Column(name = "rating")
    private Integer rating;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">

    public ChangelogSource() {
    }

    public ChangelogSource(ChangelogSourceId pk, Integer rating) {
        this.pk = pk;
        this.rating = rating;
    }

    public ChangelogSource(ChangelogSourceId pk) {
        this.pk = pk;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">

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

    //</editor-fold>

}
