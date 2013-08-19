package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.KitType;
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

@RequestMapping("/part/kits")
@Controller
@RooWebScaffold(path = "part/kits", formBackingObject = Kit.class)
@RooWebJson(jsonObject = Kit.class)
public class KitController {
    
    void populateEditForm(Model uiModel, Kit kit) {
        uiModel.addAttribute("kit", kit);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("kittypes", KitType.findAllKitTypes());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Kit kit, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, kit);
            return "part/kits/create";
        }
        uiModel.asMap().clear();
        kit.updateInterchanges();
        kit.persist();
        return "redirect:/part/kits/" + encodeUrlPathSegment(kit.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Kit kit, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, kit);
            return "part/kits/update";
        }
        uiModel.asMap().clear();
        kit.updateInterchanges();
        kit.merge();
        return "redirect:/part/kits/" + encodeUrlPathSegment(kit.getId().toString(), httpServletRequest);
    }
}
