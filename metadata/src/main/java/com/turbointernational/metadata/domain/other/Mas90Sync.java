package com.turbointernational.metadata.domain.other;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.web.View;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/12/16.
 */
@Cacheable
@Entity
@Table(name = "MAS90SYNC")
public class Mas90Sync implements Serializable {

    /**
     * Status of the sync.process.
     */
    public enum Status {IN_PROGRESS, CANCELLED, FINISHED};

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
    @Enumerated(EnumType.STRING)
    @JsonView({View.Summary.class})
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

/*
create table mas90sync(
    id bigint(20) not null auto_increment,
    started timestamp not null comment 'When the sync.process started.',
    finished timestamp comment 'When the sync.process finished.',
    to_process bigint(20) default 0 comment 'Total number of records to process.',
    updated bigint(20) default 0 comment 'Number of updates.',
    inserted bigint(20) default 0 comment 'Number of inserts.',
    skipped bigint(20) default 0 comment 'Number of skipped items.',
    user_id bigint(20) default null comment 'Ref. to an user who initiated the sync.process. NULL -- the process started by scheduler.',
    status enum('IN_PROGRESS', 'CANCELLED', 'FINISHED') not null comment 'Status of the sync.process.',
    primary key (id),
    constraint usrid_fk foreign key (user_id) references user(id)
) engine=InnoDB auto_increment=1000 default charset=utf8 comment 'History of the syncronizations with MAS90.';


insert into mas90sync(id, started, finished, to_process, updated, inserted, user_id, status) values
(10, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'CANCELLED'),
(11, '2015-01-13 09:31:11', '2015-01-13 09:36:27', 978, 270, 708, 8, 'FINISHED'),
(12, '2015-01-13 09:44:01', '2015-01-13 09:45:42', 970, 270, 700, NULL, 'FINISHED'),
(13, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(14, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(15, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(16, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(17, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(18, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(19, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(20, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(21, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(22, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(23, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(24, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(25, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(26, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(27, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(28, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(29, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(30, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(31, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(32, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(33, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(34, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(35, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(36, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(37, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(38, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(39, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(40, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(41, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(42, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(43, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(44, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(45, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(46, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(47, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(48, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(49, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(50, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(51, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(52, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(53, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(54, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(55, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(56, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(57, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(58, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(59, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(60, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(61, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(62, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(63, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(64, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(65, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(66, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(67, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(68, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(69, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(70, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(71, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, 8, 'FINISHED'),
(72, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'FINISHED'),
(73, '2015-01-13 08:11:17', '2015-01-13 08:16:14', 978, 270, 708, NULL, 'IN_PROGRESS');

 */