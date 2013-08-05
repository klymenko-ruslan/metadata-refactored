package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/part/bearingspacers")
@Controller
@RooWebScaffold(path = "part/bearingspacers", formBackingObject = BearingSpacer.class)
public class BearingSpacerController {
}
