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
@RequestMapping("/metadata/type/gasket")
public class GasketTypeController {
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        GasketType gasketType = GasketType.findGasketType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (gasketType == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(gasketType.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<GasketType> result = GasketType.findAllGasketTypes();
        return new ResponseEntity<String>(GasketType.toJsonArray(result), headers, HttpStatus.OK);
    }
}
