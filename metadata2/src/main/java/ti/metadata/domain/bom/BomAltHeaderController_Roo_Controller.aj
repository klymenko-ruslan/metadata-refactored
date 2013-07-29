// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package ti.metadata.domain.bom;

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
import ti.metadata.domain.bom.BomAltHeader;
import ti.metadata.domain.bom.BomAltHeaderController;
import ti.metadata.domain.bom.BomAltItem;

privileged aspect BomAltHeaderController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BomAltHeaderController.create(@Valid BomAltHeader bomAltHeader, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bomAltHeader);
            return "bomaltheaders/create";
        }
        uiModel.asMap().clear();
        bomAltHeader.persist();
        return "redirect:/bomaltheaders/" + encodeUrlPathSegment(bomAltHeader.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BomAltHeaderController.createForm(Model uiModel) {
        populateEditForm(uiModel, new BomAltHeader());
        return "bomaltheaders/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BomAltHeaderController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("bomaltheader", BomAltHeader.findBomAltHeader(id));
        uiModel.addAttribute("itemId", id);
        return "bomaltheaders/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BomAltHeaderController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bomaltheaders", BomAltHeader.findBomAltHeaderEntries(firstResult, sizeNo));
            float nrOfPages = (float) BomAltHeader.countBomAltHeaders() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bomaltheaders", BomAltHeader.findAllBomAltHeaders());
        }
        return "bomaltheaders/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String BomAltHeaderController.update(@Valid BomAltHeader bomAltHeader, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bomAltHeader);
            return "bomaltheaders/update";
        }
        uiModel.asMap().clear();
        bomAltHeader.merge();
        return "redirect:/bomaltheaders/" + encodeUrlPathSegment(bomAltHeader.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String BomAltHeaderController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BomAltHeader.findBomAltHeader(id));
        return "bomaltheaders/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String BomAltHeaderController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BomAltHeader bomAltHeader = BomAltHeader.findBomAltHeader(id);
        bomAltHeader.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/bomaltheaders";
    }
    
    void BomAltHeaderController.populateEditForm(Model uiModel, BomAltHeader bomAltHeader) {
        uiModel.addAttribute("bomAltHeader", bomAltHeader);
        uiModel.addAttribute("bomaltitems", BomAltItem.findAllBomAltItems());
    }
    
    String BomAltHeaderController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
