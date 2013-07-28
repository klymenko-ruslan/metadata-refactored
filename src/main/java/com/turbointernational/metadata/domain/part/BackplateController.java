package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.domain.part.Backplate;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/backplates")
@Controller
@RooWebScaffold(path = "backplates", formBackingObject = Backplate.class)
public class BackplateController {
}
