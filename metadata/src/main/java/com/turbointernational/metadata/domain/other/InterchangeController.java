package com.turbointernational.metadata.domain.other;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RooWebFinder
@RooWebJson(jsonObject = Interchange.class)
@RooWebScaffold(path = "other/interchange", formBackingObject = Interchange.class)
@RequestMapping("/other/interchange")
public class InterchangeController {
}