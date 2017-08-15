package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.Application.TEST_SKIPFILEIO;
import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.PART;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART0;
import static com.turbointernational.metadata.entity.ChangelogPart.Role.PART1;
import static com.turbointernational.metadata.entity.Manufacturer.TI_ID;
import static com.turbointernational.metadata.entity.PartType.PTID_GASKET_KIT;
import static com.turbointernational.metadata.entity.PartType.PTID_TURBO;
import static com.turbointernational.metadata.service.ImageService.PART_CRIT_DIM_LEGEND_HEIGHT;
import static com.turbointernational.metadata.service.ImageService.PART_CRIT_DIM_LEGEND_WIDTH;
import static com.turbointernational.metadata.service.ImageService.SIZES;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;
import static java.util.stream.Collectors.toSet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.im4java.core.CommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.Errors;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.ProductImageDao;
import com.turbointernational.metadata.entity.BOMAncestor;
import com.turbointernational.metadata.entity.BOMItem;
import com.turbointernational.metadata.entity.Changelog;
import com.turbointernational.metadata.entity.Manufacturer;
import com.turbointernational.metadata.entity.TurboType;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.entity.part.types.GasketKit;
import com.turbointernational.metadata.entity.part.types.Turbo;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;
import com.turbointernational.metadata.web.controller.PartController;
import com.turbointernational.metadata.web.dto.AlsoBought;
import com.turbointernational.metadata.web.dto.Interchange;
import com.turbointernational.metadata.web.dto.Page;
import com.turbointernational.metadata.web.dto.PartDesc;

import flexjson.JSONSerializer;

/**
 * Created by dmytro.trunykov@zorallabs.com on 12/15/16.
 */
@Service
public class PartService {

    private final static Logger log = LoggerFactory.getLogger(PartController.class);

    @Autowired
    private PartDao partDao;

    @Autowired
    private JdbcTemplate db;

    @Autowired
    private InterchangeService interchangeService;

    @Autowired
    private ChangelogService changelogService;

    @Autowired
    private ChangelogSourceService changelogSourceService;

    @Autowired
    private CriticalDimensionService criticalDimensionService;

    @Autowired
    private BOMService bomService;

    @Autowired
    private ProductImageDao productImageDao;

    @Value("${images.originals}")
    private File originalImagesDir;

    @Autowired
    private ImageService imageService;

    @PersistenceContext(unitName = "metadata")
    private EntityManager em;

    private JSONSerializer partJsonSerializer = new JSONSerializer().include("id").include("name")
            .include("manufacturerPartNumber").include("description").include("inactive").include("partType.id")
            .include("partType.name").exclude("partType.*").include("manufacturer.id").include("manufacturer.name")
            .exclude("manufacturer.*").exclude("bomParentParts").exclude("bom").exclude("interchange").exclude("turbos")
            .exclude("productImages").exclude("*.class");

    @Transactional
    public List<PartController.PartCreateResponse.Row> createPart(HttpServletRequest httpRequest, Part origin,
            List<String> partNumbers, Long[] sourcesIds, Integer[] ratings, String description, Long[] attachIds)
            throws Exception {
        Set<String> added = new HashSet<>(partNumbers.size());
        List<PartController.PartCreateResponse.Row> results = new ArrayList<>(partNumbers.size());
        for (Iterator<String> iter = partNumbers.iterator(); iter.hasNext();) {
            String mpn = iter.next();
            if (added.contains(mpn)) {
                continue; // skip duplicate
            }
            partDao.getEntityManager().detach(origin);
            origin.setId(null);
            origin.setManufacturerPartNumber(mpn);
            partDao.persist(origin);
            // Update the changelog.
            String json = partJsonSerializer.serialize(origin);
            List<RelatedPart> relatedParts = new ArrayList<>(1);
            relatedParts.add(new RelatedPart(origin.getId(), PART0));
            Changelog chlog = changelogService.log(PART, "Created part " + formatPart(origin) + ".", json,
                    relatedParts);
            changelogSourceService.link(httpRequest, chlog, sourcesIds, ratings, description, attachIds);
            results.add(new PartController.PartCreateResponse.Row(origin.getId(), mpn, true, null));
            added.add(mpn);
        }
        return results;
    }

    public Part createXRefPart(Long originalPartId, Part toCreate) throws IOException {
        partDao.persist(toCreate);
        // The table 'part' has a trigger on insert that associate an
        // interchangeable with the part.
        // So we must refresh the Part entity instance just after insert to
        // reflect changes made by the trigger.
        partDao.flush(); // make sure that an insert done
        partDao.refresh(toCreate);
        Long partId = toCreate.getId();
        String json = partJsonSerializer.serialize(toCreate);
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(partId, PART0));
        relatedParts.add(new RelatedPart(originalPartId, PART1));
        changelogService.log(PART, "Created part (cross reference) " + formatPart(toCreate) + ".", json, relatedParts);
        interchangeService.create(partId, originalPartId);
        return toCreate;
    }

    public Part updatePart(HttpServletRequest request, Long id, Part part) throws AssertionError, SecurityException {
        Part originPart = partDao.findOne(id);
        if (originPart.getManufacturer().getId() != part.getManufacturer().getId()
                && !request.isUserInRole("ROLE_ALTER_PART_MANUFACTURER")) {
            throw new SecurityException("You have no permission to modify a manufacturer.");
        }
        if (!originPart.getManufacturerPartNumber().equals(part.getManufacturerPartNumber())
                && !request.isUserInRole("ROLE_ALTER_PART_NUMBER")) {
            throw new SecurityException("You have no permission to modify a manufacturer.");
        }
        Errors errors = criticalDimensionService.validateCriticalDimensions(part);
        if (errors.hasErrors()) {
            log.error("Validation critical dimensions for the part (ID: {}) failed. Details: {}", part.getId(), errors);
            throw new AssertionError(errors.toString());
        }
        String originalPartJson = originPart
                .toJson(criticalDimensionService.getCriticalDimensionForPartType(part.getPartType().getId()));
        Part retVal = partDao.merge(part);
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(part.getId(), PART0));
        changelogService.log(PART, "Updated part " + formatPart(part) + ".",
                "{original: " + originalPartJson + ",updated: "
                        + part.toJson(
                                criticalDimensionService.getCriticalDimensionForPartType(part.getPartType().getId()))
                        + "}",
                relatedParts);
        return retVal;
    }

    /**
     * Update only part details attributes.
     *
     * @param request
     * @param id
     * @param manfrPartNum
     * @param manfrId
     * @param name
     * @param description
     * @param inactive
     * @param dimLength
     * @param dimWidth
     * @param dimHeight
     * @param weight
     * @return
     */
    public Part updatePartDetails(HttpServletRequest request, Long id, String manfrPartNum, Long manfrId, String name,
            String description, Boolean inactive, Double dimLength, Double dimWidth, Double dimHeight, Double weight) {
        Part originPart = partDao.findOne(id);
        if (!originPart.getManufacturer().equals(manfrId) && !request.isUserInRole("ROLE_ALTER_PART_MANUFACTURER")) {
            throw new SecurityException("You have no permission to modify a manufacturer.");
        }
        if (!originPart.getManufacturerPartNumber().equals(manfrPartNum)
                && !request.isUserInRole("ROLE_ALTER_PART_NUMBER")) {
            throw new SecurityException("You have no permission to modify a manufacturer.");
        }
        String originalPartStr = formatPart(originPart);
        String originalPartJson = originPart
                .toJson(criticalDimensionService.getCriticalDimensionForPartType(originPart.getPartType().getId()));
        Manufacturer mnfr = partDao.getEntityManager().getReference(Manufacturer.class, manfrId);
        originPart.setManufacturerPartNumber(manfrPartNum);
        originPart.setManufacturer(mnfr);
        originPart.setName(name);
        originPart.setDescription(description);
        originPart.setInactive(inactive);
        originPart.setDimLength(dimLength);
        originPart.setDimWidth(dimWidth);
        originPart.setDimHeight(dimHeight);
        originPart.setWeight(weight);
        Part retVal = partDao.merge(originPart);
        String updatedPartJson = originPart
                .toJson(criticalDimensionService.getCriticalDimensionForPartType(originPart.getPartType().getId()));
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(id, PART0));
        changelogService.log(PART, "Updated part " + originalPartStr + ".",
                "{original: " + originalPartJson + ",updated: " + updatedPartJson + "}", relatedParts);
        return retVal;
    }

    @Transactional
    public void deletePart(Long id) {
        Part part = partDao.findOne(id);
        partDao.merge(part);
        // Update the changelog
        List<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(part.getId(), PART0));
        changelogService.log(PART, "Deleted part " + formatPart(part) + ".",
                part.toJson(criticalDimensionService.getCriticalDimensionForPartType(part.getPartType().getId())),
                relatedParts);
        // Delete the part
        db.update("INSERT INTO `deleted_parts` (id) VALUES(?)", part.getId());
        partDao.remove(part);
    }

    @Transactional
    public ProductImage addProductImage(Long id, Boolean publish, byte[] imageData) throws Exception {
        Part part = partDao.findOne(id);
        // Save the file into the originals directory
        String filename = part.getId().toString() + "_" + System.currentTimeMillis() + ".jpg"; // Good
                                                                                               // enough
        File originalFile = new File(originalImagesDir, filename);
        if (System.getProperty(TEST_SKIPFILEIO) == null) {
            FileUtils.writeByteArrayToFile(originalFile, imageData);
        }
        // Create the product image
        ProductImage productImage = new ProductImage();
        productImage.setPublish(publish);
        productImage.setMain(false);
        productImage.setFilename(filename);
        productImage.setPart(part);
        productImageDao.persist(productImage);
        // Generate the resized images.
        try {
            for (int size : SIZES) {
                imageService.generateResizedImage(filename, productImage.getFilename(size), size);
            }
        } catch (CommandException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
        return productImage;
    }

    @Transactional
    public Part addCriticalDimensionLegendImage(Long id, byte[] imageData) throws Exception {
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
        FileUtils.writeByteArrayToFile(originalFile, imageData);
        imageService.generateResizedImage(filenameOriginal, filenameScaled, PART_CRIT_DIM_LEGEND_WIDTH,
                PART_CRIT_DIM_LEGEND_HEIGHT, true);
        part.setLegendImgFilename(filenameScaled);
        return part;
    }

    @Transactional
    public Part deleteTurboType(Long partId, Long turboTypeId) {
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
        return part;
    }

    @Secured("ROLE_READ")
    public Page<BOMAncestor> ancestors(Long partId, int offset, int limit) throws Exception {
        return partDao.ancestors(partId, offset, limit);
    }

    @Transactional(noRollbackFor = AssertionError.class)
    public void linkGasketKitToTurbo(Long gasketKitId, Long turboId) throws AssertionError {
        Part part = partDao.findOne(turboId);
        Long partTypeId = part.getPartType().getId();
        // Validation: Check part type of the "Turbo" part.
        if (partTypeId.longValue() != PTID_TURBO) {
            throw new AssertionError(String.format("Part %s has unexpected part type: %d. Expected a Turbo.",
                    formatPart(part), partTypeId));
        }
        // Validation: Check part type of the "Gasket Kit" part.
        Part part2 = partDao.findOne(gasketKitId);
        partTypeId = part2.getPartType().getId();
        if (partTypeId.longValue() != PTID_GASKET_KIT) {
            throw new AssertionError(String.format("Part %s has unexpected part type: %d. Expected a Gasket Kit.",
                    formatPart(part2), partTypeId));
        }
        // Validation: Check that Turbo and Gasket Kit already not linked.
        Turbo turbo = (Turbo) part;
        GasketKit oldGasketKit = turbo.getGasketKit();
        if (oldGasketKit != null) {
            if (oldGasketKit.getId().equals(gasketKitId)) {
                throw new AssertionError(String.format("Gasket Kit %s already linked with the Turbo %s.",
                        formatPart(oldGasketKit), formatPart(turbo)));
            }
        }
        // Validation: gasket kits and associated turbos must have the same
        // manfr_id
        GasketKit newGasketKit = (GasketKit) part2;
        if (!turbo.getManufacturer().getId().equals(newGasketKit.getManufacturer().getId())) {
            throw new AssertionError("The Turbo and Gasket Kit have different manufacturers.");
        }
        // Validation: that all parts in bom of Gasket Kit exist in the BOM of
        // the associated turbo
        Set<Long> turboBomIds = turbo.getBom().stream().map(bi -> bi.getChild().getId()).collect(toSet());
        Set<Long> newGasketKitBomIds = newGasketKit.getBom().stream().map(bi -> bi.getChild().getId()).collect(toSet());
        if (!turboBomIds.containsAll(newGasketKitBomIds)) {
            throw new AssertionError("Not all parts in BOM of the Gasket Kit exist in the BOM of associated Turbo.");
        }
        // Linkage.
        if (oldGasketKit != null) {
            boolean removed = oldGasketKit.getTurbos().remove(turbo);
            if (!removed) {
                log.warn(String.format("Turbo %s not found in turbos of the Gasket Kit %s.", formatPart(turbo),
                        formatPart(oldGasketKit)));
            }
        }
        newGasketKit.getTurbos().add(turbo);
        turbo.setGasketKit(newGasketKit);
        partDao.merge(part);
    }

    @Transactional
    public Turbo clearGasketKitInPart(Long partId) {
        Turbo turbo = (Turbo) partDao.findOne(partId);
        GasketKit gasketKit = turbo.getGasketKit();
        if (gasketKit != null) {
            boolean removed = gasketKit.getTurbos().remove(turbo);
            if (!removed) {
                log.warn(String.format("Turbo %s not found in turbos of the gasket kit %s.", formatPart(turbo),
                        formatPart(gasketKit)));
            }
        }
        turbo.setGasketKit(null);
        return turbo;
    }

    @Transactional
    public List<Turbo> unlinkTurboInGasketKit(Long partId) {
        Turbo turbo = (Turbo) partDao.findOne(partId);
        GasketKit gasketKit = turbo.getGasketKit();
        gasketKit.getTurbos().remove(turbo);
        turbo.setGasketKit(null);
        List<Turbo> retVal = partDao.listTurbosLinkedToGasketKit(gasketKit.getId());
        return retVal;
    }

    @Transactional(noRollbackFor = AssertionError.class)
    public List<PartController.LinkTurboResponse.Row> linkTurbosToGasketKit(Long gasketKitId, List<Long> turboIds) {
        List<PartController.LinkTurboResponse.Row> rows = new ArrayList<>();
        for (Long turboId : turboIds) {
            boolean success = true;
            String errMsg = null;
            try {
                linkGasketKitToTurbo(gasketKitId, turboId);
            } catch (AssertionError e) {
                success = false;
                errMsg = e.getMessage();
            }
            Part part = partDao.findOne(turboId);
            rows.add(new PartController.LinkTurboResponse.Row(turboId, part.getManufacturerPartNumber(), success,
                    errMsg));
        }
        return rows;
    }

    public Page<AlsoBought> filterAlsoBough(String manufacturerPartNumber, String fltrManufacturerPartNumber,
            String fltrPartTypeValue, String sortProperty, String sortOrder, Integer offset, Integer limit) {
        Page<AlsoBought> retVal = partDao.filterAlsoBough(manufacturerPartNumber, fltrManufacturerPartNumber,
                fltrPartTypeValue, sortProperty, sortOrder, offset, limit);
        // Add properties 'partType' and 'name' to the result DTOs.
        if (!retVal.getRecs().isEmpty()) {
            List<AlsoBought> recs = retVal.getRecs();
            List<String> mnfrNmbrs = recs.stream().map(ab -> ab.getManufacturerPartNumber())
                    .collect(Collectors.toList());
            // Map 'manufacturer number' -> 'part'
            List<Part> parts = em.createNamedQuery("findPartsByMnfrsAndNumbers", Part.class)
                    .setParameter("mnfrId", TI_ID).setParameter("mnfrPrtNmbrs", mnfrNmbrs).getResultList();
            Map<String, Part> mnfrNmb2rec = parts.stream()
                    .collect(Collectors.toMap(part -> part.getManufacturerPartNumber(), part -> part));
            recs.forEach(ab -> {
                String mnfrPrtNmb = ab.getManufacturerPartNumber();
                Part p = mnfrNmb2rec.get(mnfrPrtNmb);
                if (p != null) {
                    Long rPartId = p.getId();
                    String rPartName = p.getName();
                    String rPartTypeName = p.getPartType().getName();
                    ab.setPartId(rPartId);
                    ab.setPartTypeName(rPartTypeName);
                    ab.setName(rPartName);
                    Interchange interchange = interchangeService.findForPart(p);
                    List<PartDesc> interchanges = interchange.getParts().stream()
                            .filter(ip -> !ip.getManufacturerPartNumber().equals(mnfrPrtNmb))
                            .map(ip -> new PartDesc(ip.getId(), ip.getManufacturerPartNumber()))
                            .collect(Collectors.toList());
                    ab.setInterchanges(interchanges);
                }
            });
        }
        return retVal;
    }

    public Map<Long, List<Part>> getPartBomsInterchanges(Long partId) throws Exception {
        List<BOMItem> boms = bomService.getByParentId(partId);
        // BOMItem.id => [Part0, Part1, ...]
        Map<Long, List<Part>> retVal = new HashMap<>(boms.size());
        for (BOMItem bi : boms) {
            Long bomId = bi.getId();
            Interchange interchange = interchangeService.findForPart(bi.getChild());
            if (/*interchange != null &&*/ interchange.getParts().size() > 0) {
                List<Part> parts = new ArrayList<>(interchange.getParts().size());
                interchange.getParts().stream().filter(p -> p.getId() != bi.getChild().getId())
                        .forEach(p -> parts.add(p));
                if (!parts.isEmpty()) {
                    retVal.put(bomId, parts);
                }
            }
        }
        return retVal;
    }

}
