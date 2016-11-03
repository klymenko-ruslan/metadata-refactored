package com.turbointernational.metadata.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jrodriguez
 */
@Entity
@Table(name = "changelog")
public class Changelog implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="properties">
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonView(View.Summary.class)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
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
    @Column(name = "data")
    @JsonView(View.Summary.class)
    private String data;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    //</editor-fold>

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
}
