package com.turbointernational.metadata.web.controller;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.web.controller.PartController.GasketKitResultStatus.ASSERTION_ERROR;
import static com.turbointernational.metadata.web.controller.PartController.GasketKitResultStatus.OK;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.TurboTypeDao;
import com.turbointernational.metadata.entity.TurboType;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.service.GraphDbService.GetAncestorsResponse;
import com.turbointernational.metadata.service.InterchangeService;
import com.turbointernational.metadata.service.PartService;
import com.turbointernational.metadata.service.PriceService;
import com.turbointernational.metadata.service.StandardOversizePartService;
import com.turbointernational.metadata.service.StandardOversizePartService.CreateStandardOversizePartRequest;
import com.turbointernational.metadata.service.StandardOversizePartService.CreateStandardOversizePartResponse;
import com.turbointernational.metadata.util.View;
import com.turbointernational.metadata.web.dto.AlsoBought;
import com.turbointernational.metadata.web.dto.Page;
import com.turbointernational.metadata.web.dto.ProductPricesDto;

@RequestMapping(value = "/metadata")
@RestController
public class PartController {

    @Autowired
    private PartService partService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private StandardOversizePartService standardOversizePartService;

    @Autowired
    private TurboTypeDao turboTypeDao;

    @Autowired
    private PartDao partDao;

    @Autowired
    private InterchangeService interchangeService;

    static class PartCreateRequest {

        @JsonView({ View.Summary.class })
        private Part origin;

        @JsonView({ View.Summary.class })
        private List<String> partNumbers;

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

        public Part getOrigin() {
            return origin;
        }

        public void setOrigin(Part origin) {
            this.origin = origin;
        }

        public List<String> getPartNumbers() {
            return partNumbers;
        }

        public void setPartNumbers(List<String> partNumbers) {
            this.partNumbers = partNumbers;
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
    public static class PartCreateResponse {

        @JsonInclude(ALWAYS)
        public static class Row {

            @JsonView({ View.Summary.class })
            private final Long partId;

            @JsonView({ View.Summary.class })
            private final String manufacturerPartNumber;

            @JsonView({ View.Summary.class })
            private final boolean success;

            @JsonView({ View.Summary.class })
            private final String errorMessage;

            public Row(Long partId, String manufacturerPartNumber, boolean success, String errorMessage) {
                this.partId = partId;
                this.manufacturerPartNumber = manufacturerPartNumber;
                this.success = success;
                this.errorMessage = errorMessage;
            }

            public Long getPartId() {
                return partId;
            }

            public String getManufacturerPartNumber() {
                return manufacturerPartNumber;
            }

            public boolean isSuccess() {
                return success;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

        }

        PartCreateResponse(List<Row> results) {
            this.results = results;
        }

        @JsonView({ View.Summary.class })
        private List<Row> results;

        public List<Row> getResults() {
            return results;
        }

        public void setResults(List<Row> results) {
            this.results = results;
        }

    }

    static class XRefPartCreateRequest {

        @JsonView(View.Summary.class)
        private Long originalPartId;

        @JsonView(View.Summary.class)
        private Part part;

        public Long getOriginalPartId() {
            return originalPartId;
        }

        public void setOriginalPartId(Long originalPartId) {
            this.originalPartId = originalPartId;
        }

        public Part getPart() {
            return part;
        }

        public void setPart(Part part) {
            this.part = part;
        }

    }

    static class LinkTurboRequest {

        @JsonView({ View.Summary.class })
        private Long gasketKitId;

        @JsonView({ View.Summary.class })
        private List<Long> pickedTurbos;

        public Long getGasketKitId() {
            return gasketKitId;
        }

        public void setGasketKitId(Long gasketKitId) {
            this.gasketKitId = gasketKitId;
        }

        public List<Long> getPickedTurbos() {
            return pickedTurbos;
        }

        public void setPickedTurbos(List<Long> pickedTurbos) {
            this.pickedTurbos = pickedTurbos;
        }
    }

    @JsonInclude(ALWAYS)
    public static class LinkTurboResponse {

        @JsonInclude(ALWAYS)
        public static class Row {

            @JsonView({ View.Summary.class })
            private final Long partId; // actually the partId is ID of a turbo

            @JsonView({ View.Summary.class })
            private final String manufacturerPartNumber;

            @JsonView({ View.Summary.class })
            private final boolean success;

            @JsonView({ View.Summary.class })
            private final String errorMessage;

            public Row(Long partId, String manufacturerPartNumber, boolean success, String errorMessage) {
                this.partId = partId;
                this.manufacturerPartNumber = manufacturerPartNumber;
                this.success = success;
                this.errorMessage = errorMessage;
            }

            public Long getPartId() {
                return partId;
            }

            public String getManufacturerPartNumber() {
                return manufacturerPartNumber;
            }

            public boolean isSuccess() {
                return success;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

        }

        @JsonView({ View.Summary.class })
        private List<Row> rows;

        @JsonView({ View.Summary.class })
        private List<Turbo> turbos;

        LinkTurboResponse(List<Row> rows, List<Turbo> turbos) {
            this.rows = rows;
            this.turbos = turbos;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

        public List<Turbo> getTurbos() {
            return turbos;
        }

        public void setTurbos(List<Turbo> turbos) {
            this.turbos = turbos;
        }

    }

    //@PreAuthorize("hasRole('ROLE_READ') or hasIpAddress('127.0.0.1/32')")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Part getPart(@PathVariable("id") Long id) {
        Part part = partDao.findOne(id);
        interchangeService.initInterchange(part);
        return part;
    }

    @JsonView(View.Summary.class)
    @RequestMapping(value = "/part/{id}/prices", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody ProductPricesDto getPrices(HttpServletRequest httpRequest, @PathVariable("id") Long id) {
        // Prices for a part is loading during initialization of the Angular's
        // controller for a part view.
        // But at that initialization we have no reliable way in a JavaScript
        // code to check if an user has permission
        // to read prices or not. Loading prices without the permission will
        // lead to login page instead of
        // the part view (as consequence a browser is redirected on a login page).
        // So, to avoid this issue we make permission check on
        // the server side and just return null
        // when user has no the required permission.
        if (httpRequest.isUserInRole("ROLE_PRICE_READ")) {
            return priceService.getProductPricesById(id);
        } else {
            return null;
        }
    }

    @JsonView(View.Summary.class)
    @ResponseBody
    @RequestMapping(value = "/part/{id}/oversize/list", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<Part> findOversizeParts(@PathVariable("id") Long partId) {
        List<Part> retVal = standardOversizePartService.findOversizeParts(partId);
        return retVal;
    }

    @JsonView(View.Summary.class)
    @ResponseBody
    @RequestMapping(value = "/part/{id}/standard/list", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<Part> findStandardParts(@PathVariable("id") Long partId) {
        List<Part> retVal = standardOversizePartService.findStandardParts(partId);
        return retVal;
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    @ResponseBody
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/part/standardoversize", method = POST)
    public CreateStandardOversizePartResponse createStandardOversizePart(
            @RequestBody CreateStandardOversizePartRequest request) throws Exception {
        return standardOversizePartService.create(request);
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/part/standardoversize/{standardPartId}/{oversizePartId}", method = DELETE)
    public void deleteStandardOversizePart(@PathVariable("standardPartId") Long standardPartId,
            @PathVariable("oversizePartId") Long oversizePartId) {
        standardOversizePartService.delete(standardPartId, oversizePartId);
    }

    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/numbers", method = GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Part findByPartNumber(@RequestParam(name = "mid") Long manufacturerId,
            @RequestParam(name = "pn") String partNumber) {
        Part part = partDao.findByPartNumberAndManufacturer(manufacturerId, partNumber);
        interchangeService.initInterchange(part);
        return part;
    }

    @JsonView(View.Summary.class)
    @RequestMapping(value = "/part/alsobought", method = GET, produces = APPLICATION_JSON_VALUE)
    public Page<AlsoBought> filterAlsoBough(HttpServletRequest httpRequest, @RequestParam String manufacturerPartNumber,
            @RequestParam(required = false) String fltrManufacturerPartNumber,
            @RequestParam(required = false) String fltrPartTypeValue,
            @RequestParam(required = false) String sortProperty, @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "10") Integer limit) {
        if (httpRequest.isUserInRole("ROLE_PRICE_READ")) {
            // See comment in the method Price#getPrices() for the answer why
            // permission is checked here.
            return partService.filterAlsoBough(manufacturerPartNumber, fltrManufacturerPartNumber, fltrPartTypeValue,
                    sortProperty, sortOrder, offset, limit);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/part/{id}/ancestors", method = GET)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_READ")
    public GetAncestorsResponse.Row[] ancestors(@PathVariable("id") Long partId) throws Exception {
        return partService.ancestors(partId).getRows();
    }

    @Transactional
    @Secured("ROLE_CREATE_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part", method = POST)
    public @ResponseBody PartCreateResponse createPart(HttpServletRequest httpRequest,
            @RequestBody PartCreateRequest request) throws Exception {
        Part origin = request.getOrigin();
        List<String> partNumbers = request.getPartNumbers();
        Long[] sourcesIds = request.getSourcesIds();
        Integer[] ratings = request.getChlogSrcRatings();
        String description = request.getChlogSrcLnkDescription();
        Long[] attachIds = request.getAttachIds();
        List<PartCreateResponse.Row> responseRows = partService.createPart(httpRequest, origin, partNumbers, sourcesIds,
                ratings, description, attachIds);
        return new PartCreateResponse(responseRows);
    }

    @Transactional
    @Secured("ROLE_CREATE_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/xrefpart", method = POST)
    public @ResponseBody Part createXRefPart(@RequestBody XRefPartCreateRequest request) throws Exception {
        Part retVal = partService.createXRefPart(request.getOriginalPartId(), request.getPart());
        return retVal;
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{id}/details", method = PUT, produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody Part updatePartDetails(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Part part, @PathVariable("id") Long id) throws IOException {
        try {
            String manfrPartNum = part.getManufacturerPartNumber();
            Long manfrId = part.getManufacturer().getId();
            String name = part.getName();
            String description = part.getDescription();
            Boolean inactive = part.getInactive();
            Double dimLength = part.getDimLength();
            Double dimWidth = part.getDimWidth();
            Double dimHeight = part.getDimHeight();
            Double weight = part.getWeight();
            return partService.updatePartDetails(request, id, manfrPartNum, manfrId, name, description, inactive,
                    dimLength, dimWidth, dimHeight, weight);
        } catch (SecurityException e) {
            response.sendError(SC_FORBIDDEN, e.toString());
            return null;
        }
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{id}", method = PUT, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody Part updatePart(HttpServletRequest request, HttpServletResponse response,
            @RequestBody Part part, @PathVariable("id") Long id) throws IOException {
        try {
            return partService.updatePart(request, id, part);
        } catch (AssertionError e) {
            response.sendError(SC_BAD_REQUEST, e.toString());
            return null;
        } catch (SecurityException e) {
            response.sendError(SC_FORBIDDEN, e.toString());
            return null;
        }
    }

    @Transactional
    @RequestMapping(value = "/part/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_DELETE_PART")
    public void deletePart(@PathVariable("id") Long id) {
        partService.deletePart(id);
    }

    @Transactional
    @RequestMapping(value = "/part/{id}/image", method = POST)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<String> addProductImage(@PathVariable Long id, @RequestParam Boolean publish,
            @RequestBody byte[] imageData) throws Exception {
        ProductImage productImage = partService.addProductImage(id, publish, imageData);
        return new ResponseEntity<>(productImage.toJson(), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/part/{id}/cdlegend/image", method = POST, produces = APPLICATION_JSON_VALUE)
    @JsonView(View.Summary.class)
    @Secured("ROLE_PART_IMAGES")
    public @ResponseBody Part addCriticalDimensionLegendImage(@PathVariable Long id,
            @RequestPart("file") @Valid @NotNull @NotBlank MultipartFile mpf) throws Exception {
        byte[] imageData = mpf.getBytes();
        Part part = partService.addCriticalDimensionLegendImage(id, imageData);
        return part;
    }

    @Transactional
    @RequestMapping(value = "/part/{partId}/turboType/{turboTypeId}", method = POST)
    @Secured("ROLE_ALTER_PART")
    public void addTurboType(@PathVariable("partId") Long partId, @PathVariable("turboTypeId") Long turboTypeId) {
        Part part = partDao.findOne(partId);
        TurboType turboType = turboTypeDao.findOne(turboTypeId);
        part.getTurboTypes().add(turboType);
        partDao.merge(part);
    }

    @Transactional
    @RequestMapping(value = "/part/{partId}/turboType/{turboTypeId}", method = DELETE)
    @Secured("ROLE_ALTER_PART")
    public void deleteTurboType(@PathVariable("partId") Long partId, @PathVariable("turboTypeId") Long turboTypeId) {
        partService.deleteTurboType(partId, turboTypeId);
    }

    enum GasketKitResultStatus {
        OK, ASSERTION_ERROR
    }

    static class GasketKitResult {

        @JsonView(View.Summary.class)
        private final GasketKitResultStatus status;

        @JsonView(View.Summary.class)
        private final String message;

        GasketKitResult() {
            this.status = OK;
            this.message = null;
        }

        GasketKitResult(AssertionError e) {
            this.status = ASSERTION_ERROR;
            this.message = e.getMessage();
        }

    }

    @Transactional(noRollbackFor = AssertionError.class)
    @RequestMapping(value = "/part/{partId}/gasketkit/{gasketkitId}", method = PUT, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_ALTER_PART")
    public GasketKitResult setGasketKitForPart(@PathVariable("partId") Long partId,
            @PathVariable("gasketkitId") Long gasketkitId) {
        try {
            partService.linkGasketKitToTurbo(gasketkitId, partId);
            return new GasketKitResult();
        } catch (AssertionError e) {
            return new GasketKitResult(e);
        }
    }

    @Transactional
    @RequestMapping(value = "/part/{partId}/gasketkit", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_ALTER_PART")
    public Turbo clearGasketKitInPart(@PathVariable("partId") long partId) {
        return partService.clearGasketKitInPart(partId);
    }

    @Transactional
    @RequestMapping(value = "/part/{gasketKitId}/gasketkit/turbos", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.Summary.class)
    public List<Turbo> listTurbosLinkedToGasketKit(@PathVariable("gasketKitId") Long gasketKitId) {
        return partDao.listTurbosLinkedToGasketKit(gasketKitId);
    }

    @Transactional
    @RequestMapping(value = "/part/{partId}/gasketkit2", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_ALTER_PART")
    public List<Turbo> unlinkTurboInGasketKit(@PathVariable("partId") Long partId) {
        return partService.unlinkTurboInGasketKit(partId);
    }

}
