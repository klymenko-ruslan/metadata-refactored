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

@RequestMapping("/part/bearingspacers")
@Controller
@RooWebScaffold(path = "part/bearingspacers", formBackingObject = BearingSpacer.class)
@RooWebJson(jsonObject = BearingSpacer.class)
public class BearingSpacerController {
    
    void populateEditForm(Model uiModel, BearingSpacer bearingSpacer) {
        uiModel.addAttribute("bearingSpacer", bearingSpacer);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("bearingspacers", BearingSpacer.findAllBearingSpacers());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
        
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BearingSpacer bearingSpacer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingSpacer);
            return "part/bearingspacers/create";
        }
        uiModel.asMap().clear();
        bearingSpacer.updateInterchanges();
        bearingSpacer.persist();
        return "redirect:/part/bearingspacers/" + encodeUrlPathSegment(bearingSpacer.getId().toString(), httpServletRequest);
        
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid BearingSpacer bearingSpacer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingSpacer);
            return "part/bearingspacers/update";
        }
        uiModel.asMap().clear();
        bearingSpacer.updateInterchanges();
        bearingSpacer.merge();
        return "redirect:/part/bearingspacers/" + encodeUrlPathSegment(bearingSpacer.getId().toString(), httpServletRequest);
    }
    
    
}
