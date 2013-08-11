package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/part/bearingspacers")
@Controller
@RooWebScaffold(path = "part/bearingspacers", formBackingObject = BearingSpacer.class)
@RooWebJson(jsonObject = BearingSpacer.class)
public class BearingSpacerController {
}
