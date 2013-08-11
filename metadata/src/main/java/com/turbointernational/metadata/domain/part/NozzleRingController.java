package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/part/nozzlerings")
@Controller
@RooWebScaffold(path = "part/nozzlerings", formBackingObject = NozzleRing.class)
@RooWebJson(jsonObject = NozzleRing.class)
public class NozzleRingController {
}
