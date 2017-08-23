package com.turbointernational.metadata.service;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.BOM;
import static com.turbointernational.metadata.util.FormatUtils.formatBom;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetBomsResponse;
import com.turbointernational.metadata.service.ArangoDbConnectorService.Response;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.Bom;

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
    private ChangelogSourceService changelogSourceService;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private PartChangeService partChangeService;

    @Autowired
    private ArangoDbConnectorService arangoDbConnector;

    @PostConstruct
    public void init() {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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
         * IDs of uploaded files which should be attached to this changelog. See
         * ticket #933 for details.
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
        private Bom[] boms;

        public CreateBOMsResponse(List<Failure> failures, Bom[] boms) {
            this.failures = failures;
            this.boms = boms;
        }

        public List<Failure> getFailures() {
            return failures;
        }

        public void setFailures(List<Failure> failures) {
            this.failures = failures;
        }

        public Bom[] getBoms() {
            return boms;
        }

        public void setBoms(Bom[] boms) {
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
         * IDs of uploaded files which should be attached to this changelog. See
         * ticket #933 for details.
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

    @Transactional(noRollbackFor = { FoundBomRecursionException.class, AssertionError.class })
    public CreateBOMsResponse createBOMs(HttpServletRequest httpRequest, CreateBOMsRequest request) throws Exception {
        Long parentPartId = request.getParentPartId();
        Long[] sourcesIds = request.getSourcesIds();
        Integer[] ratings = request.getChlogSrcRatings();
        String description = request.getChlogSrcLnkDescription();
        Long[] attachIds = request.getAttachIds();
        // TODO: GetPartResponse parent =
        // arangoDbConnector.findPartById(parentPartId);
        Part parent = partDao.findOne(parentPartId);
        List<CreateBOMsResponse.Failure> failures = new ArrayList<>();
        Collection<Long> relatedPartIds = new ArrayList<Long>(request.getRows().size());
        for (CreateBOMsRequest.Row row : request.getRows()) {
            // Create a new BOM item
            Long childId = row.getChildPartId();
            // TODO: GetPartResponse child =
            // arangoDbConnector.findPartById(childId);
            Part child = partDao.findOne(childId);
            // TODO: String childPartNum = child.getPartNumber();
            String childPartNum = child.getManufacturerPartNumber();
            if (child.getManufacturer().getId() != parent.getManufacturer().getId()) {
                log.debug(
                        "Adding of the part [" + childId + "] to list of BOM for part [" + parentPartId + "] failed. ");
                failures.add(new CreateBOMsResponse.Failure(childId, child.getPartType().getName(), childPartNum,
                        row.getQuantity(), "Child part must have the same manufacturer as the parent part."));
            } else {
                Response response = arangoDbConnector.addPartToBom(parentPartId, childId, row.getQuantity());
                if (response.isSuccess()) {
                    relatedPartIds.add(childId);
                    // Save to the changelog.
                    List<RelatedPart> relatedParts = new ArrayList<>(2);
                    relatedParts.add(new RelatedPart(parentPartId, ChangelogPart.Role.BOM_PARENT));
                    relatedParts.add(new RelatedPart(childId, ChangelogPart.Role.BOM_CHILD));
                    Changelog changelog = changelogService.log(BOM, "Added bom item: " + formatBom(parent,
                            child) /*
                                    * TODO: + formatBom(parent, child,
                                    * row.getQuantity())
                                    */, null, relatedParts);
                    changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
                } else {
                    log.debug("Adding of the part [" + childId + "] to list of BOM for part [" + parentPartId
                            + "] failed.");
                    failures.add(new CreateBOMsResponse.Failure(childId, child.getPartType().getName(), childPartNum,
                            row.getQuantity(), response.getMsg()));

                }
            }
        }
        GetBomsResponse bomsResponse = arangoDbConnector.getBoms(parentPartId);
        Bom[] boms = Bom.from(bomsResponse.getRows());
        partChangeService.addedBoms(parentPartId, relatedPartIds);
        return new CreateBOMsResponse(failures, boms);
    }

    public GetBomsResponse.Row[] getByParentId(Long partId) throws Exception {
        GetBomsResponse restBoms = arangoDbConnector.getBoms(partId);
        return restBoms.getRows();
    }

    public List<BOMItem> getParentsForBom(Long partId) throws Exception {
        // return bomItemDao.findParentsForBom(partId);
        // TODO: implementation
        return null;
    }

    public List<BOMItem> getByParentAndTypeIds(Long partId, Long partTypeId) throws Exception {
        // return bomItemDao.findByParentAndTypeIds(partId, partTypeId);
        // TODO: implementation
        return null;
    }

    @Transactional(noRollbackFor = { FoundBomRecursionException.class, AssertionError.class }, propagation = REQUIRED)
    public AddToParentBOMsResponse addToParentsBOMs(HttpServletRequest httpRequest, Long primaryPartId,
            AddToParentBOMsRequest request) throws Exception {
        // int added = 0;
        // Part primaryPart = partDao.findOne(primaryPartId);
        // Long primaryPartTypeId = primaryPart.getPartType().getId();
        // Long primaryPartManufacturerId =
        // primaryPart.getManufacturer().getId();
        // List<BOMService.AddToParentBOMsRequest.Row> rows = request.getRows();
        // List<AddToParentBOMsResponse.Failure> failures = new ArrayList<>(10);
        // List<Part> affectedParts = new ArrayList<>(10);
        // for (AddToParentBOMsRequest.Row r : rows) {
        // Long pickedPartId = r.getPartId();
        // Part pickedPart = partDao.findOne(pickedPartId);
        // try {
        // Long pickedPartManufacturerId = pickedPart.getManufacturer().getId();
        // if (primaryPartManufacturerId != pickedPartManufacturerId) {
        // throw new AssertionError(String.format(
        // "Part type '%1$s' of the part [%2$d] - {%3$s} "
        // + "does not match with part type '{%4$s}' of the part [{%5$d}] -
        // {%6$s}.",
        // primaryPart.getPartType().getName(), primaryPartId,
        // primaryPart.getManufacturerPartNumber(),
        // pickedPart.getPartType().getName(), pickedPartId,
        // pickedPart.getManufacturerPartNumber()));
        // }
        // if (r.getResolution() == REPLACE) {
        // // Remove existing BOMs in the picked part.
        // for (Iterator<BOMItem> iterBoms = pickedPart.getBom().iterator();
        // iterBoms.hasNext();) {
        // BOMItem bomItem = iterBoms.next();
        // Long childPartTypeId = bomItem.getChild().getPartType().getId();
        // if (childPartTypeId.longValue() == primaryPartTypeId.longValue()) {
        // String strJsonBom = bomItem.toJson();
        // List<RelatedPart> relatedParts = new ArrayList<>(2);
        // relatedParts.add(new RelatedPart(primaryPartId, BOM_PARENT));
        // relatedParts.add(new RelatedPart(bomItem.getChild().getId(),
        // BOM_CHILD));
        // changelogService.log(BOM, "Deleted BOM item: " +
        // formatBOMItem(bomItem), strJsonBom,
        // relatedParts);
        // iterBoms.remove();
        // bomItemDao.remove(bomItem);
        // }
        // }
        // }
        // // Add the primary part to the list of BOMs of the picked part.
        // _create(httpRequest, pickedPartId, primaryPartId, r.getQuantity(),
        // request.getSourcesIds(),
        // request.getChlogSrcRatings(), request.getChlogSrcLnkDescription(),
        // request.getAttachIds());
        // added++;
        // affectedParts.add(pickedPart);
        // } catch (FoundBomRecursionException e) {
        // log.debug("Adding of the part [" + primaryPartId + "] to list of BOM
        // for part [" + pickedPartId
        // + "] failed.", e);
        // failures.add(new AddToParentBOMsResponse.Failure(pickedPartId,
        // pickedPart.getPartType().getName(),
        // pickedPart.getManufacturerPartNumber(), r.getQuantity(), "Recursion
        // check failed."));
        // } catch (AssertionError e) {
        // log.debug("Adding of the part [" + primaryPartId + "] to list of BOM
        // for part [" + pickedPartId
        // + "] failed.", e);
        // failures.add(new AddToParentBOMsResponse.Failure(pickedPartId,
        // pickedPart.getPartType().getName(),
        // pickedPart.getManufacturerPartNumber(), r.getQuantity(),
        // e.getMessage()));
        // }
        // }
        // List<BOMItem> parents = getParentsForBom(primaryPartId);
        // //rebuildBomDescendancyForPart(primaryPartId, true);
        // List<Long> affectedPartIds = affectedParts.stream().map(p ->
        // p.getId()).collect(Collectors.toList());
        // // In the call below primaryPartId is actually childPartId from point
        // of view partChangeService.
        // partChangeService.addedToParentBoms(primaryPartId, affectedPartIds);
        // return new BOMService.AddToParentBOMsResponse(added, failures,
        // parents);
        // TODO: implementation
        return null;
    }

    public void update(Long parentPartId, Long childPartId, Integer quantity) throws IOException {
        Part parent = partDao.findOne(parentPartId);
        Part child = partDao.findOne(childPartId);
        // Update the changelog.
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(parentPartId, ChangelogPart.Role.BOM_PARENT));
        relatedParts.add(new RelatedPart(childPartId, ChangelogPart.Role.BOM_CHILD));
        changelogService.log(BOM, "Changed BOM " + formatBom(parent, child) + " quantity to " + quantity, null,
                relatedParts);
        // Update
        arangoDbConnector.modifyPartInBom(parentPartId, childPartId, quantity);
        partChangeService.updatedBom(parent.getId());
    }

    public Bom[] delete(Long parentPartId, Long childPartId) throws IOException {
        Part parentPart = partDao.findOne(parentPartId);
        Part childPart = partDao.findOne(childPartId);
        // Delete it.
        arangoDbConnector.removePartFromBom(parentPartId, childPartId);
        // Notify about changes.
        partChangeService.deletedBom(parentPartId, childPartId);
        // Update the changelog.
        List<RelatedPart> relatedParts = new ArrayList<>(2);
        relatedParts.add(new RelatedPart(parentPartId, ChangelogPart.Role.BOM_PARENT));
        relatedParts.add(new RelatedPart(childPartId, ChangelogPart.Role.BOM_CHILD));
        changelogService.log(BOM, "Deleted BOM item: " + formatBom(parentPart, childPart), relatedParts);
        // Return list of BOMs after this delete operation.
        GetBomsResponse bomsResponse = arangoDbConnector.getBoms(parentPartId);
        return Bom.from(bomsResponse.getRows());
    }

}
