package com.turbointernational.metadata.domain.part;
<<<<<<< HEAD
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.type.PartType;
=======
>>>>>>> 7293ee7f1a9d7d32698ca4fced1d7462300784cd
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/part/bearinghousings")
@Controller
@RooWebScaffold(path = "part/bearinghousings", formBackingObject = BearingHousing.class)
@RooWebJson(jsonObject = BearingHousing.class)
public class BearingHousingController {
    
<<<<<<< HEAD
    void populateEditForm(Model uiModel, BearingHousing bearingHousing) {
        uiModel.addAttribute("bearingHousing", bearingHousing);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("cooltypes", CoolType.findAllCoolTypes());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
=======
>>>>>>> 7293ee7f1a9d7d32698ca4fced1d7462300784cd
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BearingHousing bearingHousing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingHousing);
            return "part/bearinghousings/create";
        }
        uiModel.asMap().clear();
        bearingHousing.persist();
        return "redirect:/part/bearinghousings/" + encodeUrlPathSegment(bearingHousing.getId().toString(), httpServletRequest);
<<<<<<< HEAD
    } 
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid BearingHousing bearingHousing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingHousing);
            return "part/bearinghousings/update";
        }
        uiModel.asMap().clear();
        bearingHousing.merge();
        return "redirect:/part/bearinghousings/" + encodeUrlPathSegment(bearingHousing.getId().toString(), httpServletRequest);
=======
>>>>>>> 7293ee7f1a9d7d32698ca4fced1d7462300784cd
    }
}
