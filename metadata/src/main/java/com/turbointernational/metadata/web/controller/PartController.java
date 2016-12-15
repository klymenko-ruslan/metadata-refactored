package com.turbointernational.metadata.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.ProductImageDao;
import com.turbointernational.metadata.dao.TurboTypeDao;
import com.turbointernational.metadata.entity.BOMAncestor;
import com.turbointernational.metadata.entity.TurboType;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.PartRepository;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.entity.part.types.GasketKit;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.service.*;
import com.turbointernational.metadata.util.View;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.apache.commons.io.FileUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.im4java.core.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.PART;
import static com.turbointernational.metadata.entity.PartType.PTID_GASKET_KIT;
import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;
import static com.turbointernational.metadata.service.ImageService.PART_CRIT_DIM_LEGEND_HEIGHT;
import static com.turbointernational.metadata.service.ImageService.PART_CRIT_DIM_LEGEND_WIDTH;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;
import static com.turbointernational.metadata.web.controller.PartController.GasketKitResultStatus.ASSERTION_ERROR;
import static com.turbointernational.metadata.web.controller.PartController.GasketKitResultStatus.OK;
import static java.util.stream.Collectors.toSet;
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
    private CriticalDimensionService criticalDimensionService;
    
    @Autowired
    private PartRepository partRepository;
    
    @Autowired
    private ProductImageDao productImageDao;
    
    @Value("${images.originals}")
    private File originalImagesDir;
    
    @Autowired
    private ImageService imageService;
    
    @Autowired
    private JdbcTemplate db;

    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{id}", method = GET,
            produces = APPLICATION_JSON_VALUE)
    public Part getPart(@PathVariable("id") Long id) {
        Part part = partRepository.findOne(id);
        return part;
    }

    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/numbers", method = GET,
            produces = APPLICATION_JSON_VALUE)
    public Part findByPartNumber(@RequestParam(name = "mid") Long manufacturerId,
                                 @RequestParam(name = "pn") String partNumber) {
        return partDao.findByPartNumberAndManufacturer(manufacturerId, partNumber);
    }

    @RequestMapping(value="/part/{id}/ancestors", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<String> ancestors(@PathVariable("id") long partId) throws Exception {
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        List<BOMAncestor> ancestors = db.query(
              "SELECT DISTINCT\n"
            + "  ba.part_id,\n"
            + "  ba.ancestor_part_id,\n"
            + "  ba.distance,\n"
            + "  ba.type\n"
            + "FROM\n"
            + "  vbom_ancestor ba\n"
            + "  JOIN part ap ON ap.id = ba.ancestor_part_id\n"
            + "WHERE\n"
            + "  ba.part_id = ?\n"
            + "  AND ba.distance > 0\n" // Non-self parts
            + "ORDER BY ba.distance, ba.type, ap.manfr_part_num",
                (rs, rowNum) -> {
                    BOMAncestor ancestor = new BOMAncestor();

                    ancestor.setDistance(rs.getInt("distance"));
                    ancestor.setType(rs.getString("type"));
                    ancestor.setPart(partDao.findOne(rs.getLong("part_id")));
                    ancestor.setAncestor(partDao.findOne(rs.getLong("ancestor_part_id")));

                    return ancestor;
                }, partId);
        
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

    @Transactional
    @Secured("ROLE_CREATE_PART")
    @JsonView(View.Detail.class)
    @ResponseBody
    @RequestMapping(value = "/part", method = POST)
    public PartService.PartCreateResponse createPart(@RequestBody PartCreateRequest request) throws Exception {
        Part origin = request.getOrigin();
        List<String> partNumbers = request.getPartNumbers();
        return partService.createPart(origin, partNumbers);
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/part/{id}", method = PUT,
            produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE)
    public Part updatePart(HttpServletRequest request, HttpServletResponse response,
                           @RequestBody Part part, @PathVariable("id") Long id) throws IOException {
        Errors errors = criticalDimensionService.validateCriticalDimensions(part);
        if (errors.hasErrors()) {
            log.error("Validation critical dimensions for the part (ID: {}) failed. Details: {}", part.getId(), errors);
            response.sendError(SC_BAD_REQUEST, errors.toString());
            return null;
        }
        Part originPart = partDao.findOne(id);
        if (originPart.getManufacturer().getId() != part.getManufacturer().getId() &&
                !request.isUserInRole("ROLE_ALTER_PART_MANUFACTURER")) {
            response.sendError(SC_FORBIDDEN, "You have no permission to modify a manufacturer.");
            return null;
        }
        if (!originPart.getManufacturerPartNumber().equals(part.getManufacturerPartNumber()) &&
                !request.isUserInRole("ROLE_ALTER_PART_NUMBER")) {
            response.sendError(SC_FORBIDDEN, "You have no permission to modify a manufacturer.");
            return null;
        }
        String originalPartJson = originPart.toJson(criticalDimensionService.getCriticalDimensionForPartType(
                part.getPartType().getId()));
        Part retVal = partDao.merge(part);
        // Update the changelog
        changelogService.log(PART, "Updated part " + formatPart(part) + ".", "{original: " +
                originalPartJson + ",updated: " +
                part.toJson(criticalDimensionService.getCriticalDimensionForPartType(part.getPartType().getId())) +
                "}");
        return retVal;
    }

    @Transactional
    @RequestMapping(value = "/part/{id}", method = DELETE,
            produces = APPLICATION_JSON_VALUE)
    @Secured("ROLE_DELETE_PART")
    public void deletePart(@PathVariable("id") Long id) {
        Part part = partDao.findOne(id);
        partDao.merge(part);
        // Update the changelog
        changelogService.log(PART, "Deleted part " + formatPart(part) + ".",
                part.toJson(criticalDimensionService.getCriticalDimensionForPartType(part.getPartType().getId())));
        // Delete the part
        db.update("INSERT INTO `deleted_parts` (id) VALUES(?)", part.getId());
        partDao.remove(part);
    }

    @Transactional
    @RequestMapping(value="/part/{id}/image", method = POST)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<String> addProductImage(@PathVariable Long id, @RequestParam Boolean publish,
                                                  @RequestBody byte[] imageData) throws Exception {
        Part part = partDao.findOne(id);
        // Save the file into the originals directory
        String filename = part.getId().toString() + "_" + System.currentTimeMillis() + ".jpg"; // Good enough
        File originalFile = new File(originalImagesDir, filename);
        FileUtils.writeByteArrayToFile(originalFile, imageData);
        // Create the product image
        ProductImage productImage = new ProductImage();
        productImage.setPublish(publish);
        productImage.setFilename(filename);
        productImage.setPart(part);
        productImageDao.persist(productImage);
        // Generate the resized images.
        try {
            for (int size : ImageService.SIZES) {
                imageService.generateResizedImage(filename, productImage.getFilename(size), size);
            }
        } catch(CommandException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
        return new ResponseEntity<>(productImage.toJson(), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value="/part/{id}/cdlegend/image", method = POST, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Summary.class)
    @Secured("ROLE_PART_IMAGES")
    public Part addCriticalDimensionLegendImage(@PathVariable Long id,
                                                  @RequestPart("file") @Valid @NotNull @NotBlank
                                                          MultipartFile mpf) throws Exception {
        Part part = partDao.findOne(id);
        String currImgFilename = part.getLegendImgFilename();
        if (currImgFilename != null) {
            imageService.delResizedImage(currImgFilename);
        }
        String pidstr = part.getId().toString();
        String now = new Long(System.currentTimeMillis()).toString();
        String filenameOriginal = pidstr + "_cdlgndorig_" + now + ".jpg";
        String filenameScaled = pidstr + "_cdlgnd_" + now + ".jpg";
        File originalFile = new File(originalImagesDir, filenameOriginal);
        mpf.transferTo(originalFile);
        imageService.generateResizedImage(filenameOriginal, filenameScaled,
                PART_CRIT_DIM_LEGEND_WIDTH, PART_CRIT_DIM_LEGEND_HEIGHT, true);
        part.setLegendImgFilename(filenameScaled);
        return part;
    }

    @Transactional
    @RequestMapping(value="/part/{partId}/turboType/{turboTypeId}", method= POST)
    @Secured("ROLE_ALTER_PART")
    public void addTurboType(@PathVariable("partId") long partId, @PathVariable("turboTypeId") long turboTypeId) {
        Part part = partDao.findOne(partId);
        TurboType turboType = turboTypeDao.findOne(turboTypeId);
        part.getTurboTypes().add(turboType);
        partDao.merge(part);
    }

    @Transactional
    @RequestMapping(value="/part/{partId}/turboType/{turboTypeId}", method= DELETE)
    @Secured("ROLE_ALTER_PART")
    public void deleteTurboType(@PathVariable("partId") long partId, @PathVariable("turboTypeId") long turboTypeId) {
        Part part = partDao.findOne(partId);
        
        // Remove any matching turbo types
        Iterator<TurboType> it = part.getTurboTypes().iterator();
        while (it.hasNext()) {
            if (it.next().getId() == turboTypeId) {
                it.remove();
                break;
            }
        }
        
        partDao.merge(part);
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
            @PathVariable("partId") long partId,
            @PathVariable("gasketkitId") long gasketkitId)
    {
        try {
            linkGasketKitToTurbo(gasketkitId, partId);
            return new GasketKitResult();
        } catch(AssertionError e) {
            return new GasketKitResult(e);
        }
    }

    private void linkGasketKitToTurbo(Long gasketKitId, Long turboId) throws AssertionError {
        Part part = partDao.findOne(turboId);
        Long partTypeId = part.getPartType().getId();
        // Validation: Check part type of the "Turbo" part.
        if (partTypeId.longValue() != PTID_TURBO) {
            throw new AssertionError(String.format("Part %s has unexpected part type: %d. Expected a Turbo.",
                    formatPart(part), partTypeId));
        }
        // Validation: Check that Turbo and Gasket Kit art already not linked.
        Turbo turbo = (Turbo) part;
        GasketKit oldGasketKit = (GasketKit) turbo.getGasketKit();
        if (oldGasketKit != null) {
            if (oldGasketKit.getId().equals(gasketKitId)) {
                throw new AssertionError(String.format("Gasket Kit %s already linked with the Turbo %s.",
                        formatPart(oldGasketKit), formatPart(turbo)));
            }
        }
        // Validation: Check part type of the "Gasket Kit" part.
        Part part2 = partDao.findOne(gasketKitId);
        partTypeId = part2.getPartType().getId();
        if (partTypeId.longValue() != PTID_GASKET_KIT) {
            throw new AssertionError(String.format("Part %s has unexpected part type: %d. Expected a Gasket Kit.",
                    formatPart(part2), partTypeId));
        }
        // Validation: gasket kits and associated turbos must have the same manfr_id
        GasketKit newGasketKit = (GasketKit) part2;
        if (!turbo.getManufacturer().getId().equals(newGasketKit.getManufacturer().getId())) {
            throw new AssertionError("The Turbo and Gasket Kit have different manufacturers.");
        }
        // Validation: that all parts in bom of Gasket Kit exist in the BOM of the associated turbo
        Set<Long> turboBomIds = turbo.getBom().stream().map(bi -> bi.getChild().getId()).collect(toSet());
        Set<Long> newGasketKitBomIds = newGasketKit.getBom().stream().map(bi -> bi.getChild().getId()).collect(toSet());
        if (!turboBomIds.containsAll(newGasketKitBomIds)) {
            throw new AssertionError("Not all parts in BOM of the Gasket Kit exist in the BOM of associated Turbo.");
        }
        // Linkage.
        if (oldGasketKit != null) {
            boolean removed = oldGasketKit.getTurbos().remove(turbo);
            if (!removed) {
                log.warn(String.format("Turbo %s not found in turbos of the Gasket Kit %s.",
                        formatPart(turbo), formatPart(oldGasketKit)));
            }
        }
        newGasketKit.getTurbos().add(turbo);
        turbo.setGasketKit(newGasketKit);
        partDao.merge(part);
    }

    @Transactional
    @RequestMapping(value="/part/{partId}/gasketkit", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_ALTER_PART")
    public Turbo clearGasketKitInPart(@PathVariable("partId") long partId) {
        Turbo turbo = (Turbo) partDao.findOne(partId);
        GasketKit gasketKit = (GasketKit) turbo.getGasketKit();
        if (gasketKit != null) {
            boolean removed = gasketKit.getTurbos().remove(turbo);
            if (!removed) {
                log.warn(String.format("Turbo %s not found in turbos of the gasket kit %s.",
                        formatPart(turbo), formatPart(gasketKit)));
            }
        }
        turbo.setGasketKit(null);
        return turbo;
    }

    @Transactional
    @RequestMapping(value="/part/{gasketKitId}/gasketkit/turbos", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.Summary.class)
    public List<Turbo> listTurbosLinkedToGasketKit(@PathVariable("gasketKitId") Long gasketKitId) {
        List<Turbo> retVal = partDao.listTurbosLinkedToGasketKit(gasketKitId);
        if (retVal == null) {
            retVal = new ArrayList<>();
        }
        return retVal;
    }

    @Transactional
    @RequestMapping(value="/part/{partId}/gasketkit2", method = DELETE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(View.Detail.class)
    @Secured("ROLE_ALTER_PART")
    public List<Turbo> unlinkTurboInGasketKit(@PathVariable("partId") long partId) {
        Turbo turbo = (Turbo) partDao.findOne(partId);
        GasketKit gasketKit = (GasketKit) turbo.getGasketKit();
        gasketKit.getTurbos().remove(turbo);
        turbo.setGasketKit(null);
        List<Turbo> retVal = partDao.listTurbosLinkedToGasketKit(gasketKit.getId());
        return retVal;
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
    static class LinkTurboResponse {

        @JsonInclude(ALWAYS)
        static class Row {

            @JsonView({View.Summary.class})
            private final Long partId; // actually the partId is ID of a turbo

            @JsonView({View.Summary.class})
            private final String manufacturerPartNumber;

            @JsonView({View.Summary.class})
            private final boolean success;

            @JsonView({View.Summary.class})
            private final String errorMessage;

            Row(Long partId, String manufacturerPartNumber, boolean success, String errorMessage) {
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

    @Transactional(noRollbackFor = AssertionError.class)
    @Secured("ROLE_ALTER_PART")
    @JsonView(View.Detail.class)
    @ResponseBody
    @RequestMapping(value="/part/{partId}/gasketkits", method = PUT,
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public LinkTurboResponse linkTurbosToGasketKit(@RequestBody LinkTurboRequest request) {
        List<LinkTurboResponse.Row> rows = new ArrayList<>();
        Long gasketKitId = request.getGasketKitId();
        for(Long turboId : request.getPickedTurbos()) {
            boolean success = true;
            String errMsg = null;
            try {
                linkGasketKitToTurbo(gasketKitId, turboId);
            } catch(AssertionError e) {
                success = false;
                errMsg = e.getMessage();
            }
            Part part = partDao.findOne(turboId);
            rows.add(new LinkTurboResponse.Row(turboId, part.getManufacturerPartNumber(), success, errMsg));
        }
        List<Turbo> turbos = partDao.listTurbosLinkedToGasketKit(gasketKitId);
        return new LinkTurboResponse(rows, turbos);
    }

}
