package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/part/heatshields")
@Controller
@RooWebScaffold(path = "part/heatshields", formBackingObject = Heatshield.class)
@RooWebJson(jsonObject = Heatshield.class)
public class HeatshieldController {
}
