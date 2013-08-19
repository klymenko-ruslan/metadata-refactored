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

@RequestMapping("/part/heatshields")
@Controller
@RooWebScaffold(path = "part/heatshields", formBackingObject = Heatshield.class)
@RooWebJson(jsonObject = Heatshield.class)
public class HeatshieldController {
    
    void populateEditForm(Model uiModel, Heatshield heatshield) {
        uiModel.addAttribute("heatshield", heatshield);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Heatshield heatshield, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, heatshield);
            return "part/heatshields/create";
        }
        uiModel.asMap().clear();
        heatshield.persist();
        return "redirect:/part/heatshields/" + encodeUrlPathSegment(heatshield.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Heatshield heatshield, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, heatshield);
            return "part/heatshields/update";
        }
        uiModel.asMap().clear();
        heatshield.merge();
        return "redirect:/part/heatshields/" + encodeUrlPathSegment(heatshield.getId().toString(), httpServletRequest);
    }
}
