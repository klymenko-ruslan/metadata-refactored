package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/part/backplates")
@Controller
@RooWebScaffold(path = "part/backplates", formBackingObject = Backplate.class)
@RooWebJson(jsonObject = Backplate.class)
public class BackplateController {
}
