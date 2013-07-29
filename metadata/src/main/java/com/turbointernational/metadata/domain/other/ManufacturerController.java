package com.turbointernational.metadata.domain.other;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/manufacturer")
@Controller
@RooWebScaffold(path = "manufacturer", formBackingObject = Manufacturer.class)
public class ManufacturerController {
}
