// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.BearingSpacer;
import com.turbointernational.metadata.domain.part.BearingSpacerController;
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

privileged aspect BearingSpacerController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BearingSpacerController.create(@Valid BearingSpacer bearingSpacer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingSpacer);
            return "part/bearingspacers/create";
        }
        uiModel.asMap().clear();
        bearingSpacer.persist();
        return "redirect:/part/bearingspacers/" + encodeUrlPathSegment(bearingSpacer.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BearingSpacerController.createForm(Model uiModel) {
        populateEditForm(uiModel, new BearingSpacer());
        return "part/bearingspacers/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BearingSpacerController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("bearingspacer", BearingSpacer.findBearingSpacer(id));
        uiModel.addAttribute("itemId", id);
        return "part/bearingspacers/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BearingSpacerController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bearingspacers", BearingSpacer.findBearingSpacerEntries(firstResult, sizeNo));
            float nrOfPages = (float) BearingSpacer.countBearingSpacers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bearingspacers", BearingSpacer.findAllBearingSpacers());
        }
        return "part/bearingspacers/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String BearingSpacerController.update(@Valid BearingSpacer bearingSpacer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingSpacer);
            return "part/bearingspacers/update";
        }
        uiModel.asMap().clear();
        bearingSpacer.merge();
        return "redirect:/part/bearingspacers/" + encodeUrlPathSegment(bearingSpacer.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String BearingSpacerController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BearingSpacer.findBearingSpacer(id));
        return "part/bearingspacers/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String BearingSpacerController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BearingSpacer bearingSpacer = BearingSpacer.findBearingSpacer(id);
        bearingSpacer.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/part/bearingspacers";
    }
    
    void BearingSpacerController.populateEditForm(Model uiModel, BearingSpacer bearingSpacer) {
        uiModel.addAttribute("bearingSpacer", bearingSpacer);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("bearingspacers", BearingSpacer.findAllBearingSpacers());
    }
    
    String BearingSpacerController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
