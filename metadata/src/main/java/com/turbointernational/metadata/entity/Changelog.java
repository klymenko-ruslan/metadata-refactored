package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author jrodriguez
 */
@Entity
@Table(name = "changelog")
@NamedQueries({
    @NamedQuery(
            name = "findChangelogsForPart",
            query = "select c " +
                    "from Changelog c " +
                    "join c.changelogParts p " +
                    "where p.part.id=:partId " +
                    "order by c.id asc")
})
public class Changelog implements Serializable {

    public enum ServiceEnum {
        BOM, INTERCHANGE, MAS90SYNC, SALESNOTES, APPLICATIONS, KIT, PART, TURBOMODEL, TURBOTYPE
    }

    //<editor-fold defaultstate="collapsed" desc="Properties">

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id")
    @JsonView(View.Summary.class)
    private Long id;

    @Column(name = "service")
    @Enumerated(STRING)
    @JsonView(View.Summary.class)
    private ServiceEnum service;

    @Temporal(TIMESTAMP)
    @Column(name = "change_date", nullable = false)
    @JsonView(View.Summary.class)
    private Date changeDate;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(View.Summary.class)
    private User user;

    @Column(name = "description", nullable = false)
    @JsonView(View.Summary.class)
    private String description;

    @Lob
    @Column(name = "data", length = 4096)
    @JsonView(View.Summary.class)
    private String data;

    @OneToMany(mappedBy = "changelog", fetch = LAZY)
    @JsonView(View.Detail.class)
    private List<ChangelogPart> changelogParts = new ArrayList<>();

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceEnum getService() {
        return service;
    }

    public void setService(ServiceEnum service) {
        this.service = service;
    }

    /**
     * @return the changeDate
     */
    public Date getChangeDate() {
        return changeDate;
    }

    /**
     * @param changeDate the changeDate to set
     */
    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    public List<ChangelogPart> getChangelogParts() {
        return changelogParts;
    }

    public void setChangelogParts(List<ChangelogPart> changelogParts) {
        this.changelogParts = changelogParts;
    }

    //</editor-fold>

}
