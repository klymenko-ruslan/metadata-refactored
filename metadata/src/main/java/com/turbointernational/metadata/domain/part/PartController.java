package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.bom.BOMItem;
import com.turbointernational.metadata.util.ElasticSearch;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsog.JSOG;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/part")
@Controller
public class PartController {

    private static final Logger log = Logger.getLogger(PartController.class.toString());
    
    // ElasticSearch
    @Autowired(required=true)
    private ElasticSearch elasticSearch;

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        
        Part part = Part.findPart(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (part == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(part.toJson(), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> listJson(
            @RequestParam(required = false, defaultValue = "1") Integer first,
            @RequestParam(required = false, defaultValue = "20") Integer count) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        List<Part> result = Part.findPartEntries(first, count);
        
        String[] fields = new String[] {
            "id",
            "manufacturer.id",
            "manufacturer.name",
            "manufacturer.name",
            "manufacturerPartNumber",
            "partType.id",
            "partType.name"
        };
        
        return new ResponseEntity<String>(Part.toJsonArray(result, fields), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    @Secured("ROLE_CREATE_PART")
    public ResponseEntity<String> createFromJson(Principal principal, @RequestBody String partJson) throws Exception {
        JSOG partJsog = JSOG.parse(partJson);
        
        Part part = Part.fromJsonToPart(partJson);
        
        part.persist();
        part.indexTurbos();
        part.updateIndex();
        
        // Update the changelog
//        Changelog.log(principal, "Created part", part.toJson());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(part.getId().toString(), headers, HttpStatus.CREATED);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Secured("ROLE_ALTER_PART")
    public ResponseEntity<String> updateFromJson(Principal principal, @RequestBody String partJson, @PathVariable("id") Long id) throws Exception {
        JSOG partJsog = JSOG.parse(partJson);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the original part so we can log the update
//        Part originalPart = Part.findPart(id);
//        String originalPartJson = originalPart.toJson();
        
        // Update the part
        Part part = Part.fromJsonToPart(partJson);
        
        if (part.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        part = Part.findPart(part.getId());
        Part.entityManager().refresh(part);
        
        part.indexTurbos();
        part.updateIndex();
        
        // Update the changelog
//        JSOG dataJsog = JSOG.object("originalPart", originalPartJson)
//                            .put("updatedPart", part.toJson());
        
//        Changelog.log(principal, "Updated part", dataJsog.toString());
        
        return new ResponseEntity<String>(part.toJson(), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Secured("ROLE_DELETE_PART")
    public ResponseEntity<String> deleteFromJson(Principal principal, @PathVariable("id") Long id) {
        Part part = Part.findPart(id);
        String partJson = part.toJson();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (part == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        part.remove();
        
        // Update the changelog
//        Changelog.log(principal, "Deleted part", partJson);
        
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}/indexTurbos")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> indexTurbos(@PathVariable("id") Long id) throws Exception {
        Part part = Part.findPart(id);
        
        part.indexTurbos();
        
        return new ResponseEntity<Void>((Void) null, HttpStatus.OK);
    }
    
    @RequestMapping(value="/all/indexTurbos")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> indexTurbos(@RequestParam(required=false) Integer startPage, @RequestParam(required=false) Integer maxPages) throws Exception {
        int pageSize = 100;
        int page = ObjectUtils.defaultIfNull(startPage, 0);
        
        List<Part> parts = Part.findPartEntriesForTurboIndexing(page * pageSize, pageSize);
        do {
            long start = System.currentTimeMillis();
            
            for (Part part : parts) {
                part.indexTurbos();
            }
            
            // Give Hibernate a breather or it'll slow WAY down
            new Part().clear();
            
            // Get the next part list
            page++;
            parts = Part.findPartEntriesForTurboIndexing(page * pageSize, pageSize);
            
            log.log(Level.INFO, "Indexed turbos for {0} parts, page {1} in {2}ms", new Object[]{
                pageSize,
                page,
                System.currentTimeMillis() - start});
        } while (parts.size() == pageSize && (maxPages != null && page < maxPages));
        
        return new ResponseEntity<Void>((Void) null, HttpStatus.OK);
    }
    
    @RequestMapping(value="/all/indexSearch")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> indexSearch(@RequestParam(required=false) Integer maxPages) throws Exception {
        
        if (maxPages == null ) {
            maxPages = Integer.MAX_VALUE;
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        int pageSize = 100;
        int page = 0;
        int result;
        do {
            result = elasticSearch.indexParts(page * pageSize, pageSize);
            log.log(Level.INFO, "Indexing parts {0}-{1}", new Object[]{page * pageSize, (page * pageSize) + pageSize});
            page++;
            
        } while (result >= pageSize && page < maxPages);

        return new ResponseEntity<Void>((Void) null, headers, HttpStatus.OK);
    }
    
    @RequestMapping(value="/{id}addProductImage", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_ADD_IMAGE")
    public ResponseEntity<Void> addProductImage(@PathVariable Long id,
                                                FileUpload upload) throws Exception {
//        TODO

        return new ResponseEntity<Void>((Void) null, HttpStatus.OK);
    }
    
    

}
