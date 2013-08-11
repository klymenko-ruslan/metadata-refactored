package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/part/gaskets")
@Controller
@RooWebScaffold(path = "part/gaskets", formBackingObject = Gasket.class)
@RooWebJson(jsonObject = Gasket.class)
public class GasketController {
}
