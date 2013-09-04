package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.changelog.Changelog;
import com.turbointernational.metadata.domain.type.PartType;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
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
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson(
            @RequestParam(required = false, defaultValue = "1") Integer first,
            @RequestParam(required = false, defaultValue = "20") Integer count,
            @RequestParam(required = false) PartType partType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        List<Part> result = Part.findPartEntries(first, count, partType);
        
        return new ResponseEntity<String>(Part.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(Principal principal, @RequestBody String json) {
        
        Part part = Part.fromJsonToPart(json);
        part.persist();
        
        // Update the changelog
        Changelog.log(principal, "Created part", part.toJson());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(Principal principal, @RequestBody String json, @PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        // Get the original part so we can log the update
        String originalPartJson = Part.findPart(id).toJson();
        
        // Update the part
        Part part = Part.fromJsonToPart(json);
        if (part.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
        // Update the changelog
        JSOG dataJsog = JSOG.object("originalPart", originalPartJson)
                            .put("updatedPart", part.toJson());
        
        Changelog.log(principal, "Updated part", dataJsog.toString());
        
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
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
        Changelog.log(principal, "Deleted part", partJson);
        
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value="/indexAll", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<Void> indexAll(@RequestParam(required=false) Integer maxPages,
                                             @RequestParam(required=false) PartType type) throws Exception {
        
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
