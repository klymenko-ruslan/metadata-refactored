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

@RequestMapping("/part/journalbearings")
@Controller
@RooWebScaffold(path = "part/journalbearings", formBackingObject = JournalBearing.class)
@RooWebJson(jsonObject = JournalBearing.class)
public class JournalBearingController {
    
    void populateEditForm(Model uiModel, JournalBearing journalBearing) {
        uiModel.addAttribute("journalBearing", journalBearing);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("journalbearings", JournalBearing.findAllJournalBearings());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid JournalBearing journalBearing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, journalBearing);
            return "part/journalbearings/create";
        }
        uiModel.asMap().clear();
        journalBearing.updateInterchanges();
        journalBearing.persist();
        return "redirect:/part/journalbearings/" + encodeUrlPathSegment(journalBearing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid JournalBearing journalBearing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, journalBearing);
            return "part/journalbearings/update";
        }
        uiModel.asMap().clear();
        journalBearing.updateInterchanges();
        journalBearing.merge();
        return "redirect:/part/journalbearings/" + encodeUrlPathSegment(journalBearing.getId().toString(), httpServletRequest);
    }
}
