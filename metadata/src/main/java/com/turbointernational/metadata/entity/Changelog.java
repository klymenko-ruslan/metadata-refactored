package com.turbointernational.metadata.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * @author jrodriguez
 */
@Entity
@Table(name = "changelog")
public class Changelog implements Serializable {

    private static final long serialVersionUID = -7760923457980476288L;

    /**
     * Services in this webapp.
     *
     * Order of the elements is important because ordinal order is used
     * to load record by ID from the 'service' table.
     */
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
