package com.turbointernational.metadata.domain.interchange;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/interchanges")
@Controller
@RooWebScaffold(path = "interchanges", formBackingObject = Interchange.class)
public class InterchangeController {
}
