package com.turbointernational.metadata.entity;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/12/16.
 */
@Cacheable
@Entity
@Table(name = "mas90sync")
public class Mas90Sync implements Serializable {

    private static final long serialVersionUID = -3544673650029676307L;

    /**
     * Status of the sync.process.
     */
    public enum Status {
        IN_PROGRESS, CANCELLED, FINISHED, FAILED
    }

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Summary.class})
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * When the sync.process started.
     */
    @JsonView({View.Summary.class})
    @Column(name = "started", nullable = false)
    private Timestamp started;

    /**
     * When the sync.process finished.
     */
    @JsonView({View.Summary.class})
    @Column(name = "finished")
    private Timestamp finished;

    /**
     * Total number of records to process.
     */
    @JsonView({View.Summary.class})
    @Column(name = "to_process")
    private Long toProcess;

    /**
     * Number of updates.
     */
    @JsonView({View.Summary.class})
    @Column(name = "updated")
    private Long updated;

    /**
     * Number of inserts.
     */
    @JsonView({View.Summary.class})
    @Column(name = "inserted")
    private Long inserted;

    /**
     * Number of skipped items.
     */
    @JsonView({View.Summary.class})
    @Column(name = "skipped")
    private Long skipped;

    @OneToOne
    @JsonView({View.Summary.class})
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Status of the sync.process.
     */
    @Enumerated(STRING)
    @JsonView({View.Summary.class})
    @Column(name = "status")
    private Status status;

    @JsonView({View.Detail.class})
    @JsonInclude(ALWAYS)
    @OneToMany(fetch = LAZY, mappedBy = "mas90Sync", cascade = ALL)
    private List<Mas90SyncFailure> failures = new ArrayList<>();

    @JsonView({View.Detail.class})
    @JsonInclude(ALWAYS)
    @OneToMany(fetch = LAZY, mappedBy = "mas90Sync", cascade = ALL)
    private List<Mas90SyncSuccess> successes = new ArrayList<>();

    //</editor-fold>

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Timestamp getStarted() {
        return started;
    }

    public void setStarted(Timestamp started) {
        this.started = started;
    }

    public Timestamp getFinished() {
        return finished;
    }

    public void setFinished(Timestamp finished) {
        this.finished = finished;
    }

    public Long getToProcess() {
        return toProcess;
    }

    public void setToProcess(Long toProcess) {
        this.toProcess = toProcess;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Long getInserted() {
        return inserted;
    }

    public void setInserted(Long inserted) {
        this.inserted = inserted;
    }

    public Long getSkipped() {
        return skipped;
    }

    public void setSkipped(Long skipped) {
        this.skipped = skipped;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Mas90SyncFailure> getFailures() {
        return failures;
    }

    public void setFailures(List<Mas90SyncFailure> failures) {
        this.failures = failures;
    }

    public List<Mas90SyncSuccess> getSuccesses() {
        return successes;
    }

    public void setSuccesses(List<Mas90SyncSuccess> successes) {
        this.successes = successes;
    }

}
