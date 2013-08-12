package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.other.Manufacturer;
import com.turbointernational.metadata.domain.type.SealType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.ui.Model;

@RequestMapping("/part/backplates")
@Controller
@RooWebScaffold(path = "part/backplates", formBackingObject = Backplate.class)
@RooWebJson(jsonObject = Backplate.class)
public class BackplateController {

    void populateEditForm(Model uiModel, Backplate backplate) {
        uiModel.addAttribute("backplate", backplate);
        uiModel.addAttribute("manufacturers", Manufacturer.findAllManufacturers());
        uiModel.addAttribute("sealtypes", SealType.findAllSealTypes());
    }
}
