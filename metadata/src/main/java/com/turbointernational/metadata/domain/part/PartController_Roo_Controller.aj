// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartController;
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

privileged aspect PartController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String PartController.create(@Valid Part part, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, part);
            return "parts/create";
        }
        uiModel.asMap().clear();
        part.persist();
        return "redirect:/parts/" + encodeUrlPathSegment(part.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String PartController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Part());
        return "parts/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String PartController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("part", Part.findPart(id));
        uiModel.addAttribute("itemId", id);
        return "parts/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String PartController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("parts", Part.findPartEntries(firstResult, sizeNo));
            float nrOfPages = (float) Part.countParts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("parts", Part.findAllParts());
        }
        return "parts/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String PartController.update(@Valid Part part, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, part);
            return "parts/update";
        }
        uiModel.asMap().clear();
        part.merge();
        return "redirect:/parts/" + encodeUrlPathSegment(part.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String PartController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Part.findPart(id));
        return "parts/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String PartController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Part part = Part.findPart(id);
        part.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/parts";
    }
    
    void PartController.populateEditForm(Model uiModel, Part part) {
        uiModel.addAttribute("part", part);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
    }
    
    String PartController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
