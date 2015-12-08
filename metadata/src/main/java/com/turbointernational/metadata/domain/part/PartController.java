package com.turbointernational.metadata.domain.part;

import com.fasterxml.jackson.annotation.JsonView;
import com.turbointernational.metadata.domain.changelog.ChangelogDao;
import com.turbointernational.metadata.domain.other.TurboType;
import com.turbointernational.metadata.domain.other.TurboTypeDao;
import com.turbointernational.metadata.domain.part.bom.BOMAncestor;
import com.turbointernational.metadata.util.ImageResizer;
import com.turbointernational.metadata.web.View;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@RequestMapping("/metadata/part")
@Controller
public class PartController {

    private static final Logger log = Logger.getLogger(PartController.class.toString());
    
    @Autowired
    ChangelogDao changelogDao;
    
    @Autowired
    TurboTypeDao turboTypeDao;
    
    @Autowired
    PartDao partDao;
    
    @Autowired
    PartRepository partRepository;
    
    @Autowired
    ProductImageDao productImageDao;
    
    @Value("${images.originals}")
    File originalImagesDir;
    
    @Autowired(required=true)
    ImageResizer resizer;
    
    @Autowired(required=true)
    JdbcTemplate db;

    @ResponseBody
    @Secured("ROLE_READ")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Part getPart(@PathVariable("id") Long id) {
        return partRepository.findOne(id);
    }
    
    @RequestMapping(value="/{id}/ancestors", method = RequestMethod.GET)
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
            new RowMapper<BOMAncestor>() {
                @Override
                public BOMAncestor mapRow(ResultSet rs, int rowNum) throws SQLException {
                    BOMAncestor ancestor = new BOMAncestor();
                    
                    ancestor.setDistance(rs.getInt("distance"));
                    ancestor.setType(rs.getString("type"));
                    ancestor.setPart(partDao.findOne(rs.getLong("part_id")));
                    ancestor.setAncestor(partDao.findOne(rs.getLong("ancestor_part_id")));

                    return ancestor;
                }
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
        
        return new ResponseEntity<String>(json, headers, HttpStatus.OK);
    }
    
    @Transactional
    @Secured("ROLE_CREATE_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody long createPart(Principal principal, @RequestBody Part part) throws Exception {
        partDao.persist(part);
        
        // Update the changelog
        changelogDao.log("Created part", part.toJson());
        
        return part.getId();
    }

    @Transactional
    @Secured("ROLE_ALTER_PART")
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Part updatePart(Principal principal, @RequestBody Part part, @PathVariable("id") Long id) throws PartNotFoundException {
        
        String originalPartJson = partDao.findOne(id).toJson();
        
        if (partDao.merge(part) == null) {
            throw new PartNotFoundException(id);
        }
        
        partDao.flush();
        
        // Update the changelog
        changelogDao.log("Updated part",
            "{original: " + originalPartJson + ",updated: " + part.toJson() + "}");
        
        return part;
    }
    
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_DELETE_PART")
    public @ResponseBody void deletePart(Principal principal, @PathVariable("id") Long id) throws PartNotFoundException {
        Part part = partDao.findOne(id);
        
        if (partDao.merge(part) == null) {
            throw new PartNotFoundException(id);
        }
        
        // Update the changelog
        changelogDao.log("Deleted part", part.toJson());
        
        // Delete the part
        db.update("INSERT INTO `deleted_parts` (id) VALUES(?)", part.getId());
        partDao.remove(part);
    }

    @Transactional
    @RequestMapping(value="/all/rebuildBom")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void rebuildAllBom() throws Exception {
        partDao.rebuildBomDescendancy();
    }
    
    @Transactional
    @RequestMapping(value="/{id}/image", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<ProductImage> addProductImage(@PathVariable Long id, @RequestBody byte[] imageData) throws Exception {
        
        // Look up the part
        Part part = partDao.findOne(id);
        
        // Save the file into the originals directory
        String filename = part.getId().toString() + "_" + System.currentTimeMillis() + ".jpg"; // Good enough
        File originalFile = new File(originalImagesDir, filename);
        FileUtils.writeByteArrayToFile(originalFile, imageData);
        
        // Create the product image
        ProductImage productImage = new ProductImage();
        productImage.setFilename(filename);
        productImage.setPart(part);
        part.getProductImages().add(productImage);
        
        // Save it
        productImageDao.persist(productImage);

        // Generate the resized images
        for (int size : ImageResizer.SIZES) {
            resizer.generateResizedImage(productImage, size);
        }
        
        return new ResponseEntity(productImage.toJson(), HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value="/{partId}/turboType/{turboTypeId}", method=RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void addTurboType(@PathVariable("partId") long partId, @PathVariable("turboTypeId") long turboTypeId) {
        Part part = partDao.findOne(partId);
        TurboType turboType = turboTypeDao.findOne(turboTypeId);
        part.getTurboTypes().add(turboType);
        partDao.merge(part);
    }
    
    @Transactional
    @RequestMapping(value="/{partId}/turboType/{turboTypeId}", method=RequestMethod.DELETE)
    @ResponseBody
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

    private class PartNotFoundException extends Exception {
        public PartNotFoundException(long partId) {
            super("No part found with id: " + partId);
        }
    }
}
