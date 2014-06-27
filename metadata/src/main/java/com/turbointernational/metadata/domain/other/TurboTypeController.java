package com.turbointernational.metadata.domain.other;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/metadata/other/turboType")
public class TurboTypeController {
    
    @RequestMapping(value = "/manufacturer/{manufacturerId}", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> listJson(@PathVariable("manufacturerId") Long manufacturerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TurboType> result = TurboType.findTurboTypesByManufacturerId(manufacturerId);
        return new ResponseEntity<String>(TurboType.toJsonArray(result), headers, HttpStatus.OK);
    }
}
