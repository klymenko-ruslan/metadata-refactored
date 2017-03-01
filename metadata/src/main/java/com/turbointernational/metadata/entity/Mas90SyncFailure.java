package com.turbointernational.metadata.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 11/9/16.
 */
@Cacheable
@Entity
@Table(name = "mas90sync_failure")
public class Mas90SyncFailure {

    //<editor-fold defaultstate="collapsed" desc="Properties">

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @JsonView({View.Summary.class})
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JsonView({View.Summary.class})
    @JsonBackReference
    @JoinColumn(name="mas90sync_id", nullable = false)
    private Mas90Sync mas90Sync;

    @JsonView({View.Summary.class})
    @Column(name = "log")
    private String log;

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mas90Sync getMas90Sync() {
        return mas90Sync;
    }

    public void setMas90Sync(Mas90Sync mas90Sync) {
        this.mas90Sync = mas90Sync;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    //</editor-fold>

}
