// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.Cartridge;
import com.turbointernational.metadata.domain.part.CartridgeController;
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

privileged aspect CartridgeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String CartridgeController.create(@Valid Cartridge cartridge, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, cartridge);
            return "cartridges/create";
        }
        uiModel.asMap().clear();
        cartridge.persist();
        return "redirect:/cartridges/" + encodeUrlPathSegment(cartridge.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CartridgeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Cartridge());
        return "cartridges/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String CartridgeController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("cartridge", Cartridge.findCartridge(id));
        uiModel.addAttribute("itemId", id);
        return "cartridges/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String CartridgeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("cartridges", Cartridge.findCartridgeEntries(firstResult, sizeNo));
            float nrOfPages = (float) Cartridge.countCartridges() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("cartridges", Cartridge.findAllCartridges());
        }
        return "cartridges/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String CartridgeController.update(@Valid Cartridge cartridge, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, cartridge);
            return "cartridges/update";
        }
        uiModel.asMap().clear();
        cartridge.merge();
        return "redirect:/cartridges/" + encodeUrlPathSegment(cartridge.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String CartridgeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Cartridge.findCartridge(id));
        return "cartridges/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String CartridgeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Cartridge cartridge = Cartridge.findCartridge(id);
        cartridge.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/cartridges";
    }
    
    void CartridgeController.populateEditForm(Model uiModel, Cartridge cartridge) {
        uiModel.addAttribute("cartridge", cartridge);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
    }
    
    String CartridgeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
