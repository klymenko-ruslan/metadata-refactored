package com.turbointernational.metadata.domain.type;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/metadata/type/kit")
public class KitTypeController {
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        KitType kitType = KitType.findKitType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (kitType == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(kitType.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<KitType> result = KitType.findAllKitTypes();
        return new ResponseEntity<String>(KitType.toJsonArray(result), headers, HttpStatus.OK);
    }
}
