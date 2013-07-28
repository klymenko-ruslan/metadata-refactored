// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.type;

import com.turbointernational.metadata.domain.type.GasketType;
import com.turbointernational.metadata.domain.type.GasketTypeController;
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

privileged aspect GasketTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String GasketTypeController.create(@Valid GasketType gasketType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gasketType);
            return "gaskettypes/create";
        }
        uiModel.asMap().clear();
        gasketType.persist();
        return "redirect:/gaskettypes/" + encodeUrlPathSegment(gasketType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String GasketTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new GasketType());
        return "gaskettypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String GasketTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("gaskettype", GasketType.findGasketType(id));
        uiModel.addAttribute("itemId", id);
        return "gaskettypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String GasketTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("gaskettypes", GasketType.findGasketTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) GasketType.countGasketTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("gaskettypes", GasketType.findAllGasketTypes());
        }
        return "gaskettypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String GasketTypeController.update(@Valid GasketType gasketType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gasketType);
            return "gaskettypes/update";
        }
        uiModel.asMap().clear();
        gasketType.merge();
        return "redirect:/gaskettypes/" + encodeUrlPathSegment(gasketType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String GasketTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, GasketType.findGasketType(id));
        return "gaskettypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String GasketTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        GasketType gasketType = GasketType.findGasketType(id);
        gasketType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/gaskettypes";
    }
    
    void GasketTypeController.populateEditForm(Model uiModel, GasketType gasketType) {
        uiModel.addAttribute("gasketType", gasketType);
    }
    
    String GasketTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
