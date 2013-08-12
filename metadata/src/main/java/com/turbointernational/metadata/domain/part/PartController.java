package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.util.ElasticSearch;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/part/parts")
@Controller
@RooWebScaffold(path = "part/parts", formBackingObject = Part.class)
@RooWebJson(jsonObject = Part.class)
public class PartController {

    private static final Logger log = Logger.getLogger(PartController.class.toString());

    void populateEditForm(Model uiModel, Part part) {
        uiModel.addAttribute("part", part);
    }

    // ElasticSearch
    @Autowired(required=true)
    private ElasticSearch elasticSearch;

    @RequestMapping(value="/search", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonSearch(
            @RequestParam(defaultValue="*", required = false) String search,
            @RequestParam(defaultValue = "*", required=false) String partType,
            @RequestParam(defaultValue = "0", required=false) int from,
            @RequestParam(defaultValue = "20", required=false) int size) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        String result = elasticSearch.partSearch(search, partType, from, size);
        return new ResponseEntity<String>(result, headers, HttpStatus.OK);
    }

    @RequestMapping(value="/indexAll", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<Void> jsonIndexAll() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        int pageSize = 100;
        int page = 0;
        Collection<Part> result;
        
        do {
            result = Part.findPartEntries(page * pageSize, pageSize);
            log.log(Level.INFO, "Indexing parts {0}-{1}", new Object[]{page * pageSize, (page * pageSize) + pageSize});
            page++;
            
            elasticSearch.indexParts(result);
        } while (result.size() >= pageSize);

        return new ResponseEntity<Void>((Void) null, headers, HttpStatus.OK);
    }


//    @RequestMapping(value = "search", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> jsonSearchInterchanges(
//            @RequestParam("id") String id,
//            @RequestParam("part_type") String partType,
//            @RequestParam("name") String name,
//            @RequestParam("description") String description,
//            @RequestParam("manufacturer_name") String manufacturerName,
//            @RequestParam("manufacturer_part_number") String manufacturerPartNumber,
//            @RequestParam(value = "page", required = false) Integer page,
//            @RequestParam(value = "size", required = false) Integer size) {
//
//        TypedQuery<Interchange> q = Interchange.searchInterchanges(JSOG);
//        q.setMaxResults(size);
//        q.setFirstResult(page * size);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(Interchange.toJsonArray(q.getResultList()), headers, HttpStatus.OK);
//    }
}
