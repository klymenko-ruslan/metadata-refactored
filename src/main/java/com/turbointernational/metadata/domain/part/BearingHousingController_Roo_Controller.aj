// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.BearingHousing;
import com.turbointernational.metadata.domain.part.BearingHousingController;
import com.turbointernational.metadata.domain.type.CoolType;
import com.turbointernational.metadata.domain.type.PartType;
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

privileged aspect BearingHousingController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BearingHousingController.create(@Valid BearingHousing bearingHousing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingHousing);
            return "bearinghousings/create";
        }
        uiModel.asMap().clear();
        bearingHousing.persist();
        return "redirect:/bearinghousings/" + encodeUrlPathSegment(bearingHousing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BearingHousingController.createForm(Model uiModel) {
        populateEditForm(uiModel, new BearingHousing());
        return "bearinghousings/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BearingHousingController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("bearinghousing", BearingHousing.findBearingHousing(id));
        uiModel.addAttribute("itemId", id);
        return "bearinghousings/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BearingHousingController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bearinghousings", BearingHousing.findBearingHousingEntries(firstResult, sizeNo));
            float nrOfPages = (float) BearingHousing.countBearingHousings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bearinghousings", BearingHousing.findAllBearingHousings());
        }
        return "bearinghousings/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String BearingHousingController.update(@Valid BearingHousing bearingHousing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bearingHousing);
            return "bearinghousings/update";
        }
        uiModel.asMap().clear();
        bearingHousing.merge();
        return "redirect:/bearinghousings/" + encodeUrlPathSegment(bearingHousing.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String BearingHousingController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BearingHousing.findBearingHousing(id));
        return "bearinghousings/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String BearingHousingController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BearingHousing bearingHousing = BearingHousing.findBearingHousing(id);
        bearingHousing.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/bearinghousings";
    }
    
    void BearingHousingController.populateEditForm(Model uiModel, BearingHousing bearingHousing) {
        uiModel.addAttribute("bearingHousing", bearingHousing);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("cooltypes", CoolType.findAllCoolTypes());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    String BearingHousingController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
