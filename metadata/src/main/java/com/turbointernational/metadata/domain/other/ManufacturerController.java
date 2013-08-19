package com.turbointernational.metadata.domain.other;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/other/manufacturer")
@Controller
@RooWebFinder
@RooWebScaffold(path = "other/manufacturer", formBackingObject = Manufacturer.class)
@RooWebJson(jsonObject=Manufacturer.class)
public class ManufacturerController {
}
