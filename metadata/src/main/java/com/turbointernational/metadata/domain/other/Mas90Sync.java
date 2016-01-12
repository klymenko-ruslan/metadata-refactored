package com.turbointernational.metadata.domain.other;

import com.turbointernational.metadata.domain.security.User;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/12/16.
 */
@Cacheable
@Entity
@Table(name = "MAS90SYNC")
public class Mas90Sync {

    /**
     * Status of the sync.process.
     */
    private enum Status {IN_PROGRESS, CANCELLED, FINISHED};

    //<editor-fold defaultstate="collapsed" desc="Properties">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * When the sync.process started.
     */
    @Column(name = "started", nullable = false)
    private Timestamp started;

    /**
     * When the sync.process finished.
     */
    @Column(name = "finished")
    private Timestamp finished;

    /**
     * Total number of records to process.
     */
    @Column(name = "to_process")
    private Long toProcess;

    /**
     * Number of updates.
     */
    @Column(name = "updated")
    private Long updated;

    /**
     * Number of inserts.
     */
    @Column(name = "inserted")
    private Long inserted;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Status of the sync.process.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
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

}
