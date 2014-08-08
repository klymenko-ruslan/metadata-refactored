package com.turbointernational.metadata.domain.other;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/metadata/other/turboModel")
public class TurboModelController {
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<String> listByTurboTypeIdJson(@RequestParam Long turboTypeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TurboModel> result = TurboModel.findTurboModelsByTurboTypeId(turboTypeId);
        return new ResponseEntity<String>(TurboModel.toJsonArray(result), headers, HttpStatus.OK);
    }
}
