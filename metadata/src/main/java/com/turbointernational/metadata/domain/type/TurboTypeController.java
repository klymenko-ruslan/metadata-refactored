package com.turbointernational.metadata.domain.type;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/type/turbotypes")
@Controller
@RooWebScaffold(path = "type/turbotypes", formBackingObject = TurboType.class)
public class TurboTypeController {
}
