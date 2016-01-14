package com.turbointernational.metadata.services;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.other.Mas90Sync;
import com.turbointernational.metadata.domain.other.Mas90SyncDao;
import com.turbointernational.metadata.web.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.concurrent.Future;

/**
 * Created by dmytro.trunykov@zorallabs.com on 1/14/16.
 */
@Component
public class Mas90Synchronizer {

    private final Logger log = LoggerFactory.getLogger(Mas90Synchronizer.class);

    public static class SyncProcessStatus implements Cloneable {

        private final static long DEF_PARTSUPDATE_TOTAL_STEPS = 0L;
        private final static long DEF_PARTSUPDATE_CURRENT_STEP = 0L;
        private final static long DEF_PARTSUPDATE_INSERTS = 0L;
        private final static long DEF_PARTSUPDATE_UPDATES = 0L;
        // By default we assume that finished == true.
        // It is important (see JS logic).
        private final static boolean DEF_PARTSUPDATE_FINISHED = Boolean.TRUE;

        @JsonView({View.Summary.class})
        private long partsUpdateTotalSteps;

        @JsonView({View.Summary.class})
        private long partsUpdateCurrentStep;


        @JsonView({View.Summary.class})
        private long partsUpdateInserts;

        @JsonView({View.Summary.class})
        private long partsUpdateUpdates;

        @JsonView({View.Summary.class})
        private boolean finished;

        SyncProcessStatus() {
            reset();
        }

        void reset() {
            this.partsUpdateTotalSteps = DEF_PARTSUPDATE_TOTAL_STEPS;
            this.partsUpdateCurrentStep = DEF_PARTSUPDATE_CURRENT_STEP;
            this.partsUpdateInserts = DEF_PARTSUPDATE_INSERTS;
            this.partsUpdateUpdates = DEF_PARTSUPDATE_UPDATES;
            this.finished = DEF_PARTSUPDATE_FINISHED;
        }

        public long getPartsUpdateTotalSteps() {
            return partsUpdateTotalSteps;
        }

        public void setPartsUpdateTotalSteps(long partsUpdateTotalSteps) {
            this.partsUpdateTotalSteps = partsUpdateTotalSteps;
        }

        public long getPartsUpdateCurrentStep() {
            return partsUpdateCurrentStep;
        }

        public void setPartsUpdateCurrentStep(long partsUpdateCurrentStep) {
            this.partsUpdateCurrentStep = partsUpdateCurrentStep;
        }

        public long getPartsUpdateInserts() {
            return partsUpdateInserts;
        }

        public void setPartsUpdateInserts(long partsUpdateInserts) {
            this.partsUpdateInserts = partsUpdateInserts;
        }

        public long getPartsUpdateUpdates() {
            return partsUpdateUpdates;
        }

        public void setPartsUpdateUpdates(long partsUpdateUpdates) {
            this.partsUpdateUpdates = partsUpdateUpdates;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public Object clone() throws CloneNotSupportedException {
            SyncProcessStatus retVal = new SyncProcessStatus();
            retVal.partsUpdateCurrentStep = partsUpdateCurrentStep;
            retVal.partsUpdateTotalSteps = partsUpdateTotalSteps;
            retVal.partsUpdateInserts = partsUpdateInserts;
            retVal.partsUpdateUpdates = partsUpdateUpdates;
            retVal.finished = finished;
            return retVal;
        }

        @Override
        public String toString() {
            return "SyncProcessStatus{" +
                    "partsUpdateTotalSteps=" + partsUpdateTotalSteps +
                    ", partsUpdateCurrentStep=" + partsUpdateCurrentStep +
                    ", partsUpdateInserts=" + partsUpdateInserts +
                    ", partsUpdateUpdates=" + partsUpdateUpdates +
                    ", finished=" + finished +
                    '}';
        }
    }

    JdbcTemplate mssqldb;

    @Autowired
    private Mas90SyncDao mas90SyncDao;

    SyncProcessStatus syncProcessStatus;

    Future<Mas90Sync> syncProcess;

    /**
     * This method is needed to begin a separate transaction.
     *
     * @return
     */
    @Async
    @Transactional
    public Future<Void> _start(Mas90Sync record) {
        // TODO: implementation
        log.info("_start: {}", 1);
//        synchronized (syncProcessStatus) {
            syncProcessStatus.setPartsUpdateTotalSteps(12);
//        }
        log.info("_start: {}", 2);
        for (int i = 0; i < syncProcessStatus.getPartsUpdateTotalSteps(); i++) {
            log.info("Synchronization: {}", i);
            try {
                Thread.sleep(1000L);
                synchronized (syncProcessStatus) {
                    syncProcessStatus.setPartsUpdateCurrentStep(i);
                }
            } catch(InterruptedException e) {
                log.warn("Unexpected interruption.", e);
            }
        }
        long updated, inserted;
        synchronized (syncProcessStatus) {
            updated = syncProcessStatus.getPartsUpdateUpdates();
            inserted = syncProcessStatus.getPartsUpdateInserts();
        }
        syncProcessStatus.setFinished(true);
        record.setStatus(Mas90Sync.Status.FINISHED);
        record.setUpdated(updated);
        record.setInserted(inserted);
        mas90SyncDao.merge(record);
        log.info("Synchronization process finished.");
        return new AsyncResult<>(null);
    }
}
