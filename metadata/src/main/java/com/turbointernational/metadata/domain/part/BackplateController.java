package com.turbointernational.metadata.domain.part;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/part/backplates")
@Controller
@RooWebScaffold(path = "part/backplates", formBackingObject = Backplate.class)
public class BackplateController {
}
