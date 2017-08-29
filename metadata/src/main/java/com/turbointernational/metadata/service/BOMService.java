package com.turbointernational.metadata.service;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.BOM;
import static com.turbointernational.metadata.service.ArangoDbConnectorService.checkSuccess;
import static com.turbointernational.metadata.service.BOMService.AddToParentBOMsRequest.ResolutionEnum.REPLACE;
import static com.turbointernational.metadata.util.FormatUtils.formatBom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.service.ArangoDbConnectorService.CreateAltBomResponse;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetAltBomsResponse;
import com.turbointernational.metadata.service.ArangoDbConnectorService.GetBomsResponse;
import com.turbointernational.metadata.service.ArangoDbConnectorService.Response;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.AltBom;
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

    @PersistenceContext(unitName = "metadata")
    protected EntityManager em;

    @Autowired
    private PartDao partDao;

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private PartChangeService partChangeService;

    @Autowired
    private ArangoDbConnectorService arangoDbConnector;

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

    private boolean _create(HttpServletRequest httpRequest, Part parentPart, Part childPart, Integer quantity,
            List<CreateBOMsResponse.Failure> failures, Long[] sourcesIds, Integer[] ratings, String description,
            Long[] attachIds) throws DataAccessResourceFailureException, JsonProcessingException {
        Long parentPartId = parentPart.getId();
        Long childPartId = childPart.getId();
        if (childPart.getManufacturer().getId() != parentPart.getManufacturer().getId()) {
            String errMsg = "Child part must have the same manufacturer as the Parent part.";
            failures.add(new CreateBOMsResponse.Failure(childPartId, childPart.getPartType().getName(),
                    childPart.getManufacturerPartNumber(), quantity, errMsg));
            return false;
        }
        Response response = arangoDbConnector.addPartToBom(parentPartId, childPartId, quantity);
        if (response.isSuccess()) {
            // Save to the changelog.
            List<RelatedPart> relatedParts = new ArrayList<>(2);
            relatedParts.add(new RelatedPart(parentPartId, ChangelogPart.Role.BOM_PARENT));
            relatedParts.add(new RelatedPart(childPartId, ChangelogPart.Role.BOM_CHILD));
            Changelog changelog = changelogService.log(BOM,
                    "Added bom item: " + formatBom(parentPart, childPart, quantity), null, relatedParts);
            changelogSourceService.link(httpRequest, changelog, sourcesIds, ratings, description, attachIds);
        } else {
            log.debug(
                    "Adding of the part [" + childPartId + "] to list of BOM for part [" + parentPartId + "] failed.");
            failures.add(new CreateBOMsResponse.Failure(childPartId, childPart.getPartType().getName(),
                    childPart.getManufacturerPartNumber(), quantity, response.getMsg()));
        }
        return true;
    }

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
        Collection<Long> relatedPartIds = new ArrayList<Long>(request.getRows().size() + 1);
        for (CreateBOMsRequest.Row row : request.getRows()) {
            // Create a new BOM item
            Long childId = row.getChildPartId();
            // TODO: GetPartResponse child =
            // arangoDbConnector.findPartById(childId);
            Part child = partDao.findOne(childId);
            // TODO: String childPartNum = child.getPartNumber();
            boolean created = _create(httpRequest, parent, child, row.getQuantity(), failures, sourcesIds, ratings,
                    description, attachIds);
            if (created) {
                relatedPartIds.add(childId);
            }
        }
        GetBomsResponse bomsResponse = arangoDbConnector.getBoms(parentPartId);
        partChangeService.addedBoms(parentPartId, relatedPartIds);
        Bom[] boms = Bom.from(bomsResponse.getRows());
        return new CreateBOMsResponse(failures, boms);
    }

    public GetBomsResponse.Row[] getByParentId(Long partId) throws Exception {
        GetBomsResponse restBoms = arangoDbConnector.getBoms(partId);
        return restBoms.getRows();
    }

    public GetBomsResponse.Row[] getParentsForBom(Long partId) throws Exception {
        GetBomsResponse restBoms = arangoDbConnector.getParentsBoms(partId);
        return restBoms.getRows();
    }

    public GetBomsResponse.Row[] getByParentAndTypeIds(Long partId, Long partTypeId) throws Exception {
        // TODO: this method should be implemented on the service side
        GetBomsResponse.Row[] parents = getParentsForBom(partId);
        int n = parents.length;
        List<GetBomsResponse.Row> filtered = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            GetBomsResponse.Row row = parents[i];
            if (row.getPartType().getId().equals(partTypeId)) {
                filtered.add(row);
            }
        }
        GetBomsResponse.Row[] retVal = new GetBomsResponse.Row[filtered.size()];
        filtered.toArray(retVal);
        return retVal;
    }

    public CreateBOMsResponse addToParentsBOMs(HttpServletRequest httpRequest, Long primaryPartId,
            AddToParentBOMsRequest request) throws Exception {
        Part primaryPart = partDao.findOne(primaryPartId);
        Long primaryPartTypeId = primaryPart.getPartType().getId();
        Long primaryPartManufacturerId = primaryPart.getManufacturer().getId();
        List<BOMService.AddToParentBOMsRequest.Row> rows = request.getRows();
        List<CreateBOMsResponse.Failure> failures = new ArrayList<>(10);
        Collection<Long> relatedPartIds = new ArrayList<Long>(request.getRows().size() + 1);
        for (AddToParentBOMsRequest.Row r : rows) {
            Long pickedPartId = r.getPartId();
            GetBomsResponse.Row[] pickedPartBoms = arangoDbConnector.getBoms(pickedPartId).getRows();
            Part pickedPart = partDao.findOne(pickedPartId);
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
                for (int i = 0; i < pickedPartBoms.length; i++) {
                    GetBomsResponse.Row row = pickedPartBoms[i];
                    Long childPartTypeId = row.getPartType().getId();
                    if (childPartTypeId.longValue() == primaryPartTypeId.longValue()) {
                        List<RelatedPart> relatedParts = new ArrayList<>(2);
                        relatedParts.add(new RelatedPart(primaryPartId, ChangelogPart.Role.BOM_PARENT));
                        relatedParts.add(new RelatedPart(row.getPartId(), ChangelogPart.Role.BOM_CHILD));
                        Response response = arangoDbConnector.removePartFromBom(pickedPartId, primaryPartId);
                        checkSuccess(response);
                        String txtAudit = row.toAuditLog();
                        changelogService.log(BOM,
                                "Deleted BOM item: " + formatBom(pickedPart, primaryPart, row.getQty()), txtAudit,
                                relatedParts);
                    }
                }
            }
            boolean created = _create(httpRequest, pickedPart, primaryPart, r.getQuantity(), failures, null, null, null,
                    null);
            if (created) {
                relatedPartIds.add(pickedPartId);
            }
        }
        GetBomsResponse.Row[] parents = getParentsForBom(primaryPartId);
        // In the call below primaryPartId is actually childPartId from point of
        // view partChangeService.
        partChangeService.addedToParentBoms(primaryPartId, relatedPartIds);
        Bom[] boms = Bom.from(parents);
        return new BOMService.CreateBOMsResponse(failures, boms);
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
        Response response = arangoDbConnector.modifyPartInBom(parentPartId, childPartId, quantity);
        checkSuccess(response);
        partChangeService.updatedBom(parent.getId());
    }

    public Bom[] delete(Long parentPartId, Long childPartId) throws IOException {
        Part parentPart = partDao.findOne(parentPartId);
        Part childPart = partDao.findOne(childPartId);
        // Delete it.
        Response response = arangoDbConnector.removePartFromBom(parentPartId, childPartId);
        checkSuccess(response);
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

    public AltBom getAlternatives(Long parentPartId, Long childPartId) {
        GetAltBomsResponse altBomsResponse = arangoDbConnector.getAltBoms(parentPartId, childPartId);
        return AltBom.from(altBomsResponse);
    }

    public Long createAltBom(Long parentPartId, Long childPartId, Long partId) throws JsonProcessingException {
        CreateAltBomResponse response = arangoDbConnector.createAltBom(parentPartId, childPartId, partId);
        return response.getAltHeaderId();
    }

}
