package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/part/turboes")
@Controller
@RooWebScaffold(path = "part/turboes", formBackingObject = Turbo.class)
public class TurboController {
}
