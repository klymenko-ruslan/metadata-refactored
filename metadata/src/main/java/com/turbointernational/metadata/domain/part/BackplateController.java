package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.SealType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/part/backplates")
@Controller
@RooWebScaffold(path = "part/backplates", formBackingObject = Backplate.class)
@RooWebJson(jsonObject = Backplate.class)
public class BackplateController {

    void populateEditForm(Model uiModel, Backplate backplate) {
        uiModel.addAttribute("backplate", backplate);
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("sealtypes", SealType.findAllSealTypes());
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Backplate backplate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, backplate);
            return "part/backplates/create";
        }
        uiModel.asMap().clear();
        backplate.updateInterchanges();
        backplate.persist();
        return "redirect:/part/backplates/" + encodeUrlPathSegment(backplate.getId().toString(), httpServletRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Backplate backplate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, backplate);
            return "part/backplates/update";
        }
        uiModel.asMap().clear();
        backplate.updateInterchanges();
        backplate.merge();
        return "redirect:/part/backplates/" + encodeUrlPathSegment(backplate.getId().toString(), httpServletRequest);
    }
}
