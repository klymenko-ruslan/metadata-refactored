package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/part/pistonrings")
@Controller
@RooWebScaffold(path = "part/pistonrings", formBackingObject = PistonRing.class)
public class PistonRingController {
    
    void populateEditForm(Model uiModel, PistonRing pistonRing) {
        uiModel.addAttribute("pistonRing", pistonRing);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid PistonRing pistonRing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, pistonRing);
            return "part/pistonrings/create";
        }
        uiModel.asMap().clear();
        pistonRing.persist();
        return "redirect:/part/pistonrings/" + encodeUrlPathSegment(pistonRing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid PistonRing pistonRing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, pistonRing);
            return "part/pistonrings/update";
        }
        uiModel.asMap().clear();
        pistonRing.merge();
        return "redirect:/part/pistonrings/" + encodeUrlPathSegment(pistonRing.getId().toString(), httpServletRequest);
    }
}
