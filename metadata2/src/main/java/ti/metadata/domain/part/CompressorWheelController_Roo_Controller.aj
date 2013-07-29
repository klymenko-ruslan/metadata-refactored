// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.part;

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
import ti.metadata.domain.part.CompressorWheel;
import ti.metadata.domain.part.CompressorWheelController;
import ti.metadata.domain.part.Part;

privileged aspect CompressorWheelController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String CompressorWheelController.create(@Valid CompressorWheel compressorWheel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, compressorWheel);
            return "compressorwheels/create";
        }
        uiModel.asMap().clear();
        compressorWheel.persist();
        return "redirect:/compressorwheels/" + encodeUrlPathSegment(compressorWheel.getPartId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String CompressorWheelController.createForm(Model uiModel) {
        populateEditForm(uiModel, new CompressorWheel());
        return "compressorwheels/create";
    }
    
    @RequestMapping(value = "/{partId}", produces = "text/html")
    public String CompressorWheelController.show(@PathVariable("partId") Long partId, Model uiModel) {
        uiModel.addAttribute("compressorwheel", CompressorWheel.findCompressorWheel(partId));
        uiModel.addAttribute("itemId", partId);
        return "compressorwheels/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String CompressorWheelController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("compressorwheels", CompressorWheel.findCompressorWheelEntries(firstResult, sizeNo));
            float nrOfPages = (float) CompressorWheel.countCompressorWheels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("compressorwheels", CompressorWheel.findAllCompressorWheels());
        }
        return "compressorwheels/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String CompressorWheelController.update(@Valid CompressorWheel compressorWheel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, compressorWheel);
            return "compressorwheels/update";
        }
        uiModel.asMap().clear();
        compressorWheel.merge();
        return "redirect:/compressorwheels/" + encodeUrlPathSegment(compressorWheel.getPartId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{partId}", params = "form", produces = "text/html")
    public String CompressorWheelController.updateForm(@PathVariable("partId") Long partId, Model uiModel) {
        populateEditForm(uiModel, CompressorWheel.findCompressorWheel(partId));
        return "compressorwheels/update";
    }
    
    @RequestMapping(value = "/{partId}", method = RequestMethod.DELETE, produces = "text/html")
    public String CompressorWheelController.delete(@PathVariable("partId") Long partId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CompressorWheel compressorWheel = CompressorWheel.findCompressorWheel(partId);
        compressorWheel.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/compressorwheels";
    }
    
    void CompressorWheelController.populateEditForm(Model uiModel, CompressorWheel compressorWheel) {
        uiModel.addAttribute("compressorWheel", compressorWheel);
        uiModel.addAttribute("parts", Part.findAllParts());
    }
    
    String CompressorWheelController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
