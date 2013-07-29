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
import ti.metadata.domain.part.Gasket;
import ti.metadata.domain.part.GasketController;
import ti.metadata.domain.part.Part;
import ti.metadata.domain.type.GasketType;

privileged aspect GasketController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String GasketController.create(@Valid Gasket gasket, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gasket);
            return "gaskets/create";
        }
        uiModel.asMap().clear();
        gasket.persist();
        return "redirect:/gaskets/" + encodeUrlPathSegment(gasket.getPartId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String GasketController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Gasket());
        return "gaskets/create";
    }
    
    @RequestMapping(value = "/{partId}", produces = "text/html")
    public String GasketController.show(@PathVariable("partId") Long partId, Model uiModel) {
        uiModel.addAttribute("gasket", Gasket.findGasket(partId));
        uiModel.addAttribute("itemId", partId);
        return "gaskets/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String GasketController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("gaskets", Gasket.findGasketEntries(firstResult, sizeNo));
            float nrOfPages = (float) Gasket.countGaskets() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("gaskets", Gasket.findAllGaskets());
        }
        return "gaskets/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String GasketController.update(@Valid Gasket gasket, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gasket);
            return "gaskets/update";
        }
        uiModel.asMap().clear();
        gasket.merge();
        return "redirect:/gaskets/" + encodeUrlPathSegment(gasket.getPartId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{partId}", params = "form", produces = "text/html")
    public String GasketController.updateForm(@PathVariable("partId") Long partId, Model uiModel) {
        populateEditForm(uiModel, Gasket.findGasket(partId));
        return "gaskets/update";
    }
    
    @RequestMapping(value = "/{partId}", method = RequestMethod.DELETE, produces = "text/html")
    public String GasketController.delete(@PathVariable("partId") Long partId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Gasket gasket = Gasket.findGasket(partId);
        gasket.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/gaskets";
    }
    
    void GasketController.populateEditForm(Model uiModel, Gasket gasket) {
        uiModel.addAttribute("gasket", gasket);
        uiModel.addAttribute("parts", Part.findAllParts());
        uiModel.addAttribute("gaskettypes", GasketType.findAllGasketTypes());
    }
    
    String GasketController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
