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

@RequestMapping("/part/turbinewheels")
@Controller
@RooWebScaffold(path = "part/turbinewheels", formBackingObject = TurbineWheel.class)
public class TurbineWheelController {
    
    void populateEditForm(Model uiModel, TurbineWheel turbineWheel) {
        uiModel.addAttribute("turbineWheel", turbineWheel);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid TurbineWheel turbineWheel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, turbineWheel);
            return "part/turbinewheels/create";
        }
        uiModel.asMap().clear();
        turbineWheel.updateInterchanges();
        turbineWheel.persist();
        return "redirect:/part/turbinewheels/" + encodeUrlPathSegment(turbineWheel.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid TurbineWheel turbineWheel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, turbineWheel);
            return "part/turbinewheels/update";
        }
        uiModel.asMap().clear();
        turbineWheel.updateInterchanges();
        turbineWheel.merge();
        return "redirect:/part/turbinewheels/" + encodeUrlPathSegment(turbineWheel.getId().toString(), httpServletRequest);
    }
}
