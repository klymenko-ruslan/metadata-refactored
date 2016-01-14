package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.other.Mas90Sync;
import com.turbointernational.metadata.domain.other.Mas90SyncDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Synchronization service between Mas90 and 'metadata' database.
 *
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Service
public class Mas90SyncService {

    private final Logger log = LoggerFactory.getLogger(Mas90SyncService.class);

    public static class SyncProcessStatus {
        final Mas90Sync record;
        final int totalSteps;

        AtomicInteger currentStep;

        SyncProcessStatus(Mas90Sync record, int totalSteps) {
            this.record = record;
            this.totalSteps = totalSteps;
            currentStep.set(0);
        }
    }

    private class SyncProcess extends Thread {

        private final SyncProcessStatus status;

        SyncProcess(SyncProcessStatus status) {
            this.status = status;
        }

        @Override
        public void run() {
            for (int i = 0; i < status.totalSteps; i++) {
                synchronized (status) {
                    if (status.record.getStatus() == Mas90Sync.Status.CANCELLED) {
                        break;
                    }
                }
                log.info("Synchronization: {}", i);
                try {
                    status.currentStep.incrementAndGet();
                    Thread.sleep(1000L);
                } catch(InterruptedException e) {
                    log.warn("Unexpected interruption.", e);
                }
            }
            log.info("Synchronization process finished with status: {}", status.record.getStatus());
        }

    }

    private JdbcTemplate mssqldb;

    @Qualifier("dataSourceMas90")
    @Autowired
    private DataSource dataSourceMas90;

    @Autowired
    private Mas90SyncDao mas90SyncDao;

    private SyncProcess syncProcess = null;

    @PostConstruct
    public void init() {
        mssqldb = new JdbcTemplate(dataSourceMas90);
    }

    public Mas90SyncDao.Page history(int startPosition, int maxResults) {
        return mas90SyncDao.findHistory(startPosition, maxResults);
    }

    public void start() {
        if (syncProcess != null && syncProcess.isAlive()) {
            throw new IllegalStateException("New 'sync.process' can't be started because exists other process.");
        }
        long now = System.currentTimeMillis();
        Mas90Sync record = new Mas90Sync();
        record.setStarted(new Timestamp(now));
        record.setInserted(0L);
        record.setUpdated(0L);
        record.setToProcess(0L);
        record.setStatus(Mas90Sync.Status.IN_PROGRESS);
        mas90SyncDao.persist(record);

    }

}
