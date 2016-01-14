package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.other.Mas90Sync;
import com.turbointernational.metadata.domain.other.Mas90SyncDao;
import com.turbointernational.metadata.domain.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Timestamp;

/**
 * Synchronization service between Mas90 and 'metadata' database.
 *
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Service
public class Mas90SyncService {

    private final Logger log = LoggerFactory.getLogger(Mas90SyncService.class);

    @Autowired
    private Mas90Synchronizer mas90Synchronizer;

    @Autowired
    private Mas90SyncDao mas90SyncDao;

    @Qualifier("dataSourceMas90")
    @Autowired
    DataSource dataSourceMas90;

    @PostConstruct
    public void init() {
        mas90Synchronizer.mssqldb = new JdbcTemplate(dataSourceMas90);
        mas90Synchronizer.syncProcessStatus = new Mas90Synchronizer.SyncProcessStatus();
        mas90Synchronizer.syncProcess = null;
     }

    public Mas90SyncDao.Page history(int startPosition, int maxResults) {
        return mas90SyncDao.findHistory(startPosition, maxResults);
    }

    public Mas90Synchronizer.SyncProcessStatus status() {
        synchronized (mas90Synchronizer.syncProcessStatus) {
            try {
                return (Mas90Synchronizer.SyncProcessStatus) mas90Synchronizer.syncProcessStatus.clone();
            } catch (CloneNotSupportedException e) {
                // can't be reached
                log.error("Unexpected internal error.", e);
                return null;
            }
        }
    }

    public Mas90Synchronizer.SyncProcessStatus start(User user) {
        if (!mas90Synchronizer.syncProcessStatus.isFinished()) {
            throw new IllegalStateException("New 'sync. process' can't be started because exists other process.");
        }
        long now = System.currentTimeMillis();
        synchronized (mas90Synchronizer.syncProcessStatus) {
            mas90Synchronizer.syncProcessStatus.reset();
        }
        Mas90Sync record = new Mas90Sync();
        record.setStarted(new Timestamp(now));
        record.setInserted(0L);
        record.setUpdated(0L);
        record.setToProcess(0L);
        record.setUser(user);
        record.setStatus(Mas90Sync.Status.IN_PROGRESS);
        mas90SyncDao.persist(record);
        synchronized (mas90Synchronizer.syncProcessStatus) {
            mas90Synchronizer.syncProcessStatus.setFinished(false);
        }
        mas90Synchronizer._start(record);
        return status();
    }


}
/*
im.ITEMCODE     varchar(30)
im.ITEMCODEDESC varchar(30)

im.productLine  varchar(4)
im.producttype  varchar(1)


manfr_part_num  rtrim(im.ITEMCODE)
manfr_id        11
part_type_id    @part_type_id => productLine_to_parttype_value[part_type_value = part_type.value] => pt.id
inactive        case when im.producttype = 'D' then 1 else 0
description     im.ITEMCODEDESC
 */
