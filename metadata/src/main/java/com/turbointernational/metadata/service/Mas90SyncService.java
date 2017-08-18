package com.turbointernational.metadata.service;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.MAS90SYNC;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.BOM_CHILD;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.BOM_PARENT;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.service.Mas90Service.TURBO_INTERNATIONAL_MANUFACTURER_ID;
import static java.lang.Boolean.TRUE;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.Mas90SyncDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.entity.Manufacturer;
import com.turbointernational.metadata.entity.Mas90Sync;
import com.turbointernational.metadata.entity.Mas90SyncFailure;
import com.turbointernational.metadata.entity.Mas90SyncSuccess;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Page;

/**
 * Synchronization service between Mas90 and 'metadata' database.
 * <p>
 * Created by dmytro.trunykov@zorallabs.com on 13.01.16.
 */
@Service
public class Mas90SyncService {

    private final Logger log = LoggerFactory.getLogger(Mas90SyncService.class);

    @Autowired
    private Mas90Database mas90Database;

    @Qualifier("transactionManager")
    @Autowired
    private PlatformTransactionManager txManager; // JPA

    @PersistenceContext(unitName = "metadata")
    private EntityManager entityManager;

    @Autowired
    private PartDao partDao;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private Mas90SyncDao mas90SyncDao; // local storage

    @Qualifier("dataSourceMas90")
    @Autowired
    private DataSource dataSourceMas90; // connections to MS-SQL (MAS90)

    @Autowired
    private Mas90Service mas90Service;

    private JdbcTemplate mas90db;

    private Mas90Synchronizer syncProcess;

    SyncProcessStatus syncProcessStatus;

    private final List<String> allErrors = new ArrayList<>(100);

    private final List<String> allModifications = new ArrayList<>(100);

    void registerError(String msg) {
        allErrors.add(msg);
        syncProcessStatus.addError(msg);
    }

    void registerModification(String msg) {
        allModifications.add(msg);
        syncProcessStatus.addModification(msg);
    }

    @PostConstruct
    public void init() {
        mas90db = new JdbcTemplate(dataSourceMas90);
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
        private final static boolean DEF_PARTSUPDATE_FINISHED = TRUE;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private Long startedOn;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private Long userId;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private String userName;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private long partsUpdateTotalSteps;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private long partsUpdateCurrentStep;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private long partsUpdateInserts;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private long partsUpdateUpdates;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private long partsUpdateSkipped;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private boolean finished;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private final List<String> errors = new ArrayList<>();

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
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

        public void incPartsUpdateTotalSteps() {
            this.partsUpdateTotalSteps++;
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

        public void addModification(String s) {
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
            return "SyncProcessStatus{" + "startedOn=" + startedOn + ", userId=" + userId + ", userName='" + userName
                    + '\'' + ", partsUpdateTotalSteps=" + partsUpdateTotalSteps + ", partsUpdateCurrentStep="
                    + partsUpdateCurrentStep + ", partsUpdateInserts=" + partsUpdateInserts + ", partsUpdateUpdates="
                    + partsUpdateUpdates + ", partsUpdateSkipped=" + partsUpdateSkipped + ", finished=" + finished
                    + ", modifications=" + modifications + ", errors=" + errors + '}';
        }

    }

    class Mas90Synchronizer extends Thread {

        private final Logger log = LoggerFactory.getLogger(Mas90Synchronizer.class);

        private final User user;

        private final Mas90Sync record;

        Mas90Synchronizer(User user, Mas90Sync record) {
            this.user = user;
            this.record = record;
        }

        @Override
        public void run() {
            Mas90Sync.Status status;
            log.info("Started synchronization with MAS90.");
            String countQuery = mas90Database.getCountQuery();
            long numItems = mas90db.queryForObject(countQuery, Long.class);
            synchronized (syncProcessStatus) {
                // +1 to load part_types
                syncProcessStatus.setPartsUpdateTotalSteps(numItems + 1);
            }
            // part type value => PartType
            Map<String, PartType> mas90toLocal;
            try {
                mas90toLocal = mas90Service.loadPartTypesMap();
            } catch (Exception e) {
                String msg = e.getMessage();
                synchronized (syncProcessStatus) {
                    registerError(msg);
                }
                log.warn(msg);
                return;
            }
            synchronized (syncProcessStatus) {
                syncProcessStatus.incPartsUpdateCurrentStep();
            }
            List<Long> modifiedPartIds = new ArrayList<>(1000);
            try {
                Set<Part> toBeReprocessed = new HashSet<>();
                String itemsQuery = mas90Database.getItemsQuery();
                mas90db.query(itemsQuery, rs -> { // we may skip transaction as
                                                  // we use MAS90 DB to query
                                                  // only
                    String itemcode = rs.getString(1); // e.g. 6-A-0291
                    String itemcodedesc = rs.getString(2); // e.g. HEAT SHIELD,
                                                           // T3/4, WHEEL
                    String productline = rs.getString(3); // e.g. HS
                    String producttype = rs.getString(4); // e.g. F
                    Part processedPart = null;
                    try {
                        PartType pt = mas90toLocal.get(productline);
                        if (pt != null) {
                            Long partTypeId = pt.getId();
                            processedPart = processPart(itemcode, itemcodedesc, producttype, partTypeId); // separate
                                                                                                          // transaction
                            if (processedPart != null) { // null -- failure
                                Boolean updated = processBOM(processedPart, toBeReprocessed, true); // separate
                                                                                                    // transaction
                                if (updated == TRUE) {
                                    synchronized (syncProcessStatus) {
                                        syncProcessStatus.incPartsUpdateUpdates();
                                    }
                                    modifiedPartIds.add(partTypeId);
                                }
                            }
                        } else {
                            synchronized (syncProcessStatus) {
                                registerError(String.format(
                                        "Part '%1$s' in MAS90 has " + "unknown part type (product line): '%2$s'.",
                                        itemcode, productline));
                            }
                        }
                        synchronized (syncProcessStatus) {
                            syncProcessStatus.incPartsUpdateCurrentStep();
                        }
                    } catch (Throwable th) {
                        String errMsg;
                        if (processedPart == null) {
                            errMsg = String.format("Failed processing at the part: '%1$s'", itemcode);
                        } else {
                            errMsg = String.format("Failed processing at the part: [%1$d] %2$s", processedPart.getId(),
                                    processedPart.getManufacturerPartNumber());
                        }
                        synchronized (syncProcessStatus) {
                            registerError(errMsg);
                        }
                        log.error(errMsg);
                        throw th;
                    }
                });
                if (!toBeReprocessed.isEmpty()) {
                    log.info("There are {} parts for reprocessing.", toBeReprocessed.size());
                    Set<Part> toBeReprocessed2 = new HashSet<>();
                    toBeReprocessed.forEach(thePart -> {
                        log.info("Reprocessing the part: {}", thePart.getManufacturerPartNumber());
                        processBOM(thePart, toBeReprocessed2, false);
                        synchronized (syncProcessStatus) {
                            syncProcessStatus.incPartsUpdateCurrentStep();
                        }
                    });
                    if (!toBeReprocessed2.isEmpty()) {
                        // Because MAS90 is external system, so data there can
                        // be changed
                        // while this process is running. We can't fix this in
                        // any way,
                        // so here we just write notification that this case
                        // happened.
                        // This case is actually not critical.
                        log.warn("In the second iteration we still found absent parts.");
                        toBeReprocessed2.forEach(p -> log.info("Absent part: {}", p.getManufacturerPartNumber()));
                    }
                }
                status = Mas90Sync.Status.FINISHED;
            } catch (Throwable e) {
                synchronized (syncProcessStatus) {
                    registerError("Critical error, processing stopped. Cause: " + getRootErrorMessage(e));
                    syncProcessStatus.setFinished(true);
                }
                log.error("Synchronization with MAS90 failed.", e);
                status = Mas90Sync.Status.FAILED;
            }
            // Save to a history table a result of this synchronization.
            final Mas90Sync.Status status2 = status; // helper final variable in
                                                     // order to be accessible
                                                     // from lambda below
            TransactionTemplate transaction2 = new TransactionTemplate(txManager);
            transaction2.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new
                                                                                                 // transaction
            transaction2.execute((TransactionCallback<Void>) transactionStatus -> {
                long total, updated, inserted, skipped;
                synchronized (syncProcessStatus) {
                    total = syncProcessStatus.getPartsUpdateTotalSteps();
                    updated = syncProcessStatus.getPartsUpdateUpdates();
                    inserted = syncProcessStatus.getPartsUpdateInserts();
                    skipped = syncProcessStatus.getPartsUpdateSkipped();
                    syncProcessStatus.setFinished(true);
                }
                record.setStatus(status2);
                record.setFinished(new Timestamp(System.currentTimeMillis()));
                record.setToProcess(total);
                record.setUpdated(updated);
                record.setInserted(inserted);
                record.setSkipped(skipped);
                for (String error : allErrors) {
                    Mas90SyncFailure m90sf = new Mas90SyncFailure();
                    m90sf.setLog(error);
                    m90sf.setMas90Sync(record);
                    record.getFailures().add(m90sf);
                }
                allErrors.clear();
                for (String m : allModifications) {
                    Mas90SyncSuccess m90ss = new Mas90SyncSuccess();
                    m90ss.setLog(m);
                    m90ss.setMas90Sync(record);
                    record.getSuccesses().add(m90ss);
                }
                allModifications.clear();
                mas90SyncDao.merge(record);
                return null;
            });
            log.info("Synchronization with MAS90 finished.");
        }

        /**
         * Insert or update a part.
         * <p>
         * This operation is made in a separate transaction. We need a separate
         * transaction to update/insert the Part because different types of
         * exception could arise during this operation and we don't wont to
         * rollback a transaction with-in the main loop.
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
            modifyTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new
                                                                                                      // transaction
            Part processedPart = modifyTransaction.execute(ts -> {
                Long partId = null;
                Part part = mas90Service.findTurboInternationalPart(itemcode);
                try {
                    if (part == null) {
                        part = insertPart(itemcode, itemcodedesc, partTypeId, inactive);
                        partId = part.getId();
                        String logMsg = String.format("Inserted a new part: [%d] %s", part.getId(),
                                part.getManufacturerPartNumber());
                        log.info(logMsg);
                        List<RelatedPart> relatedParts = new ArrayList<>(1);
                        relatedParts.add(new RelatedPart(partId, PART0));
                        changelogService.log(MAS90SYNC, user, logMsg, relatedParts);
                        synchronized (syncProcessStatus) {
                            syncProcessStatus.incPartsUpdateInserts();
                            registerModification(logMsg);
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
                            throw new IllegalArgumentException("Updated part has different part type " + "in MAS90 ("
                                    + partTypeId + ") and in 'metadata' (" + part.getPartType().getId() + ").");
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
                        registerError(err);
                        syncProcessStatus.incPartsUpdateSkipped();
                    }
                    log.error(err/* , e */);
                    part = null;
                    ts.setRollbackOnly();
                }
                return part;
            });
            return processedPart;
        }

        private Part insertPart(String itemcode, String itemcodedesc, Long partTypeId, Boolean inactive) {
            Part p = Part.newInstance(partTypeId);
            PartType partType = entityManager.getReference(PartType.class, partTypeId);
            p.setManufacturerPartNumber(itemcode);
            Manufacturer manufacturer = entityManager.getReference(Manufacturer.class,
                    TURBO_INTERNATIONAL_MANUFACTURER_ID);
            p.setManufacturer(manufacturer);
            p.setDescription(itemcodedesc);
            p.setPartType(partType);
            p.setInactive(inactive);
            partDao.persist(p);
            return p;
        }

        private boolean updatePart(Part p, String itemcodedesc, Boolean inactive) {
            boolean dirty = false;
            StringBuilder modified = new StringBuilder(256);
            String currDesc = p.getDescription();
            if (StringUtils.isBlank(currDesc) && itemcodedesc != null) {
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
            if (dirty) {
                String s = String.format("Updated the part: [%d] %s ", p.getId(), p.getManufacturerPartNumber())
                        + modified.toString();
                log.info(s);
                List<RelatedPart> relatedParts = new ArrayList<>(1);
                relatedParts.add(new RelatedPart(p.getId(), PART0));
                changelogService.log(MAS90SYNC, user, s, relatedParts);
                synchronized (syncProcessStatus) {
                    registerModification(s);
                }
            }
            return dirty;
        }

        private Boolean processBOM(Part thePart, Set<Part> toBeReprocessed, boolean adjustCounter) {
            long partId = thePart.getId();
            String manufacturerPartNumber = thePart.getManufacturerPartNumber();
            class Mas90Bom {
                final String childManufacturerCode;
                final int quantity;

                Mas90Bom(String childManufacturerCode, int quantity) {
                    this.childManufacturerCode = childManufacturerCode;
                    this.quantity = quantity;
                }
            }

            TransactionTemplate tt = new TransactionTemplate(txManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // new
                                                                                       // transaction
            Boolean updated = tt.execute(ts -> {
                String bomsQuery = mas90Database.getBomsQuery();
                List<Mas90Bom> mas90boms = mas90db.query(bomsQuery, ps -> ps.setString(1, manufacturerPartNumber),
                        (rs, rowNum) -> {
                            String componentItemCode = rs.getString(1);
                            int quantityPerBill = rs.getInt(2);
                            return new Mas90Bom(componentItemCode, quantityPerBill);
                        });
                // Load part in this transaction (EntityManager context).
                Part part = entityManager.find(Part.class, partId);
                if (part == null) {
                    throw new AssertionError("Internal error. Part not found: " + partId);
                }
                Manufacturer manufacturer = part.getManufacturer();
                Long manufacturerId = manufacturer.getId();
                if (manufacturerId != TURBO_INTERNATIONAL_MANUFACTURER_ID) {
                    throw new AssertionError(String.format("Part (id=%d) from unexpected manufacturer (id=%d).", partId,
                            manufacturerId));
                }
                Set<BOMItem> boms = part.getBom();

                // Merge BOMs.
                boolean dirty = false;

                // Preparation.
                // Build index: itemcode -> Mas90Bom.
                Map<String, Mas90Bom> idxMas90Boms = new HashMap<>();
                mas90boms.forEach(mb -> idxMas90Boms.put(mb.childManufacturerCode, mb));
                // Build index: child.manufacturerPartNumber -> BomItem.
                Map<String, BOMItem> idxBoms = new HashMap<>();
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
                            modification = String.format(
                                    "Part [%d] %s modified. BOM [%d] (child: [%d] %s) " + "is removed.", partId,
                                    manufacturerPartNumber, bom.getId(), child.getId(), chMnPrtNum);
                            entityManager.remove(bom);
                            rmIter.remove();
                            dirty = true;
                        } else {
                            if (bom.getQuantity() != mas90Bom.quantity) {
                                modification = String.format(
                                        "Part [%d] %s modified. BOM [%d] (child: [%d] %s) "
                                                + "updated. Quantity: %d => %d",
                                        partId, manufacturerPartNumber, bom.getId(), child.getId(), chMnPrtNum,
                                        bom.getQuantity(), mas90Bom.quantity);
                                bom.setQuantity(mas90Bom.quantity);
                                entityManager.merge(bom);
                                dirty = true;
                            }
                        }
                        if (modification != null) {
                            log.info(modification);
                            List<RelatedPart> relatedParts = new ArrayList<>(2);
                            relatedParts.add(new RelatedPart(bom.getParent().getId(), BOM_PARENT));
                            relatedParts.add(new RelatedPart(bom.getChild().getId(), BOM_CHILD));
                            changelogService.log(MAS90SYNC, user, modification, relatedParts);
                            synchronized (syncProcessStatus) {
                                registerModification(modification);
                            }
                        }
                    } else {
                        log.debug(
                                "Modification of the BOM (ID: {}) skipped as child part belongs "
                                        + "to foreign manufacturer (ID: {}).",
                                bom.getId(), child.getManufacturer().getId());
                    }
                }
                // Insertion.
                Iterator<Mas90Bom> addIter = mas90boms.iterator();
                while (addIter.hasNext()) {
                    Mas90Bom mb = addIter.next();
                    if (!idxBoms.containsKey(mb.childManufacturerCode)) {
                        if (!mas90Service.isManfrNum(mb.childManufacturerCode)) {
                            log.info("Skip this part to add as child BOM because of unsuitable "
                                    + "manufacturer number: {}", mb.childManufacturerCode);
                            continue;
                        }
                        BOMItem newBom = new BOMItem();
                        newBom.setParent(part);
                        Part child = mas90Service.findTurboInternationalPart(mb.childManufacturerCode);
                        // In theory it is possible that child is not imported
                        // yet (because it will be processed
                        // in the main loop later). In this case we add
                        // 'thePart' to a list 'toBeReprocessed'.
                        // Parts in that list will be processed again when the
                        // main loop finished.
                        if (child != null) {
                            newBom.setChild(child);
                            newBom.setQuantity(mb.quantity);
                            boms.add(newBom);
                            entityManager.persist(newBom);
                            dirty = true;
                            String modification = String.format(
                                    "Part [%d] %s modified. Added BOM [%d] " + "(child: [%d] %s), quantity=%d.", partId,
                                    manufacturerPartNumber, newBom.getId(), child.getId(),
                                    child.getManufacturerPartNumber(), newBom.getQuantity());
                            log.info(modification);
                            List<RelatedPart> relatedParts = new ArrayList<>(2);
                            relatedParts.add(new RelatedPart(newBom.getParent().getId(), BOM_PARENT));
                            relatedParts.add(new RelatedPart(newBom.getChild().getId(), BOM_CHILD));
                            changelogService.log(MAS90SYNC, user, modification, relatedParts);
                            synchronized (syncProcessStatus) {
                                registerModification(modification);
                            }
                        } else {
                            log.info(
                                    "BOM's child not found (manufacture code: {}) for the part (ID: {}). "
                                            + "Added to a list for reprocessing when all parts will be imported.",
                                    mb.childManufacturerCode, partId);
                            toBeReprocessed.add(thePart);
                            if (adjustCounter) {
                                synchronized (syncProcessStatus) {
                                    syncProcessStatus.incPartsUpdateTotalSteps();
                                }
                            }
                        }
                    }
                }
                if (dirty) {
                    partDao.merge(part);
                }
                return dirty;
            });
            // bomService.rebuildBomDescendancyForPart(partId, true); // Ticket
            // #807.
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

    public Page<Mas90Sync> history(int startPosition, int maxResults) {
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

    Mas90Sync prepareStart(User user) {
        Mas90Sync record = null;
        synchronized (syncProcessStatus) {
            if (!syncProcessStatus.isFinished()) {
                throw new IllegalStateException(
                        "New synchronization process can't be started because " + "exists other process.");
            }
            long now = System.currentTimeMillis();
            record = new Mas90Sync();
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
            allErrors.clear();
            allModifications.clear();
            syncProcessStatus.setStartedOn(now);
            if (user == null) {
                syncProcessStatus.setUserId(null);
                syncProcessStatus.setUserName(null);
            } else {
                syncProcessStatus.setUserId(user.getId());
                syncProcessStatus.setUserName(user.getName());
            }
            syncProcessStatus.setFinished(false);
        }
        return record;
    }

    public SyncProcessStatus start(User user) {
        if (user == null) {
            throw new AssertionError("User can't be null.");
        }
        Mas90Sync record = prepareStart(user);
        syncProcess = new Mas90Synchronizer(user, record);
        syncProcess.start();
        return status();
    }

    public Mas90Sync result(Long id) {
        Mas90Sync m90s = mas90SyncDao.findOne(id);
        return m90s;
    }

}
