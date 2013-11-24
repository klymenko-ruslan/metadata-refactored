package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.util.ElasticSearch;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jsog.JSOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/part")
@Controller
public class PartController {

    private static final Logger log = Logger.getLogger(PartController.class.toString());
    
    // ElasticSearch
    @Autowired(required=true)
    private ElasticSearch elasticSearch;

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
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
    public ResponseEntity<String> listJson(
            @RequestParam(required = false, defaultValue = "1") Integer first,
            @RequestParam(required = false, defaultValue = "20") Integer count,
            @RequestParam(required = false) String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        List<Part> result = Part.findPartEntries(first, count, type);
        
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
    public ResponseEntity<String> createFromJson(Principal principal, @RequestBody String partJson) throws Exception {
        JSOG partJsog = JSOG.parse(partJson);
        
        Part part = Part.fromJsonToPart(partJson);
        
//        // Update the interchange group
//        part.setInterchangeByPartId(partJsog.get("interchangePartId").getLongValue());
        
        part.persist();
        
        // Update the changelog
//        Changelog.log(principal, "Created part", part.toJson());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(part.getId().toString(), headers, HttpStatus.CREATED);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateFromJson(Principal principal, @RequestBody String partJson, @PathVariable("id") Long id) throws Exception {
        JSOG partJsog = JSOG.parse(partJson);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the original part so we can log the update
//        Part originalPart = Part.findPart(id);
//        String originalPartJson = originalPart.toJson();
        
        // Update the part
        Part part = Part.fromJsonToPart(partJson);
//        
//        // Handle BOM updates
//        if (part.getBom() != null) {
//            for (BOMItem item : part.getBom()) {
//                Long childId = item.getChild().getId();
//                item.setChild(Part.findPart(childId));
//                
//                item.setParent(part);
//            }
//        }
//        
//        // Special part type handling
//        if (part instanceof Turbo) {
//            Turbo turbo = (Turbo) part;
//            turbo.setTurboModel(TurboModel.findTurboModel(turbo.getTurboModel().getId()));
//        }
//        
        if (part.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        part = Part.findPart(part.getId());
        Part.entityManager().refresh(part);
//        
//        // Update the interchange group
//        part.setInterchangeByPartId(partJsog.get("interchangePartId").getLongValue());
        
        // Update the changelog
//        JSOG dataJsog = JSOG.object("originalPart", originalPartJson)
//                            .put("updatedPart", part.toJson());
        
//        Changelog.log(principal, "Updated part", dataJsog.toString());
        
        return new ResponseEntity<String>(part.toJson(), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
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

    @RequestMapping(value="/indexAll")
    @ResponseBody
    public ResponseEntity<Void> indexAll(@RequestParam(required=false) Integer maxPages,
                                         @RequestParam(required=false) String type) throws Exception {
        
        if (maxPages == null ) {
            maxPages = Integer.MAX_VALUE;
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        int pageSize = 100;
        int page = 0;
        Collection<Part> result;
        
        do {
            if (type == null) {
                result = Part.findPartEntries(page * pageSize, pageSize);
            } else {
                result = Part.findPartEntries(page * pageSize, pageSize, type);
            }
            log.log(Level.INFO, "Indexing parts {0}-{1}", new Object[]{page * pageSize, (page * pageSize) + pageSize});
            page++;
            
            for (Part part : result) {
                part.indexPart();
            }
            
            elasticSearch.indexParts(result);
        } while (result.size() >= pageSize && page < maxPages);

        return new ResponseEntity<Void>((Void) null, headers, HttpStatus.OK);
    }

}
