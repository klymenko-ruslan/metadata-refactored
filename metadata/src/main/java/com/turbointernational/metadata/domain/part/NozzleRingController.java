package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/part/nozzlerings")
@Controller
@RooWebScaffold(path = "part/nozzlerings", formBackingObject = NozzleRing.class)
@RooWebJson(jsonObject = NozzleRing.class)
public class NozzleRingController {
    
    void populateEditForm(Model uiModel, NozzleRing nozzleRing) {
        uiModel.addAttribute("nozzleRing", nozzleRing);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid NozzleRing nozzleRing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, nozzleRing);
            return "part/nozzlerings/create";
        }
        uiModel.asMap().clear();
        nozzleRing.updateInterchanges();
        nozzleRing.persist();
        return "redirect:/part/nozzlerings/" + encodeUrlPathSegment(nozzleRing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid NozzleRing nozzleRing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, nozzleRing);
            return "part/nozzlerings/update";
        }
        uiModel.asMap().clear();
        nozzleRing.updateInterchanges();
        nozzleRing.merge();
        return "redirect:/part/nozzlerings/" + encodeUrlPathSegment(nozzleRing.getId().toString(), httpServletRequest);
    }
}
