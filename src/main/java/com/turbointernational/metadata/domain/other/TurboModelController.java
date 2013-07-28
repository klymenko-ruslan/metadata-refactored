package com.turbointernational.metadata.domain.other;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/turbomodels")
@Controller
@RooWebScaffold(path = "turbomodels", formBackingObject = TurboModel.class)
public class TurboModelController {
}
