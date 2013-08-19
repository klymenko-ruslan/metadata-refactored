package com.turbointernational.metadata.domain.part;
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
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BearingHousing bearingHousing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingHousing);
            return "part/bearinghousings/create";
        }
        uiModel.asMap().clear();
        bearingHousing.persist();
        return "redirect:/part/bearinghousings/" + encodeUrlPathSegment(bearingHousing.getId().toString(), httpServletRequest);
    }
}
