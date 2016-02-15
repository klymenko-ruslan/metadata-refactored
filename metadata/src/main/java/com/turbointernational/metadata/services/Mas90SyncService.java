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
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.sql.Timestamp;
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
        private final List<String> errors = new ArrayList<>();

        @JsonView({View.Summary.class})
        private final List<String> modifications = new ArrayList<>();

        SyncProcessStatus() {
            reset();
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

        public long incPartsUpdateCurrentStep() {
            return ++this.partsUpdateCurrentStep;
        }

        public long getPartsUpdateInserts() {
            return partsUpdateInserts;
        }

        public long incPartsUpdateInserts() {
            return ++partsUpdateInserts;
        }

        public long getPartsUpdateUpdates() {
            return partsUpdateUpdates;
        }

        public long incPartsUpdateUpdates() {
            return ++partsUpdateUpdates;
        }

        public long getPartsUpdateSkipped() {
            return partsUpdateSkipped;
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

        public void addError(String error) {
            errors.add(error);
        }

        public void resetErrors() {
            errors.clear();
        }

        public List<String> getModifications() {
            return modifications;
        }

        public void addModifications(String s) {
            modifications.add(s);
        }

        public void resetModifications() {
            modifications.clear();
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
            this.finished = DEF_PARTSUPDATE_FINISHED;
            resetErrors();
            resetModifications();
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
            retVal.errors.addAll(errors);
            retVal.modifications.addAll(modifications);
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
                    ", modifications=" + modifications +
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
                    syncProcessStatus.setPartsUpdateTotalSteps(numItems + 1); // +1 to load part_types
                }
                Map<String, Long> mas90toLocal = loadPartTypesMap(); // part type value => part type ID
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
                        Part processedPart = null;
                        try {
                            Long partTypeId = mas90toLocal.get(productline);
                            if (partTypeId != null) {
                                processedPart = processPart(itemcode, itemcodedesc, producttype, partTypeId); // separate transaction
                                if (processedPart != null) {
                                    Boolean updated = processBOM(processedPart.getId(), processedPart.getManufacturerPartNumber()); // separate transaction
                                    if (updated == Boolean.TRUE) {
                                        synchronized (syncProcessStatus) {
                                            syncProcessStatus.incPartsUpdateUpdates();
                                        }
                                    }
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
                        } catch (Throwable th) {
                            String errMsg;
                            if (processedPart == null) {
                                errMsg = String.format("Failed processing at the part: '%s'", itemcode);
                            } else {
                                errMsg = String.format("Failed processing at the part: [%d] {%s}",
                                        processedPart.getId(), processedPart.getManufacturerPartNumber());
                            }
                            synchronized (syncProcessStatus) {
                                syncProcessStatus.addError(errMsg);
                            }
                            log.error(errMsg);
                            throw th;
                        }
                    });
                } catch (Throwable e) {
                    synchronized (syncProcessStatus) {
                        syncProcessStatus.addError("Critical error, processing stopped. Cause: " + getRootErrorMessage(e));
                        syncProcessStatus.setFinished(true);
                    }
                    log.error("Synchronization with MAS90 failed." , e);
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
                partDao.rebuildBomDescendancy(); // Ticket #592.
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
         * @return processed part or null on failure
         */
        private Part processPart(String itemcode, String itemcodedesc, String producttype, Long partTypeId) {
            Boolean inactive = "D".equals(producttype);
            TransactionTemplate modifyTransaction = new TransactionTemplate(txManager);
            modifyTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new transaction
            Part processedPart = modifyTransaction.execute(ts -> {
                Long partId = null;
                Part part = partDao.findByPartNumberAndManufacturer(TURBO_INTERNATIONAL_MANUFACTURER_ID, itemcode);
                try {
                    if (part == null) {
                        part = insertPart(itemcode, itemcodedesc, partTypeId, inactive);
                        partId = part.getId();
                        String logMsg = String.format("Inserted a new part: [%d] %s", part.getId(), part.getManufacturerPartNumber());
                        log.info(logMsg);
                        synchronized (syncProcessStatus) {
                            syncProcessStatus.incPartsUpdateInserts();
                            syncProcessStatus.addModifications(logMsg);
                        }
                    } else {
                        if (part.getPartType().getId() == partTypeId) {
                            partId = part.getId();
                            boolean updated = updatePart(part, itemcodedesc, inactive);
                            if (updated) {
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
                    String cause = getRootErrorMessage(e);
                    String err;
                    if (partId != null) {
                        err = String.format("Update of the part [%d] %s failed. Cause: %s", partId, itemcode, cause);
                    } else {
                        err = String.format("Creation of a new part '%s' failed. Cause: %s", itemcode, cause);
                    }
                    synchronized (syncProcessStatus) {
                        syncProcessStatus.addError(err);
                        syncProcessStatus.incPartsUpdateSkipped();
                    }
                    log.error(err /*, e*/);
                    part = null;
                    ts.setRollbackOnly();
                }
                return part;
            });
            return processedPart;
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
            boolean dirty = false;
            StringBuilder modified = new StringBuilder(256);
            String currDesc = p.getDescription();
            if (currDesc == null && itemcodedesc != null) {
                dirty = true;
                modified.append(String.format("Updated description: %s => %s.", currDesc, itemcodedesc));
                p.setDescription(itemcodedesc);
            }
            Boolean currInactive = p.getInactive();
            if (!ObjectUtils.equals(currInactive, inactive)) {
                if (dirty) {
                    modified.append(" ");
                } else {
                    dirty = true;
                }
                modified.append(String.format("Updated attr. 'inactive': %B => %B.", currInactive, inactive));
                p.setInactive(inactive);
            }
            if (dirty ) {
                synchronized (syncProcessStatus) {
                    String s = String.format("Updated the part: [%d] %s ", p.getId(), p.getManufacturerPartNumber()) +
                            modified.toString();
                    syncProcessStatus.addModifications(s);
                    log.info(s);
                }
            }
            return dirty;
        }

        private Boolean processBOM(long partId, String manufacturerPartNumber) {

            class Mas90Bom {
                final String childManufacturerCode;
                final int quantity;
                Mas90Bom(String childManufacturerCode, int quantity) {
                    this.childManufacturerCode = childManufacturerCode;
                    this.quantity = quantity;
                }
            }

            TransactionTemplate tt = new TransactionTemplate(txManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new transaction
            Boolean updated = tt.execute(ts -> {
                List<Mas90Bom> mas90boms = mssqldb.query(
                        "select bd.componentitemcode, bd.quantityperbill " +
                        "from " +
                        "   bm_billdetail as bd join ( " +
                        "       select billno, max(revision) as last_revision from bm_billheader group by billno " +
                        "   ) as br on bd.billno = br.billno and bd.revision = br.last_revision " +
                        "where bd.billno = ?",
                        ps -> {
                            ps.setString(1, manufacturerPartNumber);
                        },
                        (rs, rowNum) -> {
                            String componentItemCode = rs.getString(1);
                            int quantityPerBill = rs.getInt(2);
                            return new Mas90Bom(componentItemCode, quantityPerBill);
                        }

                );
                // Load part in this transaction (Entity Manager context).
                Part part = partDao.getEntityManager().find(Part.class, partId);
                assert part != null : "Internal error. Part not found: " + partId;
                Manufacturer manufacturer = part.getManufacturer();
                assert manufacturer != null : "Manufacturer for the part " + partId + " not found.";
                Long manufacturerId = part.getId();
                assert manufacturerId != null : "Nullable manufacturer ID.";
                assert manufacturerId == TURBO_INTERNATIONAL_MANUFACTURER_ID :
                    String.format("Part (id=%d) from unexpected manufacturer (id=%d).", partId, manufacturerId);
                Set<BOMItem> boms = part.getBom();

                // Merge BOMs.
                boolean dirty = false;

                // Preparation.
                // Build index: itemcode -> Mas90Bom.
                Map<String, Mas90Bom> idxMas90Boms = new HashMap();
                mas90boms.forEach(mb -> idxMas90Boms.put(mb.childManufacturerCode, mb));
                // Build index: child.manufacturerPartNumber -> BomItem.
                Map<String, BOMItem> idxBoms = new HashMap();
                boms.forEach(b -> idxBoms.put(b.getChild().getManufacturerPartNumber(), b));
                // Removal and update.
                Iterator<BOMItem> rmIter = boms.iterator();
                while (rmIter.hasNext()) {
                    BOMItem bom = rmIter.next();
                    Part child = bom.getChild();
                    if (child.getManufacturer().getId() == TURBO_INTERNATIONAL_MANUFACTURER_ID) {
                        String chMnPrtNum = child.getManufacturerPartNumber();
                        Mas90Bom mas90Bom = idxMas90Boms.get(chMnPrtNum);
                        String modification = null;
                        if (mas90Bom == null) {
                            modification = String.format("Part [%d] %s modified. BOM [%d] (child: [%d] %s) " +
                                    "is removed.", partId, manufacturerPartNumber, bom.getId(), child.getId(),
                                    chMnPrtNum);
                            partDao.getEntityManager().remove(bom);
                            rmIter.remove();
                            dirty = true;
                        } else {
                            if (bom.getQuantity() != mas90Bom.quantity) {
                                modification = String.format("Part [%d] %s modified. BOM [%d] (child: [%d] %s) " +
                                    "updated. Quantity: %d => %d", partId, manufacturerPartNumber, bom.getId(),
                                        child.getId(), chMnPrtNum, bom.getQuantity(), mas90Bom.quantity);
                                bom.setQuantity(mas90Bom.quantity);
                                partDao.getEntityManager().merge(bom);
                                dirty = true;
                            }
                        }
                        if (modification != null) {
                            synchronized (syncProcessStatus) {
                                syncProcessStatus.addModifications(modification);
                            }
                            log.info(modification);
                        }
                    } else {
                        log.debug("Modification of the BOM (ID: {}) skipped as child part belongs " +
                                "to foreign manufacturer (ID: {}).", bom.getId(), child.getManufacturer().getId());
                    }
                }
                // Insert
                Iterator<Mas90Bom> addIter = mas90boms.iterator();
                while(addIter.hasNext()) {
                    Mas90Bom mb = addIter.next();
                    if (!idxBoms.containsKey(mb.childManufacturerCode)) {
                        BOMItem newBom = new BOMItem();
                        newBom.setParent(part);
                        Part child = partDao.findByPartNumberAndManufacturer(TURBO_INTERNATIONAL_MANUFACTURER_ID,
                                mb.childManufacturerCode);
                        // In theory it is possible that child is not imported yet (because it will be processed
                        // in the main loop later). In this case we skip insertion in this synchronization session,
                        // as this BOM can be added in a next session.
                        if (child != null) {
                            newBom.setChild(child);
                            newBom.setQuantity(mb.quantity);
                            boms.add(newBom);
                            partDao.getEntityManager().persist(newBom);
                            dirty = true;
                            String modification = String.format("Part [%d] %s modified. Added BOM [%d] " +
                                    "(child: [%d] %s), quantity=%d.", partId, manufacturerPartNumber, newBom.getId(),
                                    child.getId(), child.getManufacturerPartNumber(), newBom.getQuantity());
                            synchronized (syncProcessStatus) {
                                syncProcessStatus.addModifications(modification);
                            }
                            log.info(modification);
                        } else {
                            log.debug("BOM's child not found (manufacture code: {}) for the part (ID: {}).",
                                    mb.childManufacturerCode, partId);
                        }
                    }
                }
                if (dirty) {
                    partDao.merge(part);
                }
                return dirty;
            });
            return updated;
        }

        private String getRootErrorMessage(Throwable th) {
            String retVal;
            Throwable rootCause = ExceptionUtils.getRootCause(th);
            Throwable ex = rootCause == null ? th : rootCause;
            retVal = ex.getMessage();
            if (StringUtils.isEmpty(retVal)) {
                retVal = ClassUtils.getShortClassName(ex, null);
            }
            return retVal;
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
                syncProcessStatus.resetModifications();
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
            record.setFinished(null);
            record.setInserted(0L);
            record.setUpdated(0L);
            record.setSkipped(0L);
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
