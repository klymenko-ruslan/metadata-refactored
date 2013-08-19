package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.GasketType;
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

@RequestMapping("/part/gaskets")
@Controller
@RooWebScaffold(path = "part/gaskets", formBackingObject = Gasket.class)
@RooWebJson(jsonObject = Gasket.class)
public class GasketController {
    
     void populateEditForm(Model uiModel, Gasket gasket) {
        uiModel.addAttribute("gasket", gasket);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("gaskettypes", GasketType.findAllGasketTypes());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
     
     @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Gasket gasket, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gasket);
            return "part/gaskets/create";
        }
        uiModel.asMap().clear();
        gasket.updateInterchanges();
        gasket.persist();
        return "redirect:/part/gaskets/" + encodeUrlPathSegment(gasket.getId().toString(), httpServletRequest);
    }
    
     @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Gasket gasket, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gasket);
            return "part/gaskets/update";
        }
        uiModel.asMap().clear();
        gasket.updateInterchanges();
        gasket.merge();
        return "redirect:/part/gaskets/" + encodeUrlPathSegment(gasket.getId().toString(), httpServletRequest);
    }
}
