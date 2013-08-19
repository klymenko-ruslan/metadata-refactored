package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/part/kits")
@Controller
@RooWebScaffold(path = "part/kits", formBackingObject = Kit.class)
@RooWebJson(jsonObject = Kit.class)
public class KitController {
}
