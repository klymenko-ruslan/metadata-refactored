package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.changelog.Changelog;
import com.turbointernational.metadata.domain.other.TurboType;
import com.turbointernational.metadata.domain.part.bom.BOMAncestor;
import com.turbointernational.metadata.util.ImageResizer;
import flexjson.JSONSerializer;
import flexjson.transformer.HibernateTransformer;
import java.io.File;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/part")
@Controller
public class PartController {

    private static final Logger log = Logger.getLogger(PartController.class.toString());
    
    @Value("${images.originals}")
    File originalImagesDir;
    
    @Autowired(required=true)
    ImageResizer resizer;
    
    @Autowired(required=true)
    JdbcTemplate db;

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> show(@PathVariable("id") Long id) {
        
        Part part = Part.findPart(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (part == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(part.toJson(), headers, HttpStatus.OK);
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
                    ancestor.setPart(Part.findPart(rs.getLong("part_id")));
                    ancestor.setAncestor(Part.findPart(rs.getLong("ancestor_part_id")));

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
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_CREATE_PART")
    public ResponseEntity<String> createFromJson(Principal principal, @RequestBody String partJson) throws Exception {
        Part part = Part.fromJsonToPart(partJson);
        
        part.persist();
        
        // Update the changelog
        Changelog.log("Created part", part.toJson());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(part.getId().toString(), headers, HttpStatus.CREATED);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Secured("ROLE_ALTER_PART")
    public ResponseEntity<String> updateFromJson(Principal principal, @RequestBody String partJson, @PathVariable("id") Long id) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the original part so we can log the update
        Part originalPart = Part.findPart(id);
        String originalPartJson = originalPart.toJson();
        
        // Update the part
        Part part = Part.fromJsonToPart(partJson);
        
        if (part.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        part = Part.findPart(part.getId());
        Part.entityManager().refresh(part);
        
        // Update the changelog
        Changelog.log("Updated part",
            "{original: " + originalPartJson + ",updated: " + part.toJson() + "}");
        
        return new ResponseEntity<String>(part.toJson(), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Secured("ROLE_DELETE_PART")
    public ResponseEntity<String> deleteFromJson(Principal principal, @PathVariable("id") Long id) {
        Part part = Part.findPart(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (part == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        // Update the changelog
        Changelog.log("Deleted part", part.toJson());
        
        // Delete the part
        db.update("INSERT INTO `deleted_parts` (id) VALUES(?)", part.getId());
        part.remove();
        
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value="/all/rebuildBom")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public void rebuildAllBom() throws Exception {
        Part.rebuildBomDescendancy();
    }
    
    @Transactional
    @RequestMapping(value="/{id}/image", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<ProductImage> addProductImage(@PathVariable Long id, @RequestBody byte[] imageData) throws Exception {
        
        // Look up the part
        Part part = Part.findPart(id);
        
        // Save the file into the originals directory
        String filename = part.getId().toString() + "_" + System.currentTimeMillis() + ".jpg"; // Good enough
        File originalFile = new File(originalImagesDir, filename);
        FileUtils.writeByteArrayToFile(originalFile, imageData);
        
        // Create the product image
        ProductImage productImage = new ProductImage();
        productImage.setFilename(filename);
        productImage.setPart(part);
        
        // Save it
        productImage.persist();
        
        // Update the part
        part.getProductImages().add(productImage);
        part.merge();

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
        Part part = Part.findPart(partId);
        TurboType turboType = TurboType.findTurboType(turboTypeId);
        part.getTurboTypes().add(turboType);
        part.merge();
    }
    
    @Transactional
    @RequestMapping(value="/{partId}/turboType/{turboTypeId}", method=RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_ALTER_PART")
    public void deleteTurboType(@PathVariable("partId") long partId, @PathVariable("turboTypeId") long turboTypeId) {
        Part part = Part.findPart(partId);
        
        // Remove any matching turbo types
        Iterator<TurboType> it = part.getTurboTypes().iterator();
        while (it.hasNext()) {
            if (it.next().getId() == turboTypeId) {
                it.remove();
                break;
            }
        }
        
        part.merge();
    }

}
