package com.turbointernational.metadata.services;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.Mas90Sync;
import com.turbointernational.metadata.domain.other.Mas90SyncDao;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.domain.part.types.*;
import com.turbointernational.metadata.domain.security.User;
import com.turbointernational.metadata.domain.type.PartType;
import com.turbointernational.metadata.domain.type.PartTypeDao;
import com.turbointernational.metadata.web.View;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Synchronization service between Mas90 and 'metadata' database.
 * <p>
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Service
public class Mas90SyncService {

    private final Logger log = LoggerFactory.getLogger(Mas90SyncService.class);

    private final static long TURBO_INTERNATIONAL_MANUFACTURER_ID = 11L;

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private PartDao partDao;

    @Autowired
    private PartTypeDao partTypeDao;

    @Autowired
    private Mas90SyncDao mas90SyncDao;      // local storage

    @Qualifier("dataSourceMas90")
    @Autowired
    private DataSource dataSourceMas90;     // connections to MS-SQL (MAS90)

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;     // connections to MYSQL (metadata)

    private JdbcTemplate mssqldb;

    private JdbcTemplate metadatadb;

    private Mas90Synchronizer syncProcess;

    private SyncProcessStatus syncProcessStatus;

    @PostConstruct
    public void init() {
        mssqldb = new JdbcTemplate(dataSourceMas90);
        metadatadb = new JdbcTemplate(dataSource);
        syncProcessStatus = new SyncProcessStatus();
        syncProcess = null;
    }

    public static class SyncProcessStatus implements Cloneable {

        private final static Long DEF_STARTED_ON = null;
        private final static Long DEF_USER_ID = null;
        private final static String DEF_USER_NAME = null;
        private final static long DEF_PARTSUPDATE_TOTAL_STEPS = 0L;
        private final static long DEF_PARTSUPDATE_CURRENT_STEP = 0L;
        private final static long DEF_PARTSUPDATE_INSERTS = 0L;
        private final static long DEF_PARTSUPDATE_UPDATES = 0L;
        private final static long DEF_PARTSUPDATE_SKIPPED = 0L;
         // By default we assume that finished == true.
        // It is important (see JS logic).
        private final static boolean DEF_PARTSUPDATE_FINISHED = Boolean.TRUE;

        @JsonView({View.Summary.class})
        private Long startedOn;

        @JsonView({View.Summary.class})
        private Long userId;

        @JsonView({View.Summary.class})
        private String userName;

        @JsonView({View.Summary.class})
        private long partsUpdateTotalSteps;

        @JsonView({View.Summary.class})
        private long partsUpdateCurrentStep;

        @JsonView({View.Summary.class})
        private long partsUpdateInserts;

        @JsonView({View.Summary.class})
        private long partsUpdateUpdates;

        @JsonView({View.Summary.class})
        private long partsUpdateSkipped;

        @JsonView({View.Summary.class})
        private boolean finished;

        @JsonView({View.Summary.class})
        private List<String> errors = new ArrayList<>();

        SyncProcessStatus() {
            reset();
        }

        public Long getStartedOn() {
            return startedOn;
        }

        public void setStartedOn(Long startedOn) {
            this.startedOn = startedOn;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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

        public long incPartsUpdateCurrentStep() {
            return ++this.partsUpdateCurrentStep;
        }

        public long getPartsUpdateInserts() {
            return partsUpdateInserts;
        }

        public void setPartsUpdateInserts(long partsUpdateInserts) {
            this.partsUpdateInserts = partsUpdateInserts;
        }

        public long incPartsUpdateInserts() {
            return ++partsUpdateInserts;
        }

        public long getPartsUpdateUpdates() {
            return partsUpdateUpdates;
        }

        public void setPartsUpdateUpdates(long partsUpdateUpdates) {
            this.partsUpdateUpdates = partsUpdateUpdates;
        }

        public long incPartsUpdateUpdates() {
            return ++partsUpdateUpdates;
        }

        public long getPartsUpdateSkipped() {
            return partsUpdateSkipped;
        }

        public void setPartsUpdateSkipped(long partsUpdateSkipped) {
            this.partsUpdateSkipped = partsUpdateSkipped;
        }

        public long incPartsUpdateSkipped() {
            return ++partsUpdateSkipped;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }

        public List<String> getErrors() {
            return errors;
        }

        public void setErrors(List<String> errors) {
            this.errors = errors;
        }

        public void addError(String error) {
            errors.add(error);
        }

        public void resetErrors() {
            errors.clear();
        }

        void reset() {
            this.startedOn = DEF_STARTED_ON;
            this.userId = DEF_USER_ID;
            this.userName = DEF_USER_NAME;
            this.partsUpdateTotalSteps = DEF_PARTSUPDATE_TOTAL_STEPS;
            this.partsUpdateCurrentStep = DEF_PARTSUPDATE_CURRENT_STEP;
            this.partsUpdateInserts = DEF_PARTSUPDATE_INSERTS;
            this.partsUpdateUpdates = DEF_PARTSUPDATE_UPDATES;
            this.partsUpdateSkipped = DEF_PARTSUPDATE_SKIPPED;
            this.errors.clear();
            this.finished = DEF_PARTSUPDATE_FINISHED;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            SyncProcessStatus retVal = new SyncProcessStatus();
            retVal.startedOn = startedOn;
            retVal.userId = userId;
            retVal.userName = userName;
            retVal.partsUpdateCurrentStep = partsUpdateCurrentStep;
            retVal.partsUpdateTotalSteps = partsUpdateTotalSteps;
            retVal.partsUpdateInserts = partsUpdateInserts;
            retVal.partsUpdateUpdates = partsUpdateUpdates;
            retVal.partsUpdateSkipped = partsUpdateSkipped;
            retVal.errors = new ArrayList<>(errors);
            retVal.finished = finished;
            return retVal;
        }

        @Override
        public String toString() {
            return "SyncProcessStatus{" +
                    "startedOn=" + startedOn +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    ", partsUpdateTotalSteps=" + partsUpdateTotalSteps +
                    ", partsUpdateCurrentStep=" + partsUpdateCurrentStep +
                    ", partsUpdateInserts=" + partsUpdateInserts +
                    ", partsUpdateUpdates=" + partsUpdateUpdates +
                    ", partsUpdateSkipped=" + partsUpdateSkipped +
                    ", finished=" + finished +
                    ", errors=" + errors +
                    '}';
        }

    }

    class Mas90Synchronizer extends Thread {

        private final Logger log = LoggerFactory.getLogger(Mas90Synchronizer.class);

        private final Mas90Sync record;

        private Mas90Synchronizer(Mas90Sync record) {
            this.record = record;
        }

        /**
         * This method is needed to begin a separate transaction.
         *
         * @return
         */
        @Override
        public void run() {
            TransactionTemplate transaction = new TransactionTemplate(txManager);
            transaction.execute((TransactionCallback<Void>) transactionStatus -> {
                log.info("Started synchronization with MAS90.");
                long numItems = mssqldb.queryForObject("select count(*) " +
                        " from ci_item as im join productLine_to_parttype_value as t2 " +
                        " on im.productline = t2.productLineCode where " +
                        " im.itemcode like '[0-9]-[a-z]-[0-6][0-9][0-9][0-9]' or " +
                        " im.itemcode like '[0-9][0-9]-[a-z]-[0-6][0-9][0-9][0-9]'", Long.class);
                synchronized (syncProcessStatus) {
                    syncProcessStatus.setPartsUpdateTotalSteps(numItems + 2); // +2 to load part_types and #bom
                }
                Map<String, Long> mas90toLocal = loadPartTypesMap(); // part type value => part type ID
                synchronized (syncProcessStatus) {
                    syncProcessStatus.incPartsUpdateCurrentStep();
                }
                loadLatestBom();
                synchronized (syncProcessStatus) {
                    syncProcessStatus.incPartsUpdateCurrentStep();
                }
                try {
                    mssqldb.query("select itemcode, itemcodedesc, productline, producttype  " +
                            " from ci_item as im join productLine_to_parttype_value as t2 " +
                            " on im.productline = t2.productLineCode where " +
                            " itemcode like '[0-9]-[a-z]-[0-6][0-9][0-9][0-9]' or " +
                            " itemcode like '[0-9][0-9]-[a-z]-[0-6][0-9][0-9][0-9]'", rs -> {
                        String itemcode = rs.getString(1);
                        String itemcodedesc = rs.getString(2);
                        String productline = rs.getString(3);
                        String producttype = rs.getString(4);
                        Long partTypeId = mas90toLocal.get(productline);
                        if (partTypeId != null) {
                            long partId = processPart(itemcode, itemcodedesc, producttype, partTypeId); // separate transaction
                            if (partId != -1) {
                                processBOM(partId, itemcode); // separate transaction
                            }
                        } else {
                            // Actually this should never happen because we joined ci_item
                            // with productLine_to_parttype_value.
                            log.warn("Can't convert productline ('{}') to product_type_id. Item with code '{}' skipped.",
                                    productline, itemcode);
                        }
                        synchronized (syncProcessStatus) {
                            syncProcessStatus.incPartsUpdateCurrentStep();
                        }
                    });
                } catch (Throwable e) {
                    synchronized (syncProcessStatus) {
                        syncProcessStatus.setFinished(true);
                    }
                    log.error("Synchronization with MAS90 failed.", e);
                    transactionStatus.setRollbackOnly();
                    return null;
                }
                long total, updated, inserted, skipped;
                synchronized (syncProcessStatus) {
                    total = syncProcessStatus.getPartsUpdateTotalSteps();
                    updated = syncProcessStatus.getPartsUpdateUpdates();
                    inserted = syncProcessStatus.getPartsUpdateInserts();
                    skipped = syncProcessStatus.getPartsUpdateSkipped();
                    syncProcessStatus.setFinished(true);
                }
                record.setStatus(Mas90Sync.Status.FINISHED);
                record.setToProcess(total);
                record.setUpdated(updated);
                record.setInserted(inserted);
                record.setSkipped(skipped);
                mas90SyncDao.merge(record);
                log.info("Finish synchronization with MAS90.");
                return null;
            });

        }

        /**
         * MAS90 use different part type codes than in the local database -- 'metadata'.
         *
         * This method makes mapping between product type codes in MAS90 and 'metadata'.
         *
         * @return map ProductLineCode => part_type_id
         */
        private Map<String, Long> loadPartTypesMap() {
            Map<String, Long> retVal = new HashMap<>(30);
            mssqldb.query("select ProductLineCode, part_type_value from productLine_to_parttype_value", rs -> {
                String productLineCode = rs.getString(1);
                String partTypeValue = rs.getString(2);
                PartType pt = partTypeDao.findPartTypeByValue(partTypeValue);
                if (pt != null) {
                    retVal.put(productLineCode, pt.getId());
                    log.debug("Mapping: {} => {}", productLineCode, pt.getId());
                } else {
                    log.warn("Part type not found for productLineCode: {}", productLineCode);
                }

            });
            return retVal;
        }

        /**
         * Load BOMs of last version from MAS90 to a local table in 'metadata'.
         *
         * Result table is a table #bm2 in the original synchronization script (metadata_update_bom_from_MAS.sql).
         * We load this table in a separate transaction so result will be visible in other transactions.
         */
        private void loadLatestBom() {
            TransactionTemplate tt = new TransactionTemplate(txManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(ts -> {
                metadatadb.execute("delete from mas90sync_bom_revision");
                mssqldb.query("select bd.billno, bd.componentitemcode, bd.quantityperbill " +
                        "from bm_billdetail bd inner join (select bh.billno, max(bh.revision) max_revision " +
                        "from bm_billheader bh group by bh.billno) bv on bd.billno = bv.billno and " +
                        "bd.revision = bv.max_revision", rs -> {
                    String billno = rs.getString(1);
                    String componentitemcode = rs.getString(2);
                    long quantityperbill = rs.getLong(3);
                    metadatadb.execute("insert into mas90sync_bom_revision(billno, componentitemcode, quantityperbill)" +
                            " values(?,?,?)", (PreparedStatementCallback<Void>) ps -> {
                        ps.setString(1, billno);
                        ps.setString(2, componentitemcode);
                        ps.setLong(3, quantityperbill);
                        ps.executeUpdate();
                        return null;
                    });
                });
                return null;
            });

        }

        /**
         * Insert or update a part.
         *
         * This operation is made in a separate transaction.
         * We need a separate transaction to update/insert the Part
         * because different types of exceptions could arise during this operation
         * and we don't wont to rollback a transaction with-in the main loop.
         *
         * @param itemcode
         * @param itemcodedesc
         * @param producttype
         * @param partTypeId
         * @return partId
         */
        private long processPart(String itemcode, String itemcodedesc, String producttype, Long partTypeId) {
            long retVal = -1;
            Boolean inactive = "D".equals(producttype);
            TransactionTemplate modifyTransaction = new TransactionTemplate(txManager);
            modifyTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new transaction
            Long pId = modifyTransaction.execute(ts -> {
                Long partId = null;
                Part part = partDao.findByPartNumberAndManufacturer(TURBO_INTERNATIONAL_MANUFACTURER_ID, itemcode);
                try {
                    if (part == null) {
                        part = insertPart(itemcode, itemcodedesc, partTypeId, inactive);
                        partId = part.getId();
                        log.info("Inserted: {} - {}", part.getId(), part.getManufacturerPartNumber());
                        synchronized (syncProcessStatus) {
                            syncProcessStatus.incPartsUpdateInserts();
                        }
                    } else {
                        if (part.getPartType().getId() == partTypeId) {
                            partId = part.getId();
                            boolean updated = updatePart(part, itemcodedesc, inactive);
                            if (updated) {
                                log.info("Updated: {} - {}", part.getId(), part.getManufacturerPartNumber());
                                synchronized (syncProcessStatus) {
                                    syncProcessStatus.incPartsUpdateUpdates();
                                }
                            }
                        } else {
                            throw new IllegalArgumentException("Updated part has different part type " +
                                    "in MAS90 (" + partTypeId + ") and in 'metadata' (" +
                                    part.getPartType().getId() + ").");
                        }
                    }
                } catch (Exception e) {
                    String cause = ExceptionUtils.getRootCauseMessage(e);
                    String err = "Processing of a part with code '" + itemcode + "' failed. Cause: " + cause;
                    synchronized (syncProcessStatus) {
                        syncProcessStatus.addError(err);
                        syncProcessStatus.incPartsUpdateSkipped();
                    }
                    log.error(err /*, e*/);
                    ts.setRollbackOnly();
                }
                return partId;
            });
            if (pId != null) {
                retVal = pId;
            }
            return retVal;
        }

        private Part insertPart(String itemcode, String itemcodedesc, Long partTypeId, Boolean inactive) {
            Part p;
            if (partTypeId == 1L) {
                p = new Turbo();
            } else if (partTypeId == 2L) {
                p = new Cartridge();
            } else if (partTypeId == 3L) {
                p = new Kit();
            } else if (partTypeId == 4L) {
                p = new PistonRing();
            } else if (partTypeId == 5L) {
                p = new JournalBearing();
            } else if (partTypeId == 6L) {
                p = new Gasket();
            } else if (partTypeId == 7L) {
                p = new BearingSpacer();
            } else if (partTypeId == 11L) {
                p = new CompressorWheel();
            } else if (partTypeId == 12L) {
                p = new TurbineWheel();
            } else  if (partTypeId == 13L) {
                p = new BearingHousing();
            } else if (partTypeId == 14L) {
                p = new Backplate();
            } else if (partTypeId == 15L) {
                p = new Heatshield();
            } else if (partTypeId == 16L) {
                p = new NozzleRing();
            } else {
                p = new Part();
            }
            EntityManager em = partDao.getEntityManager();
            PartType partType = em.getReference(PartType.class, partTypeId);
            p.setManufacturerPartNumber(itemcode);
            Manufacturer manufacturer = em.getReference(Manufacturer.class, TURBO_INTERNATIONAL_MANUFACTURER_ID);
            p.setManufacturer(manufacturer);
            p.setDescription(itemcodedesc);
            p.setPartType(partType);
            p.setInactive(inactive);
            partDao.persist(p);
            return p;
        }

        private boolean updatePart(Part p, String itemcodedesc,  Boolean inactive) {
            boolean updated = false;
            String currDesc = p.getDescription();
            // if (!StringUtils.equals(currDesc, itemcodedesc)) {
            if (currDesc == null && itemcodedesc != null) {
                updated = true;
                log.info("Updated description for: {}. {} => {}", p.getManufacturerPartNumber(), currDesc, itemcodedesc);
                p.setDescription(itemcodedesc);
            }
            Boolean currInactive = p.getInactive();
            if (!ObjectUtils.equals(currInactive, inactive)) {
                updated = true;
                log.info("Updated 'inactive' for: {}. {} => {}", p.getManufacturerPartNumber(), currInactive, inactive);
                p.setInactive(inactive);
            }
            return updated;
        }

        private void processBOM(long partId, String itemcode) {
            log.info("===[BEGIN] itemcode: {} ===", itemcode);
            Part part = partDao.findOne(partId);
            log.info("Part: {}", part);
            Set<BOMItem> boms = part.getBom();
            for (BOMItem bi : boms) {
                log.info("bi: {}" + bi);
            }
            log.info("===[END] itemcode: {} ===", itemcode);
//            TransactionTemplate tt = new TransactionTemplate(txManager);
//            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new transaction
//            tt.execute(ts -> {
//                metadatadb.query(
//                        "select " +
//                        "   bm2.billno, bm2.componentitemcode, bm2.quantityperbill, bom.id bom_id, " +
//                        "   pp.manfr_part_num bom_parent, pc.manfr_part_num bom_child, bom.quantity, " +
//                        "   ifnull(bm2.BILLNO, pp.manfr_part_num) as parent, " +
//                        "   ifnull(bm2.componentitemcode, pc.manfr_part_num) as child, " +
//                        "   case when bm2.quantityperbill <> bom.quantity then 1 else 0 end as qty_mismatch, " +
//                        "   ppp.id metadata_billnumber_part_id, pcc.id metadata_componentitemcode_part_id " +
////                        "   imm.itemcode as mas_billnumber_part_id, immc.itemcode as mas_componentitemcode_part_id " +
//                        "from " +
//                        "   mas90sync_bom_revision as bm2 " +
//                        // -- only get TI parts
//                        //"   inner join #im as im on bm2.BILLNO = im.ITEMCODE " +
//                        //"   inner join #im as imc on bm2.componentitemcode = imc.ITEMCODE " +
//                        // --join against Metadata BOM
//                        "   full outer join " +
//                        "   ( bom " +
//                        // "       #bom as bom " +
//                        "       inner join part as pp on bom.parent_part_id = pp.id and pp.manfr_id = " + TURBO_INTERNATIONAL_MANUFACTURER_ID + " " +
//                        "       inner join part as pc on bom.child_part_id = pc.id and pp.manfr_id = " + TURBO_INTERNATIONAL_MANUFACTURER_ID + " " +
//                        "   ) on bm2.billno = pp.manfr_part_num and bm2.componentitemcode = pc.manfr_part_num " +
//                        // -- check to see if parts are missing from Metadata
//                        "   left join part as ppp on ifnull(bm2.billno, pp.manfr_part_num) = ppp.manfr_part_num " +
//                        "   left join part as pcc on ifnull(bm2.componentitemcode, pc.manfr_part_num) = pcc.manfr_part_num " +
//                        // -- check to see if parts are missing from MAS
////                        "   left join #im as imm on imm.ITEMCODE = ifnull(bm2.BILLNO, pp.manfr_part_num) " +
////                        "   left join #im as immc on immc.ITEMCODE = ifnull(bm2.componentitemcode, pc.manfr_part_num) " +
//                        "where " +
//                        "    bm2.billno = ? or bm2.componentitemcode = ? " + // itemcode
//                        // --and pc.id is null
//                        // --and ppp.id is null
//                        // --and pcc.id is null
//                        // --and bm2.QUANTITYPERBILL <> bom.quantity
//                        "order by parent, child",
//                        ps -> {
//                            ps.setString(1, itemcode);
//                            ps.setString(2, itemcode);
//                        },
//                        rs -> {
//                            String billno = rs.getString(1);
//                            log.info("billno: {}", billno );
//                            return;
//                        });
//                return null;
//            });
//            log.info("===[END] itemcode: {} ===", itemcode);
        }

    } // Mas90Synchronizer

    public Mas90SyncDao.Page history(int startPosition, int maxResults) {
        return mas90SyncDao.findHistory(startPosition, maxResults);
    }

    public SyncProcessStatus status() {
        synchronized (syncProcessStatus) {
            try {
                SyncProcessStatus retVal = (SyncProcessStatus) syncProcessStatus.clone();
                syncProcessStatus.resetErrors();
                return retVal;
            } catch (CloneNotSupportedException e) {
                // can't be reached
                log.error("Unexpected internal error.", e);
                return null;
            }
        }
    }

    public SyncProcessStatus start(User user) {
        if (!syncProcessStatus.isFinished()) {
            throw new IllegalStateException("New 'sync. process' can't be started because exists other process.");
        }
        long now = System.currentTimeMillis();
        Mas90Sync record = new Mas90Sync();
        synchronized (syncProcessStatus) {
            record.setStarted(new Timestamp(now));
            record.setInserted(0L);
            record.setUpdated(0L);
            record.setToProcess(0L);
            record.setUser(user);
            record.setStatus(Mas90Sync.Status.IN_PROGRESS);
            mas90SyncDao.persist(record);
            syncProcessStatus.reset();
            syncProcessStatus.setStartedOn(now);
            if (user != null) {
                syncProcessStatus.setUserId(user.getId());
                syncProcessStatus.setUserName(user.getName());
            }
            syncProcessStatus.setFinished(false);
        }
        syncProcess = new Mas90Synchronizer(record);
        syncProcess.start();
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
