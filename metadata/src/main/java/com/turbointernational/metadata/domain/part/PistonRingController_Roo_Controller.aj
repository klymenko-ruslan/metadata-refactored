// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.PistonRing;
import com.turbointernational.metadata.domain.part.PistonRingController;
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

privileged aspect PistonRingController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String PistonRingController.create(@Valid PistonRing pistonRing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, pistonRing);
            return "part/pistonrings/create";
        }
        uiModel.asMap().clear();
        pistonRing.persist();
        return "redirect:/part/pistonrings/" + encodeUrlPathSegment(pistonRing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String PistonRingController.createForm(Model uiModel) {
        populateEditForm(uiModel, new PistonRing());
        return "part/pistonrings/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String PistonRingController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("pistonring", PistonRing.findPistonRing(id));
        uiModel.addAttribute("itemId", id);
        return "part/pistonrings/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String PistonRingController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("pistonrings", PistonRing.findPistonRingEntries(firstResult, sizeNo));
            float nrOfPages = (float) PistonRing.countPistonRings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("pistonrings", PistonRing.findAllPistonRings());
        }
        return "part/pistonrings/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String PistonRingController.update(@Valid PistonRing pistonRing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, pistonRing);
            return "part/pistonrings/update";
        }
        uiModel.asMap().clear();
        pistonRing.merge();
        return "redirect:/part/pistonrings/" + encodeUrlPathSegment(pistonRing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String PistonRingController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, PistonRing.findPistonRing(id));
        return "part/pistonrings/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String PistonRingController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        PistonRing pistonRing = PistonRing.findPistonRing(id);
        pistonRing.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/part/pistonrings";
    }
    
    void PistonRingController.populateEditForm(Model uiModel, PistonRing pistonRing) {
        uiModel.addAttribute("pistonRing", pistonRing);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
    }
    
    String PistonRingController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
