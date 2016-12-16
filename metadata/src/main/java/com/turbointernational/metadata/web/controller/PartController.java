package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.TurboTypeDao;
import com.turbointernational.metadata.entity.BOMAncestor;
import com.turbointernational.metadata.entity.TurboType;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.PartRepository;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.service.BOMService;
import com.turbointernational.metadata.service.ChangelogService;
import com.turbointernational.metadata.service.PartService;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.web.controller.PartController.GasketKitResultStatus.ASSERTION_ERROR;
import static com.turbointernational.metadata.web.controller.PartController.GasketKitResultStatus.OK;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/metadata")
@RestController
public class PartController {

    private final static Logger log = LoggerFactory.getLogger(PartController.class);

    @Autowired
    private PartService partService;

    @Autowired
    private BOMService bomService;

    @Autowired
    private ChangelogService changelogService;
    
    @Autowired
    private TurboTypeDao turboTypeDao;
    
    @Autowired
    private PartDao partDao;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private JdbcTemplate db;

    static class PartCreateRequest {

        @JsonView({View.Summary.class})
        private Part origin;

        @JsonView({View.Summary.class})
        private List<String> partNumbers;


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

    }

    @JsonInclude(ALWAYS)
    public static class PartCreateResponse {

        @JsonInclude(ALWAYS)
        public static class Row {

            @JsonView({View.Summary.class})
            private final Long partId;

            @JsonView({View.Summary.class})
            private final String manufacturerPartNumber;

            @JsonView({View.Summary.class})
            private final boolean success;

            @JsonView({View.Summary.class})
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

        @JsonView({View.Summary.class})
        private List<Row> results;


        public List<Row> getResults() {
            return results;
        }

        public void setResults(List<Row> results) {
            this.results = results;
        }

    }

    static class LinkTurboRequest {

        @JsonView({View.Summary.class})
        private Long gasketKitId;

        @JsonView({View.Summary.class})
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

            @JsonView({View.Summary.class})
            private final Long partId; // actually the partId is ID of a turbo

            @JsonView({View.Summary.class})
            private final String manufacturerPartNumber;

            @JsonView({View.Summary.class})
            private final boolean success;

            @JsonView({View.Summary.class})
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

        @JsonView({View.Summary.class})
        private List<Row> rows;

        @JsonView({View.Summary.class})
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

    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{id}", method = GET,
            produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Part getPart(@PathVariable("id") Long id) {
        Part part = partRepository.findOne(id);
        return part;
    }

    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/numbers", method = GET,
            produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Part findByPartNumber(@RequestParam(name = "mid") Long manufacturerId,
                                 @RequestParam(name = "pn") String partNumber) {
        return partDao.findByPartNumberAndManufacturer(manufacturerId, partNumber);
    }

    @RequestMapping(value="/part/{id}/ancestors", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<String> ancestors(@PathVariable("id") Long partId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<BOMAncestor> ancestors = partService.ancestors(partId);
        String json = new JSONSerializer()
                .transform(new HibernateTransformer(), BOMAncestor.class)
                .include("distance")
                .include("type")
                .include("ancestor.id")
                .include("ancestor.name")
                .include("ancestor.manufacturerPartNumber")
                .include("ancestor.description")
                .include("ancestor.partType.name")
                .include("ancestor.manufacturer.name")
                .exclude("*")
                .serialize(ancestors);
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

    @Transactional
    @Secured("ROLE_CREATE_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part", method = POST)
    public @ResponseBody PartCreateResponse createPart(@RequestBody PartCreateRequest request) throws Exception {
        Part origin = request.getOrigin();
        List<String> partNumbers = request.getPartNumbers();
        List<PartCreateResponse.Row> responseRows = partService.createPart(origin, partNumbers);
        return new PartCreateResponse(responseRows);
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{id}", method = PUT,
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody Part updatePart(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody Part part, @PathVariable("id") Long id) throws IOException {
        try {
            return partService.updatePart(request, id, part);
        } catch(AssertionError e) {
            response.sendError(SC_BAD_REQUEST, e.toString());
            return null;
        } catch(SecurityException e) {
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
    @RequestMapping(value="/part/{id}/image", method = POST)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<String> addProductImage(@PathVariable Long id, @RequestParam Boolean publish,
                                                  @RequestBody byte[] imageData) throws Exception {
        ProductImage productImage = partService.addProductImage(id, publish, imageData);
        return new ResponseEntity<>(productImage.toJson(), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value="/part/{id}/cdlegend/image", method = POST, produces = APPLICATION_JSON_VALUE)
    @JsonView(View.Summary.class)
    @Secured("ROLE_PART_IMAGES")
    public @ResponseBody Part addCriticalDimensionLegendImage(@PathVariable Long id,
                                                  @RequestPart("file") @Valid @NotNull @NotBlank
                                                          MultipartFile mpf) throws Exception {
        byte[] imageData = mpf.getBytes();
        Part part = partService.addCriticalDimensionLegendImage(id, imageData);
        return part;
    }

    @Transactional
    @RequestMapping(value="/part/{partId}/turboType/{turboTypeId}", method= POST)
    @Secured("ROLE_ALTER_PART")
    public void addTurboType(@PathVariable("partId") Long partId, @PathVariable("turboTypeId") Long turboTypeId) {
        Part part = partDao.findOne(partId);
        TurboType turboType = turboTypeDao.findOne(turboTypeId);
        part.getTurboTypes().add(turboType);
        partDao.merge(part);
    }

    @Transactional
    @RequestMapping(value="/part/{partId}/turboType/{turboTypeId}", method= DELETE)
    @Secured("ROLE_ALTER_PART")
    public void deleteTurboType(@PathVariable("partId") Long partId,
                                @PathVariable("turboTypeId") Long turboTypeId) {
        partService.deleteTurboType(partId, turboTypeId);
    }

    enum GasketKitResultStatus { OK, ASSERTION_ERROR }

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

    @Transactional
    @RequestMapping(value="/part/{partId}/gasketkit/{gasketkitId}", method = PUT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_ALTER_PART")
    public GasketKitResult setGasketKitForPart(
            @PathVariable("partId") Long partId,
            @PathVariable("gasketkitId") Long gasketkitId)
    {
        try {
            partService.linkGasketKitToTurbo(gasketkitId, partId);
            return new GasketKitResult();
        } catch(AssertionError e) {
            return new GasketKitResult(e);
        }
    }

    @Transactional
    @RequestMapping(value="/part/{partId}/gasketkit", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_ALTER_PART")
    public Turbo clearGasketKitInPart(@PathVariable("partId") long partId) {
        return partService.clearGasketKitInPart(partId);
    }

    @Transactional
    @RequestMapping(value="/part/{gasketKitId}/gasketkit/turbos", method = GET,
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.Summary.class)
    public List<Turbo> listTurbosLinkedToGasketKit(@PathVariable("gasketKitId") Long gasketKitId) {
        return partDao.listTurbosLinkedToGasketKit(gasketKitId);
    }

    @Transactional
    @RequestMapping(value="/part/{partId}/gasketkit2", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_ALTER_PART")
    public List<Turbo> unlinkTurboInGasketKit(@PathVariable("partId") Long partId) {
        return partService.unlinkTurboInGasketKit(partId);
    }

    @Transactional(noRollbackFor = AssertionError.class)
    @Secured("ROLE_ALTER_PART")
    @JsonView(View.Detail.class)
    @ResponseBody
    @RequestMapping(value="/part/{partId}/gasketkits", method = PUT,
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public LinkTurboResponse linkTurbosToGasketKit(@RequestBody LinkTurboRequest request) {
        Long gasketKitId = request.getGasketKitId();
        List<LinkTurboResponse.Row> rows = partService.linkTurbosToGasketKit(gasketKitId, request.getPickedTurbos());
        List<Turbo> turbos = partDao.listTurbosLinkedToGasketKit(gasketKitId);
        return new LinkTurboResponse(rows, turbos);
    }

}
