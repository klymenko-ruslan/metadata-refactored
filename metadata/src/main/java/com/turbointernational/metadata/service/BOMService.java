package com.turbointernational.metadata.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.BOMItemDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.TurboCarModelEngineYearDao;
import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.util.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;
import static com.turbointernational.metadata.service.BOMService.AddToParentBOMsRequest.ResolutionEnum.REPLACE;
import static com.turbointernational.metadata.service.BOMService.IndexingStatus.*;
import static java.util.Collections.binarySearch;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 * Created by dmytro.trunykov@zorallabs.com on 18.02.16.
 */
@Service
public class BOMService {

    private final static Logger log = LoggerFactory.getLogger(BOMService.class);

    @Qualifier("transactionManager")
    @Autowired
    private PlatformTransactionManager txManager; // JPA

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    private PartDao partDao;

    @Autowired
    private BOMItemDao bomItemDao;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private SearchService searchService;

    /**
     * Contains the date when we started the BOM rebuild, or null if not currently rebuilding.
     */
    public static volatile Date bomRebuildStart = null;

    @Autowired
    private TurboCarModelEngineYearDao tcmeyDao;

    private final IndexingStatus indexingStatus = new IndexingStatus();

    @PostConstruct
    public void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public interface IndexingStatusCallback {
        void setProgressBomRebuild(boolean finished);
        void setProgressIndexing(int indexed);
        void registerFailure(Exception e);
    }

    public class IndexingStatus implements Cloneable {

        public final static int PHASE_NONE = 0;
        public final static int PHASE_ESTIMATION = 1;
        public final static int PHASE_INDEXING = 2;
        public final static int PHASE_FINISHED = 3;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int phase;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private String errorMessage;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean bomDescendantRebuildFinished;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int bomsIndexed;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int bomsIndexingFailures;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int bomsIndexingTotalSteps;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private int bomsIndexingCurrentStep;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private boolean indexBoms;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private Long startedOn;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private Long finishedOn;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private Long userId;

        @JsonView({View.Summary.class})
        @JsonInclude(ALWAYS)
        private String userName;

        public IndexingStatus() {
            reset();
        }

        public void reset() {
            this.phase = PHASE_NONE;
            this.errorMessage = null;
            this.bomDescendantRebuildFinished = false;
            this.bomsIndexed = 0;
            this.bomsIndexingFailures = 0;
            this.bomsIndexingTotalSteps = 0;
            this.bomsIndexingCurrentStep = 0;
            this.indexBoms = true;
            this.startedOn = null;
            this.finishedOn = null;
            this.userId = null;
            this.userName = null;
        }

        public int getPhase() {
            return phase;
        }

        public void setPhase(int phase) {
            this.phase = phase;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public int getBomsIndexed() {
            return bomsIndexed;
        }

        public void setBomsIndexed(int bomsIndexed) {
            this.bomsIndexed = bomsIndexed;
        }

        public int getBomsIndexingFailures() {
            return bomsIndexingFailures;
        }

        public void setBomsIndexingFailures(int bomsIndexingFailures) {
            this.bomsIndexingFailures = bomsIndexingFailures;
        }

        public int getBomsIndexingTotalSteps() {
            return bomsIndexingTotalSteps;
        }

        public void setBomsIndexingTotalSteps(int bomsIndexingTotalSteps) {
            this.bomsIndexingTotalSteps = bomsIndexingTotalSteps;
        }

        public int getBomsIndexingCurrentStep() {
            return bomsIndexingCurrentStep;
        }

        public void setBomsIndexingCurrentStep(int bomsIndexingCurrentStep) {
            this.bomsIndexingCurrentStep = bomsIndexingCurrentStep;
        }

        public Long getStartedOn() {
            return startedOn;
        }

        public void setStartedOn(Long startedOn) {
            this.startedOn = startedOn;
        }

        public Long getFinishedOn() {
            return finishedOn;
        }

        public void setFinishedOn(Long finishedOn) {
            this.finishedOn = finishedOn;
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

        @Override
        public Object clone() throws CloneNotSupportedException {
            IndexingStatus retVal = new IndexingStatus();
            retVal.phase = phase;
            retVal.errorMessage = errorMessage;
            retVal.bomDescendantRebuildFinished = bomDescendantRebuildFinished;
            retVal.bomsIndexed = bomsIndexed;
            retVal.bomsIndexingFailures = bomsIndexingFailures;
            retVal.bomsIndexingTotalSteps = bomsIndexingTotalSteps;
            retVal.bomsIndexingCurrentStep = bomsIndexingCurrentStep;
            retVal.indexBoms = indexBoms;
            retVal.startedOn = startedOn;
            retVal.finishedOn = finishedOn;
            retVal.userId = userId;
            retVal.userName = userName;
            return retVal;
        }

        @Override
        public String toString() {
            return "IndexingStatus{" +
                    "phase=" + phase +
                    ", errorMessage='" + errorMessage + '\'' +
                    ", bomDescendantRebuildFinished=" + bomDescendantRebuildFinished +
                    ", bomsIndexed=" + bomsIndexed +
                    ", bomsIndexingFailures=" + bomsIndexingFailures +
                    ", bomsIndexingTotalSteps=" + bomsIndexingTotalSteps +
                    ", bomsIndexingCurrentStep=" + bomsIndexingCurrentStep +
                    ", indexBoms =" + indexBoms +
                    ", startedOn=" + startedOn +
                    ", finishedOn=" + finishedOn +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    '}';
        }

        public boolean isIndexBoms() {
            return indexBoms;
        }

        public void setIndexBoms(boolean indexBoms) {
            this.indexBoms = indexBoms;
        }

        public boolean isBomDescendantRebuildFinished() {
            return bomDescendantRebuildFinished;
        }

        public void setBomDescendantRebuildFinished(boolean bomDescendantRebuildFinished) {
            this.bomDescendantRebuildFinished = bomDescendantRebuildFinished;
        }
    }

    private class RebuildingJob extends Thread {

        private final boolean indexBoms;

        private final List<Long> turboIds; // turbos for indexing

        class BomsRebuilder extends Thread {

            @Override
            public void run() {
                super.run();
                TransactionTemplate tt = new TransactionTemplate(txManager);
                tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW); // new transaction
                tt.execute((TransactionCallback<Void>) ts -> {
                    try {
                        rebuildBomDescendancy(
                                new IndexingStatusCallback() {

                                    @Override
                                    public void setProgressBomRebuild(boolean finished) {
                                        synchronized (indexingStatus) {
                                            indexingStatus.setBomDescendantRebuildFinished(finished);
                                        }
                                    }

                                    @Override
                                    public void setProgressIndexing(int indexed) {
                                        synchronized (indexingStatus) {
                                            indexingStatus.setBomsIndexingCurrentStep(indexed);
                                        }
                                    }

                                    @Override
                                    public void registerFailure(Exception e) {
                                        synchronized (indexingStatus) {
                                            int failures = indexingStatus.getBomsIndexingFailures();
                                            indexingStatus.setBomsIndexingFailures(failures + 1);
                                            indexingStatus.setErrorMessage(e.getMessage());
                                        }
                                    }

                                },
                                turboIds,
                                indexBoms);
                    } catch (Exception e) {
                        synchronized (indexingStatus) {
                            if (indexingStatus.getErrorMessage() != null) {
                                indexingStatus.setErrorMessage(e.getMessage());
                                indexingStatus.setBomsIndexingFailures(1);
                            }
                        }
                    }
                    return null;
                });
            }

        }

        RebuildingJob(List<Long> turboIds, boolean indexBoms) {
            super();
            this.turboIds = turboIds;
            this.indexBoms = indexBoms;
        }

        @Override
        public void run() {
            super.run();
            TransactionTemplate tt = new TransactionTemplate(txManager);
            tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW); // new transaction
            tt.execute((TransactionCallback<Void>) ts -> {
                try {
                    int bomsTotal = 0;
                    if (indexBoms) {
                        Number n = jdbcTemplate.queryForObject("select count(distinct tcmey.part_id) " +
                            "from turbo_car_model_engine_year as tcmey join part as p on tcmey.part_id = p.id " +
                            "where p.part_type_id = " + PTID_TURBO, Number.class);
                        bomsTotal = n.intValue();
                    }
                    Thread bomsIndexer = null;
                    bomsIndexer = new BomsRebuilder();
                    synchronized (indexingStatus) {
                        indexingStatus.setBomsIndexingTotalSteps(bomsTotal);
                        indexingStatus.setPhase(PHASE_INDEXING);
                    }
                    bomsIndexer.start();
                    bomsIndexer.join();
                } catch (Exception e) {
                    log.error("BOMs rebuilding job failed.", e);
                    synchronized (indexingStatus) {
                        if (indexingStatus.getErrorMessage() == null) {
                            indexingStatus.setErrorMessage(e.getMessage());
                        }
                    }
                } finally {
                    synchronized (indexingStatus) {
                        indexingStatus.setPhase(PHASE_FINISHED);
                        indexingStatus.setFinishedOn(System.currentTimeMillis());
                    }
                }
                return null;
            });
        }

    }

    public IndexingStatus startRebuild(User user, List<Long> turboIds, boolean indexBoms) throws Exception {
        IndexingStatus retVal;
        long now = System.currentTimeMillis();
        synchronized (indexingStatus) {
            int phase = indexingStatus.getPhase();
            if (phase == PHASE_ESTIMATION || phase == PHASE_INDEXING) {
                throw new IllegalStateException("BOMs indexing is already in progress.");
            }
            indexingStatus.reset();
            indexingStatus.setPhase(PHASE_ESTIMATION);
            indexingStatus.setStartedOn(now);
            indexingStatus.setUserId(user.getId());
            indexingStatus.setUserName(user.getUsername());
            indexingStatus.setIndexBoms(indexBoms);
            retVal = getRebuildStatus();
        }
        Thread job = new RebuildingJob(turboIds, indexBoms);
        job.start();
        return retVal;
    }

    public IndexingStatus getRebuildStatus() throws Exception {
        synchronized (indexingStatus) {
            return (IndexingStatus) indexingStatus.clone();
        }
    }

    public static final Date getBomRebuildStart() {
        return bomRebuildStart;
    }

    public static class AddToParentBOMsRequest {

        enum ResolutionEnum {
            ADD, REPLACE
        };

        public static class Row {

            @JsonView({View.Summary.class})
            private Long partId;

            @JsonView({View.Summary.class})
            private ResolutionEnum resolution;

            @JsonView({View.Summary.class})
            private Integer quontity;

            public Long getPartId() {
                return partId;
            }

            public void setPartId(Long partId) {
                this.partId = partId;
            }

            public ResolutionEnum getResolution() {
                return resolution;
            }

            public void setResolution(ResolutionEnum resolution) {
                this.resolution = resolution;
            }

            public Integer getQuontity() {
                return quontity;
            }

            public void setQuontity(Integer quontity) {
                this.quontity = quontity;
            }

        }

        @JsonView({View.Summary.class})
        private List<Row> rows;

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

    }

    @JsonInclude(ALWAYS)
    public static class AddToParentBOMsResponse {

        @JsonInclude(ALWAYS)
        public static class Failure {

            @JsonView(View.Summary.class)
            private Long partId;

            @JsonView(View.Summary.class)
            private String type;

            @JsonView(View.Summary.class)
            private String manufacturerPartNumber;

            @JsonView(View.Summary.class)
            private Integer quantity;

            @JsonView(View.Summary.class)
            private String errorMessage;

            public Failure() {
            }

            public Failure(Long partId, String type, String manufacturerPartNumber, Integer quantity, String errorMessage) {
                this.partId = partId;
                this.type = type;
                this.manufacturerPartNumber = manufacturerPartNumber;
                this.quantity = quantity;
                this.errorMessage = errorMessage;
            }

            public Long getPartId() {
                return partId;
            }

            public void setPartId(Long partId) {
                this.partId = partId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getManufacturerPartNumber() {
                return manufacturerPartNumber;
            }

            public void setManufacturerPartNumber(String manufacturerPartNumber) {
                this.manufacturerPartNumber = manufacturerPartNumber;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }

        @JsonView(View.Summary.class)
        private int added;

        @JsonView(View.Summary.class)
        private List<Failure> failures;

        @JsonView(View.Summary.class)
        private List<BOMItem> parents;

        public AddToParentBOMsResponse(int added, List<Failure> failures, List<BOMItem> parents) {
            this.added = added;
            this.failures = failures;
            this.parents = parents;
        }

        public int getAdded() {
            return added;
        }

        public void setAdded(int added) {
            this.added = added;
        }

        public List<Failure> getFailures() {
            return failures;
        }

        public void setFailures(List<Failure> failures) {
            this.failures = failures;
        }

        public List<BOMItem> getParents() {
            return parents;
        }

        public void setParents(List<BOMItem> parents) {
            this.parents = parents;
        }

    }

    /**
     * Signals that a BOMs tree has a circular recursion.
     */
    public static class FoundBomRecursionException extends Exception {

        /**
         * An ID of a part which makes the circular recursion.
         */
        private Long failedId;

        FoundBomRecursionException(Long failedId) {
            this.failedId = failedId;
        }

        public Long getFailedId() {
            return failedId;
        }

        @Override
        public String getMessage() {
            return failedId.toString();
        }

    }

    private void indexBom(AtomicInteger i, AtomicLong t1, IndexingStatusCallback callback, long turboId) {
        try {
            searchService.indexPart(turboId);
            int i1 = i.incrementAndGet();
            if (i1 % 100 == 0) {
                long t = System.currentTimeMillis();
                log.debug("100 turbos indexed for {} millis.", t - t1.get());
                t1.set(t);
            }
            if (callback != null) {
                callback.setProgressIndexing(i1);
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.registerFailure(e);
            } else {
                log.error("Indexing of a part [" + turboId + "] failed.", e);
            }
        }
    }

    @Async("bomRebuildExecutor")
    @Transactional(propagation = REQUIRES_NEW)
    public void rebuildBomDescendancy(IndexingStatusCallback callback, List<Long> turboIds, boolean indexBoms) {
        log.info("Rebuilding BOM descendancy started.");
        try {
            bomRebuildStart = new Date();
            long t0 = System.currentTimeMillis();
            em.createNativeQuery("CALL RebuildBomDescendancy()").executeUpdate();
            em.clear();
            AtomicLong t1 = new AtomicLong(System.currentTimeMillis());
            log.info("CALL RebuildBomDescendancy(): {} milliseconds.", t1.get() - t0);
            if (callback != null) {
                callback.setProgressBomRebuild(true);
            }
            // Ticket #807.
            AtomicInteger i = new AtomicInteger(0);
            if (indexBoms) {
                log.info("BOM indexing started.");
                if (turboIds == null || turboIds.isEmpty()) {
                    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                    jdbcTemplate.query("select distinct tcmey.part_id " +
                            "from turbo_car_model_engine_year as tcmey join part as p on tcmey.part_id = p.id " +
                            "where p.part_type_id = " + PTID_TURBO, rs -> {
                        long turboId = rs.getLong(1);
                        indexBom(i, t1, callback, turboId);
                    });
                } else {
                    for (Long turboId: turboIds) {
                        indexBom(i, t1, callback, turboId);
                    }
                }
                log.info("BOM indexing finished.");
            }
            long t3 = System.currentTimeMillis();
            log.info("BOM descendancy rebuild completed: {} rows, {} milliseconds.", i.get(), t3 - t0);
        } catch(Throwable th) {
            log.error("Unexpected exception thrown during BOM rebuild.", th);
        } finally {
            bomRebuildStart = null;
            log.info("Rebuilding BOM descendancy finished.");
        }
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void rebuildBomDescendancyForPart(Long partId, boolean clean) {
        Query call = em.createNativeQuery("CALL RebuildBomDescendancyForPart(:partId, :clean)");
        call.setParameter("partId", partId);
        call.setParameter("clean", clean ? 1 : 0);
        call.executeUpdate();
        em.clear();
        searchService.indexPart(partId);
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void rebuildBomDescendancyForParts(List<Long> partIds, boolean clean) {
        for(Long partId : partIds) {
            rebuildBomDescendancyForPart(partId, clean);
        }
    }

    @Transactional(propagation = REQUIRES_NEW)
    public void rebuildBomDescendancyForParts(Iterator<Part> parts, boolean clean) {
        while (parts.hasNext()) {
            Part part = parts.next();
            rebuildBomDescendancyForPart(part.getId(), clean);
        }
    }

    @Transactional(noRollbackFor = {FoundBomRecursionException.class, AssertionError.class})
    public BOMItem create(Long parentPartId, Long childPartId, Integer quantity, boolean rebuildBom) throws FoundBomRecursionException {
        // Create a new BOM item
        Part parent = partDao.findOne(parentPartId);
        Part child = partDao.findOne(childPartId);
        if (child.getManufacturer().getId() != parent.getManufacturer().getId()) {
            throw new AssertionError("Child part must have the same manufacturer as the Parent part.");
        }
        bomRecursionCheck(parent, child);
        BOMItem item = new BOMItem();
        item.setParent(parent);
        item.setChild(child);
        item.setQuantity(quantity);
        parent.getBom().add(item);
        bomItemDao.persist(item);
        // Update the changelog
        changelogService.log("Added bom item.", item.toJson());
        if (rebuildBom) {
            rebuildBomDescendancyForPart(parentPartId, true); // TODO: is clean=true required?
        }
        return item;
    }

    @Transactional
    public List<BOMItem> getByParentId(Long partId) throws Exception {
        return bomItemDao.findByParentId(partId);
    }

    @Transactional
    public List<BOMItem> getParentsForBom(Long partId) throws Exception {
        return bomItemDao.findParentsForBom(partId);
    }

    @Transactional
    public List<BOMItem> getByParentAndTypeIds(Long partId, Long partTypeId) throws Exception {
        return bomItemDao.findByParentAndTypeIds(partId, partTypeId);
    }

    @Transactional(noRollbackFor = {FoundBomRecursionException.class, AssertionError.class})
    public AddToParentBOMsResponse addToParentsBOMs(Long primaryPartId, AddToParentBOMsRequest request) throws Exception {
        int added = 0;
        Part primaryPart = partDao.findOne(primaryPartId);
        Long primaryPartTypeId = primaryPart.getPartType().getId();
        Long primaryPartManufacturerId = primaryPart.getManufacturer().getId();
        List<BOMService.AddToParentBOMsRequest.Row> rows = request.getRows();
        List<AddToParentBOMsResponse.Failure> failures = new ArrayList<>(10);
        for (AddToParentBOMsRequest.Row r : rows) {
            Long pickedPartId = r.getPartId();
            Part pickedPart = partDao.findOne(pickedPartId);
            try {
                Long pickedPartManufacturerId = pickedPart.getManufacturer().getId();
                if (primaryPartManufacturerId != pickedPartManufacturerId) {
                    throw new AssertionError(String.format("Part type '%1$s' of the part [%2$d] - {%3$s} " +
                                    "does not match with part type '{%4$s}' of the part [{%5$d}] - {%6$s}.",
                            primaryPart.getPartType().getName(),
                            primaryPartId, primaryPart.getManufacturerPartNumber(),
                            pickedPart.getPartType().getName(),
                            pickedPartId, pickedPart.getManufacturerPartNumber()
                    ));
                }
                if (r.getResolution() == REPLACE) {
                    // Remove existing BOMs in the picked part.
                    for(Iterator<BOMItem> iterBoms = pickedPart.getBom().iterator(); iterBoms.hasNext();) {
                        BOMItem bomItem = iterBoms.next();
                        Long childPartTypeId = bomItem.getChild().getPartType().getId();
                        if (childPartTypeId == primaryPartTypeId) {
                            String strJsonBom = bomItem.toJson();
                            changelogService.log("Deleted BOM item.", strJsonBom);
                            iterBoms.remove();
                            bomItemDao.remove(bomItem);
                        }
                    }
                }
                // Add the primary part to the list of BOMs of the picked part.
                create(pickedPartId, primaryPartId, r.getQuontity(), false);
                added++;
            } catch(FoundBomRecursionException e) {
                log.debug("Adding of the part [" + primaryPartId + "] to list of BOM for part [" +
                        pickedPartId + "] failed.", e);
                failures.add(new AddToParentBOMsResponse.Failure(pickedPartId, pickedPart.getPartType().getName(),
                        pickedPart.getManufacturerPartNumber(), r.getQuontity(), "Recursion check failed."));
            } catch (AssertionError e) {
                log.debug("Adding of the part [" + primaryPartId + "] to list of BOM for part [" +
                        pickedPartId + "] failed.", e);
                failures.add(new AddToParentBOMsResponse.Failure(pickedPartId, pickedPart.getPartType().getName(),
                        pickedPart.getManufacturerPartNumber(), r.getQuontity(), e.getMessage()));
            }
        }
        List<BOMItem> parents = getParentsForBom(primaryPartId);
        // rebuildBomDescendancy();
        rebuildBomDescendancyForPart(primaryPartId, true);
        return new BOMService.AddToParentBOMsResponse(added, failures, parents);
    }

    @Transactional
    public void update(Long id, Integer quantity) {
        // Get the item
        BOMItem item = bomItemDao.findOne(id);
        // Update the changelog
        changelogService.log("Changed BOM item quantity to " + quantity, item.toJson());
        // Update
        item.setQuantity(quantity);
        bomItemDao.merge(item);
    }

    @Transactional
    public void delete(Long id) {
        // Get the object
        BOMItem item = bomItemDao.findOne(id);
        Part parent = item.getParent();
        // Update the changelog
        String strJsonBom = item.toJson();
        changelogService.log("Deleted BOM item.", strJsonBom);
        // Remove the BOM Item from the parent
        parent.getBom().remove(item);
        partDao.merge(parent);
        // Delete it
        bomItemDao.remove(item);
        rebuildBomDescendancyForPart(parent.getId(), true);
    }

    /**
     * Check that two specified parts has no any cycled recursions:
     * <ul>
     *     <li>not in a BOMs tree of the parentPart</li>
     *     <li>not in a BOMs tree of the childPart</li>
     *     <li>not in a union of these BOMs trees</li>
     * </ul>
     *
     * @param parentPart
     * @param childPart
     * @throws FoundBomRecursionException
     */
    void bomRecursionCheck(Part parentPart, Part childPart) throws FoundBomRecursionException {
        List<Long> parentBoms = loadAllBomsOfPart(parentPart);
        List<Long> childBoms = loadAllBomsOfPart(childPart);
        // Try to find an intersection.
        for (Iterator<Long> parentIter = parentBoms.iterator(); parentIter.hasNext(); ) {
            Long id = parentIter.next();
            if (binarySearch(childBoms, id) >= 0) {
                throw new FoundBomRecursionException(id);
            }
        }
    }

    /**
     * Load IDs of all parts which are BOMs for the specified part.
     *
     * The method recursively walks a tree of BOMs of the specified part
     * and fills a sorted list of IDs of the found parts.
     * The list will also include an ID of the specified part.
     *
     * @param part a part to load its BOMs
     * @return (ascending) ordered list of IDs of parts which are BOM for the specified part.
     *          The list also includes ID of the part.
     * @throws FoundBomRecursionException if the BOM's tree contains cycled recursion
     */
    List<Long> loadAllBomsOfPart(Part part) throws FoundBomRecursionException {
        List<Long> retVal = new ArrayList<>(); // ordered list of IDs
        Stack<Iterator<BOMItem>> postponed = new Stack<>();
        Part p = part;
        while (p != null || !postponed.empty()) {
            if (p == null) {
                Iterator<BOMItem> bomIter = postponed.peek();
                if (bomIter.hasNext()) {
                    BOMItem bom = bomIter.next();
                    p = bom.getChild();
                } else {
                    postponed.pop(); // remove from the stack this exhausted iterator
                }
            } else {
                Long id = p.getId();
                if (binarySearch(retVal, id) >= 0) {
                    throw new FoundBomRecursionException(id);
                }
                retVal.add(id);
                Collections.sort(retVal);
                Set<BOMItem> boms = p.getBom();
                postponed.push(boms.iterator());
                p = null;
            }
        }
        return retVal;
    }

}
