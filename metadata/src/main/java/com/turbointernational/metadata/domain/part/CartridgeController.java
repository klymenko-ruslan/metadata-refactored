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

@RequestMapping("/part/cartridges")
@Controller
@RooWebScaffold(path = "part/cartridges", formBackingObject = Cartridge.class)
@RooWebJson(jsonObject = Cartridge.class)
public class CartridgeController {
    
     void populateEditForm(Model uiModel, Cartridge cartridge) {
        uiModel.addAttribute("cartridge", cartridge);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
        
    }
     
     @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Cartridge cartridge, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, cartridge);
            return "part/cartridges/create";
        }
        uiModel.asMap().clear();
        cartridge.updateInterchanges();
        cartridge.persist();
        return "redirect:/part/cartridges/" + encodeUrlPathSegment(cartridge.getId().toString(), httpServletRequest);
    }
    
     @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Cartridge cartridge, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, cartridge);
            return "part/cartridges/update";
        }
        uiModel.asMap().clear();
        cartridge.updateInterchanges();
        cartridge.merge();
        return "redirect:/part/cartridges/" + encodeUrlPathSegment(cartridge.getId().toString(), httpServletRequest);
    }
   
    
    
}
