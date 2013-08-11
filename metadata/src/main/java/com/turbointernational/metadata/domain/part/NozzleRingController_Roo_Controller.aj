// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.NozzleRing;
import com.turbointernational.metadata.domain.part.NozzleRingController;
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

privileged aspect NozzleRingController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String NozzleRingController.create(@Valid NozzleRing nozzleRing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, nozzleRing);
            return "part/nozzlerings/create";
        }
        uiModel.asMap().clear();
        nozzleRing.persist();
        return "redirect:/part/nozzlerings/" + encodeUrlPathSegment(nozzleRing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String NozzleRingController.createForm(Model uiModel) {
        populateEditForm(uiModel, new NozzleRing());
        return "part/nozzlerings/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String NozzleRingController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("nozzlering", NozzleRing.findNozzleRing(id));
        uiModel.addAttribute("itemId", id);
        return "part/nozzlerings/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String NozzleRingController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("nozzlerings", NozzleRing.findNozzleRingEntries(firstResult, sizeNo));
            float nrOfPages = (float) NozzleRing.countNozzleRings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("nozzlerings", NozzleRing.findAllNozzleRings());
        }
        return "part/nozzlerings/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String NozzleRingController.update(@Valid NozzleRing nozzleRing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, nozzleRing);
            return "part/nozzlerings/update";
        }
        uiModel.asMap().clear();
        nozzleRing.merge();
        return "redirect:/part/nozzlerings/" + encodeUrlPathSegment(nozzleRing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String NozzleRingController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, NozzleRing.findNozzleRing(id));
        return "part/nozzlerings/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String NozzleRingController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        NozzleRing nozzleRing = NozzleRing.findNozzleRing(id);
        nozzleRing.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/part/nozzlerings";
    }
    
    void NozzleRingController.populateEditForm(Model uiModel, NozzleRing nozzleRing) {
        uiModel.addAttribute("nozzleRing", nozzleRing);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
    }
    
    String NozzleRingController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
