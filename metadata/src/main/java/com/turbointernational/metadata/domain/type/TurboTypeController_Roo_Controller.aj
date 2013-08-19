// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.TurboType;
import com.turbointernational.metadata.domain.type.TurboTypeController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect TurboTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String TurboTypeController.create(@Valid TurboType turboType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, turboType);
            return "type/turbotypes/create";
        }
        uiModel.asMap().clear();
        turboType.persist();
        return "redirect:/type/turbotypes/" + encodeUrlPathSegment(turboType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String TurboTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new TurboType());
        return "type/turbotypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String TurboTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("turbotype", TurboType.findTurboType(id));
        uiModel.addAttribute("itemId", id);
        return "type/turbotypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String TurboTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("turbotypes", TurboType.findTurboTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) TurboType.countTurboTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("turbotypes", TurboType.findAllTurboTypes());
        }
        return "type/turbotypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String TurboTypeController.update(@Valid TurboType turboType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, turboType);
            return "type/turbotypes/update";
        }
        uiModel.asMap().clear();
        turboType.merge();
        return "redirect:/type/turbotypes/" + encodeUrlPathSegment(turboType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String TurboTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, TurboType.findTurboType(id));
        return "type/turbotypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String TurboTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        TurboType turboType = TurboType.findTurboType(id);
        turboType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/type/turbotypes";
    }
    
    void TurboTypeController.populateEditForm(Model uiModel, TurboType turboType) {
        uiModel.addAttribute("turboType", turboType);
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
    }
    
    String TurboTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
