package com.turbointernational.metadata.domain.other;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;

@RequestMapping("/other/turbomodels")
@Controller
@RooWebFinder
@RooWebScaffold(path = "other/turbomodels", formBackingObject = TurboModel.class)
@RooWebJson(jsonObject = TurboModel.class)
public class TurboModelController {
}
