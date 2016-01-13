package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.other.Mas90SyncDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Synchronization service between Mas90 and 'metadata' database.
 *
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Service
public class Mas90SyncService {

    public static class SyncProcessStatus {

    }

    private class SyncProcess extends Thread {

        private boolean stopped;

        private final int numInterations;
        private final long iterationPeriod;

        SyncProcess(int numIterations, long iterationPeriod) {
            this.numInterations = numIterations;
            this.iterationPeriod = iterationPeriod;
        }

        @Override
        public void run() {
            for (int i = 0; !stopped && i < numInterations; i++) {

            }
        }

    }

    private JdbcTemplate mssqldb;

    @Qualifier("dataSourceMas90")
    @Autowired
    private DataSource dataSourceMas90;

    @Autowired
    private Mas90SyncDao mas90SyncDao;

    @PostConstruct
    public void init() {
        mssqldb = new JdbcTemplate(dataSourceMas90);
    }

    public Mas90SyncDao.Page history(int startPosition, int maxResults) {
        return mas90SyncDao.findHistory(startPosition, maxResults);
    }

    public void start() {

    }

}
