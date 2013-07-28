// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.turbointernational.metadata.domain.part;

import com.turbointernational.metadata.domain.interchange.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.part.TurbineWheel;
import com.turbointernational.metadata.domain.part.TurbineWheelController;
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

privileged aspect TurbineWheelController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String TurbineWheelController.create(@Valid TurbineWheel turbineWheel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, turbineWheel);
            return "turbinewheels/create";
        }
        uiModel.asMap().clear();
        turbineWheel.persist();
        return "redirect:/turbinewheels/" + encodeUrlPathSegment(turbineWheel.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String TurbineWheelController.createForm(Model uiModel) {
        populateEditForm(uiModel, new TurbineWheel());
        return "turbinewheels/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String TurbineWheelController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("turbinewheel", TurbineWheel.findTurbineWheel(id));
        uiModel.addAttribute("itemId", id);
        return "turbinewheels/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String TurbineWheelController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("turbinewheels", TurbineWheel.findTurbineWheelEntries(firstResult, sizeNo));
            float nrOfPages = (float) TurbineWheel.countTurbineWheels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("turbinewheels", TurbineWheel.findAllTurbineWheels());
        }
        return "turbinewheels/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String TurbineWheelController.update(@Valid TurbineWheel turbineWheel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, turbineWheel);
            return "turbinewheels/update";
        }
        uiModel.asMap().clear();
        turbineWheel.merge();
        return "redirect:/turbinewheels/" + encodeUrlPathSegment(turbineWheel.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String TurbineWheelController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, TurbineWheel.findTurbineWheel(id));
        return "turbinewheels/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String TurbineWheelController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        TurbineWheel turbineWheel = TurbineWheel.findTurbineWheel(id);
        turbineWheel.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/turbinewheels";
    }
    
    void TurbineWheelController.populateEditForm(Model uiModel, TurbineWheel turbineWheel) {
        uiModel.addAttribute("turbineWheel", turbineWheel);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    String TurbineWheelController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
