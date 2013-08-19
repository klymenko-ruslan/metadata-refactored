package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Interchange;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.PartType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/part/compressorwheels")
@Controller
@RooWebScaffold(path = "part/compressorwheels", formBackingObject = CompressorWheel.class)
@RooWebJson(jsonObject = CompressorWheel.class)
public class CompressorWheelController {
    
    void populateEditForm(Model uiModel, CompressorWheel compressorWheel) {
        uiModel.addAttribute("compressorWheel", compressorWheel);
        uiModel.addAttribute("interchanges", Interchange.findAllInterchanges());
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("parttypes", PartType.findAllPartTypes());
    }
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CompressorWheel compressorWheel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, compressorWheel);
            return "part/compressorwheels/create";
        }
        uiModel.asMap().clear();
        compressorWheel.updateInterchanges();
        compressorWheel.persist();
        return "redirect:/part/compressorwheels/" + encodeUrlPathSegment(compressorWheel.getId().toString(), httpServletRequest);
    }
    
     @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid CompressorWheel compressorWheel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws Exception {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, compressorWheel);
            return "part/compressorwheels/update";
        }
        uiModel.asMap().clear();
        compressorWheel.updateInterchanges();
        compressorWheel.merge();
        return "redirect:/part/compressorwheels/" + encodeUrlPathSegment(compressorWheel.getId().toString(), httpServletRequest);
    }
}
