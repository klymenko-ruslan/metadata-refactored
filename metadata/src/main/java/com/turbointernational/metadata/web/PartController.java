package com.turbointernational.metadata.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.other.TurboType;
import com.turbointernational.metadata.domain.other.TurboTypeDao;
import com.turbointernational.metadata.domain.part.*;
import com.turbointernational.metadata.domain.part.bom.BOMAncestor;
import com.turbointernational.metadata.services.BOMService;
import com.turbointernational.metadata.services.CriticalDimensionService;
import com.turbointernational.metadata.services.ImageService;
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
import static com.turbointernational.metadata.services.ImageService.PART_CRIT_DIM_LEGEND_HEIGHT;
import static com.turbointernational.metadata.services.ImageService.PART_CRIT_DIM_LEGEND_WIDTH;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/metadata")
@RestController
public class PartController {

    private final static Logger log = LoggerFactory.getLogger(PartController.class);

    @Autowired
    private BOMService bomService;

    @Autowired
    private ChangelogDao changelogDao;
    
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

    @JsonInclude(ALWAYS)
    static class PartCreateResponse {

        @JsonInclude(ALWAYS)
        static class Row {

            @JsonView({View.Summary.class})
            private final Long partId;

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
    
    @Transactional
    @Secured("ROLE_CREATE_PART")
    @JsonView(View.Detail.class)
    @ResponseBody
    @RequestMapping(value = "/part", method = POST)
    public PartCreateResponse createPart(@RequestBody PartCreateRequest request) throws Exception {
        JSONSerializer jsonSerializer = new JSONSerializer()
                .include("id")
                .include("name")
                .include("manufacturerPartNumber")
                .include("description")
                .include("inactive")
                .include("partType.id")
                .include("partType.name")
                .exclude("partType.*")
                .include("manufacturer.id")
                .include("manufacturer.name")
                .exclude("manufacturer.*")
                .exclude("bomParentParts")
                .exclude("bom")
                .exclude("interchange")
                .exclude("turbos")
                .exclude("productImages")
                .exclude("*.class");
        Part origin = request.getOrigin();
        List<String> partNumbers = request.getPartNumbers();
        Set<String> added = new HashSet<>(partNumbers.size());
        List<PartCreateResponse.Row> results = new ArrayList<>(partNumbers.size());
        for(Iterator<String> iter = partNumbers.iterator(); iter.hasNext();) {
            String mpn = iter.next();
            if (added.contains(mpn)) {
                continue; // skip duplicate
            }
            partDao.getEntityManager().detach(origin);
            origin.setId(null);
            origin.setManufacturerPartNumber(mpn);
            partDao.persist(origin);
            // Update the changelog.
            String json = jsonSerializer.serialize(origin);
            changelogDao.log("Created part", json);
            results.add(new PartCreateResponse.Row(origin.getId(), mpn, true, null));
            added.add(mpn);
        }
        return new PartCreateResponse(results);
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
        String originalPartJson = originPart.toJson(criticalDimensionService.getCriticalDimensionForPartType(part.getPartType().getId()));
        Part retVal = partDao.merge(part);
        // Update the changelog
        changelogDao.log("Updated part", "{original: " + originalPartJson + ",updated: " + part.toJson(criticalDimensionService.getCriticalDimensionForPartType(part.getPartType().getId())) + "}");
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
        changelogDao.log("Deleted part", part.toJson(criticalDimensionService.getCriticalDimensionForPartType(part.getPartType().getId())));
        // Delete the part
        db.update("INSERT INTO `deleted_parts` (id) VALUES(?)", part.getId());
        partDao.remove(part);
    }

    @Transactional
    @RequestMapping(value="/part/{id}/image", method = POST)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<String> addProductImage(@PathVariable Long id, @RequestBody byte[] imageData) throws Exception {
        Part part = partDao.findOne(id);
        // Save the file into the originals directory
        String filename = part.getId().toString() + "_" + System.currentTimeMillis() + ".jpg"; // Good enough
        File originalFile = new File(originalImagesDir, filename);
        FileUtils.writeByteArrayToFile(originalFile, imageData);
        // Create the product image
        ProductImage productImage = new ProductImage();
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

}
