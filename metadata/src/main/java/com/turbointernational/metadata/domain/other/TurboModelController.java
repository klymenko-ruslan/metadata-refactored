package com.turbointernational.metadata.domain.other;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/other/turbomodels")
@Controller
@RooWebFinder
@RooWebScaffold(path = "other/turbomodels", formBackingObject = TurboModel.class)
public class TurboModelController {
}
