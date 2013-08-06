// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Backplate;
import com.turbointernational.metadata.domain.part.BackplateController;
import com.turbointernational.metadata.domain.type.SealType;
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

privileged aspect BackplateController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BackplateController.create(@Valid Backplate backplate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, backplate);
            return "part/backplates/create";
        }
        uiModel.asMap().clear();
        backplate.persist();
        return "redirect:/part/backplates/" + encodeUrlPathSegment(backplate.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BackplateController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Backplate());
        return "part/backplates/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BackplateController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("backplate", Backplate.findBackplate(id));
        uiModel.addAttribute("itemId", id);
        return "part/backplates/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BackplateController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("backplates", Backplate.findBackplateEntries(firstResult, sizeNo));
            float nrOfPages = (float) Backplate.countBackplates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("backplates", Backplate.findAllBackplates());
        }
        return "part/backplates/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String BackplateController.update(@Valid Backplate backplate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, backplate);
            return "part/backplates/update";
        }
        uiModel.asMap().clear();
        backplate.merge();
        return "redirect:/part/backplates/" + encodeUrlPathSegment(backplate.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String BackplateController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Backplate.findBackplate(id));
        return "part/backplates/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String BackplateController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Backplate backplate = Backplate.findBackplate(id);
        backplate.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/part/backplates";
    }
    
    void BackplateController.populateEditForm(Model uiModel, Backplate backplate) {
        uiModel.addAttribute("backplate", backplate);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("sealtypes", SealType.findAllSealTypes());
    }
    
    String BackplateController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
