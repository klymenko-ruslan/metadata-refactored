package com.turbointernational.metadata.service;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.BOM;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.BOM_CHILD;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.BOM_PARENT;
import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;
import static com.turbointernational.metadata.service.BOMService.AddToParentBOMsRequest.ResolutionEnum.REPLACE;
import static com.turbointernational.metadata.service.BOMService.IndexingStatus.PHASE_ESTIMATION;
import static com.turbointernational.metadata.service.BOMService.IndexingStatus.PHASE_FINISHED;
import static com.turbointernational.metadata.service.BOMService.IndexingStatus.PHASE_INDEXING;
import static com.turbointernational.metadata.util.FormatUtils.formatBOMItem;
import static java.util.Collections.binarySearch;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.BOMItemDao;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.User;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.BOMService.CreateBOMsRequest.Row;
import com.turbointernational.metadata.service.BOMService.CreateBOMsResponse.Failure;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.util.View;

/**
 * Created by dmytro.trunykov@zorallabs.com on 2016-02-18.
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

    @PersistenceContext(unitName = "metadata")
    protected EntityManager em;

    @Autowired
    private PartDao partDao;

    @Autowired
    private BOMItemDao bomItemDao;

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private SearchService searchService;

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

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private int phase;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private String errorMessage;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private boolean bomDescendantRebuildFinished;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private int bomsIndexed;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private int bomsIndexingFailures;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private int bomsIndexingTotalSteps;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private int bomsIndexingCurrentStep;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private boolean indexBoms;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private Long startedOn;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private Long finishedOn;

        @JsonView({ View.Summary.class })
        @JsonInclude(ALWAYS)
        private Long userId;

        @JsonView({ View.Summary.class })
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

        public boolean isRebuilding() {
            return phase == PHASE_ESTIMATION || phase == PHASE_INDEXING;
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
            return "IndexingStatus{" + "phase=" + phase + ", errorMessage='" + errorMessage + '\''
                    + ", bomDescendantRebuildFinished=" + bomDescendantRebuildFinished + ", bomsIndexed=" + bomsIndexed
                    + ", bomsIndexingFailures=" + bomsIndexingFailures + ", bomsIndexingTotalSteps="
                    + bomsIndexingTotalSteps + ", bomsIndexingCurrentStep=" + bomsIndexingCurrentStep + ", indexBoms ="
                    + indexBoms + ", startedOn=" + startedOn + ", finishedOn=" + finishedOn + ", userId=" + userId
                    + ", userName='" + userName + '\'' + '}';
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
                tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW); // new
                                                                     // transaction
                tt.execute((TransactionCallback<Void>) ts -> {
                    try {
                        rebuildBomDescendancy(new IndexingStatusCallback() {

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

                        }, turboIds, indexBoms);
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
            tt.setPropagationBehavior(PROPAGATION_REQUIRES_NEW); // new
                                                                 // transaction
            tt.execute((TransactionCallback<Void>) ts -> {
                try {
                    int bomsTotal = 0;
                    if (indexBoms) {
                        Number n = jdbcTemplate.queryForObject("select count(distinct tcmey.part_id) "
                                + "from turbo_car_model_engine_year as tcmey join part as p on tcmey.part_id = p.id "
                                + "where p.part_type_id = " + PTID_TURBO, Number.class);
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
        if (getRebuildStatus().isRebuilding()) {
            throw new AssertionError("BOM rebuild is already in progress.");
        }
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

    public IndexingStatus getRebuildStatus() {
        synchronized (indexingStatus) {
            try {
                return (IndexingStatus) indexingStatus.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError("Internal error. This exception should not be thrown.", e);
            }
        }
    }

    public static class CreateBOMsRequest {

        public static class Row {

            @JsonView(View.Summary.class)
            private Long childPartId;

            @JsonView(View.Summary.class)
            private Integer quantity;

            public Long getChildPartId() {
                return childPartId;
            }

            public void setChildPartId(Long childPartId) {
                this.childPartId = childPartId;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

        }

        @JsonView(View.Summary.class)
        private Long parentPartId;

        /**
         * Changelog source IDs which should be linked to the changelog. See
         * ticket #891 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] sourcesIds;

        @JsonView(View.Summary.class)
        private Integer[] chlogSrcRatings;

        @JsonView(View.Summary.class)
        private String chlogSrcLnkDescription;

        /**
         * IDs of uploaded files which should be attached to this changelog.
         * See ticket #933 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] attachIds;

        @JsonView({ View.Summary.class })
        private List<Row> rows;

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

        public Long getParentPartId() {
            return parentPartId;
        }

        public void setParentPartId(Long parentPartId) {
            this.parentPartId = parentPartId;
        }

        public Long[] getSourcesIds() {
            return sourcesIds;
        }

        public void setSourcesIds(Long[] sourcesIds) {
            this.sourcesIds = sourcesIds;
        }

        public Integer[] getChlogSrcRatings() {
            return chlogSrcRatings;
        }

        public void setChlogSrcRatings(Integer[] chlogSrcRatings) {
            this.chlogSrcRatings = chlogSrcRatings;
        }

        public String getChlogSrcLnkDescription() {
            return chlogSrcLnkDescription;
        }

        public void setChlogSrcLnkDescription(String chlogSrcLnkDescription) {
            this.chlogSrcLnkDescription = chlogSrcLnkDescription;
        }

        public Long[] getAttachIds() {
            return attachIds;
        }

        public void setAttachIds(Long[] attachIds) {
            this.attachIds = attachIds;
        }

    }

    @JsonInclude(ALWAYS)
    public static class CreateBOMsResponse {

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

            public Failure(Long partId, String type, String manufacturerPartNumber, Integer quantity,
                    String errorMessage) {
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
        private List<Failure> failures;

        @JsonView(View.Summary.class)
        private List<BOMItem> boms;

        public CreateBOMsResponse(List<Failure> failures, List<BOMItem> boms) {
            this.failures = failures;
            this.boms = boms;
        }

        public List<Failure> getFailures() {
            return failures;
        }

        public void setFailures(List<Failure> failures) {
            this.failures = failures;
        }

        public List<BOMItem> getBoms() {
            return boms;
        }

        public void setBoms(List<BOMItem> boms) {
            this.boms = boms;
        }

    }

    public static class AddToParentBOMsRequest {

        enum ResolutionEnum {
            ADD, REPLACE
        };

        public static class Row {

            @JsonView({ View.Summary.class })
            private Long partId;

            @JsonView({ View.Summary.class })
            private ResolutionEnum resolution;

            @JsonView({ View.Summary.class })
            private Integer quantity;

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

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

        }

        /**
         * Changelog source IDs which should be linked to the changelog. See
         * ticket #891 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] sourcesIds;

        /**
         * IDs of uploaded files which should be attached to this changelog.
         * See ticket #933 for details.
         */
        @JsonView(View.Summary.class)
        private Long[] attachIds;

        @JsonView(View.Summary.class)
        private Integer[] chlogSrcRatings;

        @JsonView(View.Summary.class)
        private String chlogSrcLnkDescription;

        @JsonView({ View.Summary.class })
        private List<Row> rows;

        public Long[] getSourcesIds() {
            return sourcesIds;
        }

        public void setSourcesIds(Long[] sourcesIds) {
            this.sourcesIds = sourcesIds;
        }

        public Integer[] getChlogSrcRatings() {
            return chlogSrcRatings;
        }

        public void setChlogSrcRatings(Integer[] chlogSrcRatings) {
            this.chlogSrcRatings = chlogSrcRatings;
        }

        public String getChlogSrcLnkDescription() {
            return chlogSrcLnkDescription;
        }

        public void setChlogSrcLnkDescription(String chlogSrcLnkDescription) {
            this.chlogSrcLnkDescription = chlogSrcLnkDescription;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

        public Long[] getAttachIds() {
            return attachIds;
        }

        public void setAttachIds(Long[] attachIds) {
            this.attachIds = attachIds;
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

            public Failure(Long partId, String type, String manufacturerPartNumber, Integer quantity,
                    String errorMessage) {
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

        private static final long serialVersionUID = 7962266317894202552L;

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
    @Transactional
    public void rebuildBomDescendancy(IndexingStatusCallback callback, List<Long> turboIds, boolean indexBoms) {
        log.info("Rebuilding BOM descendancy started.");
        try {
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
                    jdbcTemplate.query("select distinct tcmey.part_id "
                            + "from turbo_car_model_engine_year as tcmey join part as p on tcmey.part_id = p.id "
                            + "where p.part_type_id = " + PTID_TURBO, rs -> {
                                long turboId = rs.getLong(1);
                                indexBom(i, t1, callback, turboId);
                            });
                } else {
                    for (Long turboId : turboIds) {
                        indexBom(i, t1, callback, turboId);
                    }
                }
                log.info("BOM indexing finished.");
            }
            long t3 = System.currentTimeMillis();
            log.info("BOM descendancy rebuild completed: {} rows, {} milliseconds.", i.get(), t3 - t0);
        } catch (Throwable th) {
            log.error("Unexpected exception thrown during BOM rebuild.", th);
        } finally {
            log.info("Rebuilding BOM descendancy finished.");
        }
    }

    @Transactional
    public void rebuildBomDescendancyForPart(Long partId, boolean clean) {
        if (getRebuildStatus().isRebuilding()) {
            throw new AssertionError("BOM rebuild is already in progress.");
        }
        try {
            synchronized (indexingStatus) {
                indexingStatus.setPhase(PHASE_INDEXING);
            }
            Query call = em.createNativeQuery("CALL RebuildBomDescendancyForPart(:partId, :clean)");
            call.setParameter("partId", partId);
            call.setParameter("clean", clean ? 1 : 0);
            call.executeUpdate();
            searchService.indexPart(partId);
        } finally {
            synchronized (indexingStatus) {
                indexingStatus.setPhase(PHASE_FINISHED);
            }
        }
    }

    @Transactional
    public void rebuildBomDescendancyForParts(List<Long> partIds, boolean clean) {
        for (Long partId : partIds) {
            rebuildBomDescendancyForPart(partId, clean);
        }
    }

    @Transactional
    public void rebuildBomDescendancyForParts(Iterator<Part> parts, boolean clean) {
        while (parts.hasNext()) {
            Part part = parts.next();
            rebuildBomDescendancyForPart(part.getId(), clean);
        }
    }

    @SuppressWarnings("unused")
    private class CreateBOMItemResult {

        private BOMItem bom;

        private Changelog changelog;

        public CreateBOMItemResult(BOMItem bom, Changelog changelog) {
            this.bom = bom;
            this.changelog = changelog;
        }

        public BOMItem getBom() {
            return bom;
        }

        public void setBom(BOMItem bom) {
            this.bom = bom;
        }

        public Changelog getChangelog() {
            return changelog;
        }

        public void setChangelog(Changelog changelog) {
            this.changelog = changelog;
        }

    }

    private CreateBOMItemResult _create(HttpServletRequest httpRequest, Long parentPartId, Long childPartId,
            Integer quantity, Long[] sourcesIds, Integer[] ratings, String description, Long[] attachIds)
            throws FoundBomRecursionException {
        // Create a new BOM item
        Part parent = partDao.findOne(parentPartId);
        Part child = partDao.findOne(childPartId);
        if (child.getManufacturer().getId() != parent.getManufacturer().getId()) {
            throw new AssertionError("Child part must have the same manufacturer as the Parent part.");
        }
        bomRecursionCheck(parent, child);
        BOMItem bom = new BOMItem();
        bom.setParent(parent);
        bom.setChild(child);
        bom.setQuantity(quantity);
        parent.getBom().add(bom);
        bomItemDao.persist(bom);
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(parent.getId(), BOM_PARENT));
        relatedParts.add(new RelatedPart(child.getId(), BOM_CHILD));
        Changelog changelog = changelogService.log(BOM, "Added bom item: " + formatBOMItem(bom), bom.toJson(),
                relatedParts);
        changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        return new CreateBOMItemResult(bom, changelog);
    }

    @Transactional(noRollbackFor = { FoundBomRecursionException.class, AssertionError.class })
    public CreateBOMsResponse createBOMs(HttpServletRequest httpRequest, CreateBOMsRequest request) throws Exception {
        Long parentPartId = request.getParentPartId();
        List<Failure> failures = new ArrayList<>();
        for (Row row : request.getRows()) {
            // Create a new BOM item
            Part parent = partDao.findOne(parentPartId);
            Part child = partDao.findOne(row.getChildPartId());
            Long childId = child.getId();
            if (child.getManufacturer().getId() != parent.getManufacturer().getId()) {
                throw new AssertionError("Child part must have the same manufacturer as the Parent part.");
            }
            try {
                _create(httpRequest, parentPartId, childId, row.getQuantity(), request.getSourcesIds(),
                        request.getChlogSrcRatings(), request.getChlogSrcLnkDescription(), request.getAttachIds());
            } catch (FoundBomRecursionException e) {
                log.debug("Adding of the part [" + childId + "] to list of BOM for part [" + parentPartId + "] failed.",
                        e);
                failures.add(new Failure(childId, child.getPartType().getName(), child.getManufacturerPartNumber(),
                        row.getQuantity(), "Recursion check failed."));
            } catch (AssertionError e) {
                log.debug("Adding of the part [" + childId + "] to list of BOM for part [" + parentPartId + "] failed.",
                        e);
                failures.add(new Failure(childId, child.getPartType().getName(), child.getManufacturerPartNumber(),
                        row.getQuantity(), e.getMessage()));
            }
        }
        rebuildBomDescendancyForPart(parentPartId, true); // TODO: is clean=true required?
        List<BOMItem> boms = getByParentId(parentPartId);
        return new CreateBOMsResponse(failures, boms);
    }

    @Transactional
    public List<BOMItem> getByParentId(Long partId) throws Exception {
        return bomItemDao.findByParentId(partId);
    }

    @Transactional
    public List<BOMItem> getParentsForBom(Long partId) throws Exception {
        return bomItemDao.findParentsForBom(partId);
    }

    @Transactional(propagation = REQUIRED)
    public List<BOMItem> getByParentAndTypeIds(Long partId, Long partTypeId) throws Exception {
        return bomItemDao.findByParentAndTypeIds(partId, partTypeId);
    }

    @Transactional(noRollbackFor = { FoundBomRecursionException.class, AssertionError.class }, propagation = REQUIRED)
    public AddToParentBOMsResponse addToParentsBOMs(HttpServletRequest httpRequest, Long primaryPartId,
            AddToParentBOMsRequest request) throws Exception {
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
                    throw new AssertionError(String.format(
                            "Part type '%1$s' of the part [%2$d] - {%3$s} "
                                    + "does not match with part type '{%4$s}' of the part [{%5$d}] - {%6$s}.",
                            primaryPart.getPartType().getName(), primaryPartId, primaryPart.getManufacturerPartNumber(),
                            pickedPart.getPartType().getName(), pickedPartId, pickedPart.getManufacturerPartNumber()));
                }
                if (r.getResolution() == REPLACE) {
                    // Remove existing BOMs in the picked part.
                    for (Iterator<BOMItem> iterBoms = pickedPart.getBom().iterator(); iterBoms.hasNext();) {
                        BOMItem bomItem = iterBoms.next();
                        Long childPartTypeId = bomItem.getChild().getPartType().getId();
                        if (childPartTypeId.longValue() == primaryPartTypeId.longValue()) {
                            String strJsonBom = bomItem.toJson();
                            List<RelatedPart> relatedParts = new ArrayList<>(2);
                            relatedParts.add(new RelatedPart(primaryPartId, BOM_PARENT));
                            relatedParts.add(new RelatedPart(bomItem.getChild().getId(), BOM_CHILD));
                            changelogService.log(BOM, "Deleted BOM item: " + formatBOMItem(bomItem), strJsonBom,
                                    relatedParts);
                            iterBoms.remove();
                            bomItemDao.remove(bomItem);
                        }
                    }
                }
                // Add the primary part to the list of BOMs of the picked part.
                _create(httpRequest, pickedPartId, primaryPartId, r.getQuantity(), request.getSourcesIds(),
                        request.getChlogSrcRatings(), request.getChlogSrcLnkDescription(), request.getAttachIds());
                added++;
            } catch (FoundBomRecursionException e) {
                log.debug("Adding of the part [" + primaryPartId + "] to list of BOM for part [" + pickedPartId
                        + "] failed.", e);
                failures.add(new AddToParentBOMsResponse.Failure(pickedPartId, pickedPart.getPartType().getName(),
                        pickedPart.getManufacturerPartNumber(), r.getQuantity(), "Recursion check failed."));
            } catch (AssertionError e) {
                log.debug("Adding of the part [" + primaryPartId + "] to list of BOM for part [" + pickedPartId
                        + "] failed.", e);
                failures.add(new AddToParentBOMsResponse.Failure(pickedPartId, pickedPart.getPartType().getName(),
                        pickedPart.getManufacturerPartNumber(), r.getQuantity(), e.getMessage()));
            }
        }
        List<BOMItem> parents = getParentsForBom(primaryPartId);
        rebuildBomDescendancyForPart(primaryPartId, true);
        return new BOMService.AddToParentBOMsResponse(added, failures, parents);
    }

    @Transactional
    public void update(Long id, Integer quantity) {
        // Get the item
        BOMItem item = bomItemDao.findOne(id);
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(item.getParent().getId(), BOM_PARENT));
        relatedParts.add(new RelatedPart(item.getChild().getId(), BOM_CHILD));
        changelogService.log(BOM, "Changed BOM " + formatBOMItem(item) + " quantity to " + quantity, item.toJson(),
                relatedParts);
        // Update
        item.setQuantity(quantity);
        bomItemDao.merge(item);
    }

    @Transactional
    public void delete(Long id) {
        // Get the object
        BOMItem item = bomItemDao.findOne(id);
        Part parent = item.getParent();
        Long parentPartId = parent.getId();
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(parentPartId, BOM_PARENT));
        relatedParts.add(new RelatedPart(item.getChild().getId(), BOM_CHILD));
        changelogService.log(BOM, "Deleted BOM item: " + formatBOMItem(item), relatedParts);
        // Delete it.
        jdbcTemplate.update("delete from bom where id=?", id);
        bomItemDao.flush();
        rebuildBomDescendancyForPart(parentPartId, true);
    }

    /**
     * Check that two specified parts has no any cycled recursions:
     * <ul>
     * <li>not in a BOMs tree of the parentPart</li>
     * <li>not in a BOMs tree of the childPart</li>
     * <li>not in a union of these BOMs trees</li>
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
        for (Iterator<Long> parentIter = parentBoms.iterator(); parentIter.hasNext();) {
            Long id = parentIter.next();
            if (binarySearch(childBoms, id) >= 0) {
                throw new FoundBomRecursionException(id);
            }
        }
    }

    /**
     * Load IDs of all parts which are BOMs for the specified part.
     *
     * The method recursively walks a tree of BOMs of the specified part and
     * fills a sorted list of IDs of the found parts. The list will also include
     * an ID of the specified part.
     *
     * @param part
     *            a part to load its BOMs
     * @return (ascending) ordered list of IDs of parts which are BOM for the
     *         specified part. The list also includes ID of the part.
     * @throws FoundBomRecursionException
     *             if the BOM's tree contains cycled recursion
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
                    postponed.pop(); // remove from the stack this exhausted
                                     // iterator
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
