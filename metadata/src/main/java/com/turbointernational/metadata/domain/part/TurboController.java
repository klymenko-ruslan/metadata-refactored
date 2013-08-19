package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.other.TurboModel;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.type.PartType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/part/turboes")
@Controller
@RooWebScaffold(path = "part/turboes", formBackingObject = Turbo.class)
public class TurboController {
    
    void populateEditForm(Model uiModel, Turbo turbo) {
        uiModel.addAttribute("turbo", turbo);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("turbomodels", TurboModel.findAllTurboModels());
        uiModel.addAttribute("cooltypes", CoolType.findAllCoolTypes());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Turbo turbo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, turbo);
            return "part/turboes/create";
        }
        uiModel.asMap().clear();
        turbo.updateInterchanges();
        turbo.persist();
        return "redirect:/part/turboes/" + encodeUrlPathSegment(turbo.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Turbo turbo, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, turbo);
            return "part/turboes/update";
        }
        uiModel.asMap().clear();
        turbo.updateInterchanges();
        turbo.merge();
        return "redirect:/part/turboes/" + encodeUrlPathSegment(turbo.getId().toString(), httpServletRequest);
    }
}
